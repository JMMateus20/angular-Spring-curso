package com.bolsadeideas.springboot.backend.apirest.auth.authorization;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import com.bolsadeideas.springboot.backend.apirest.entity.Operacion;
import com.bolsadeideas.springboot.backend.apirest.entity.Usuario;
import com.bolsadeideas.springboot.backend.apirest.exceptions.NotFoundExceptionManaged;
import com.bolsadeideas.springboot.backend.apirest.repository.OperacionRepository;
import com.bolsadeideas.springboot.backend.apirest.repository.UsuarioRepository;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext>{

	@Autowired
	private OperacionRepository operacionRep;
	
	@Autowired
	private UsuarioRepository usuarioRep;
	
	
	@Override
	public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
		HttpServletRequest request=object.getRequest(); //se obtiene la peticion actual
		String url=extractURL(request);//se extrae la url de la peticion actual que está realizando el usuario.
		System.out.println("url de la peticion actual: " + url);
		String metodoHttp=request.getMethod(); //se extrae el metodo HTTP de la peticion actual
		System.out.println("metodo http de la peticion actual: " + metodoHttp);
		boolean isPublic=isPublic(url, metodoHttp);  //se verifica si la peticion es publica
		if (isPublic) {
			return new AuthorizationDecision(true);  //si la operacion es publica no necesita permisos, puede acceder cualquiera
		}
		boolean isGranted=isGranted(url, metodoHttp, authentication.get());
		return new AuthorizationDecision(isGranted);  //si no es pública, se verifica si el usuario esta accediendo a una operación a la cual si tiene permiso. 
	}
	
	
	private String extractURL(HttpServletRequest request) {     //como la aplicacion no tiene un contextpath definido en el archivo de propiedades, no hace falta quitarlo.
		return request.getRequestURI(); //entonces retornamos solamente la URI de la petición.
	}
	
	
	private static Predicate<Operacion> getOperacionPredicate(String url, String metodoHttp){
		return endpoint->{ //recorre los registros de la tabla operaciones para verificar cual de todos coincide con la peticion a la que se quiere acceder desde un cliente.
			String path_base=endpoint.getModulo().getPath_base();
			System.out.println("path_base de la operacion actual: " + path_base);
			Pattern pattern=Pattern.compile(path_base.concat(endpoint.getPath()));
			System.out.println("url de la operacion actual:" + pattern.toString());
			Matcher matcher=pattern.matcher(url);  //verifica si la url creada en la linea anterior coincide con la enviada por parametro, que es la url de la peticion enviada desde el cliente o peticion actual. 
			return matcher.matches() && endpoint.getMetodo_http().equals(metodoHttp);
		};
	}
	
	private boolean isPublic(String url, String metodoHttp) {
		List<Operacion> operacionesPublicas=operacionRep.buscarOperacionesPublicas();
		//recorre los registros de la lista de operacionesPublicas usando el metodo predicate
		//para saber si alguno de ellos coincide con la peticion actual que esta enviando un cliente.
		boolean isPublic=operacionesPublicas.stream().anyMatch(getOperacionPredicate(url, metodoHttp));
		System.out.println("la operacion solicitada es publica: " + isPublic);
		return isPublic;
	}  //si la peticion actual quiere acceder a una operacion publica, va a retornar true.

	private List<Operacion> obtenerOperacionesDeCliente(Authentication auth){
		UsernamePasswordAuthenticationToken authToken=(UsernamePasswordAuthenticationToken) auth;
		String username=(String) authToken.getPrincipal();
		Usuario usuarioBD=usuarioRep.findByUsername(username)
				.orElseThrow(()->new NotFoundExceptionManaged("Usuario no encontrado en la base de datos"));
		return usuarioBD.getRoles().stream()   //se debe retornar todas las operaciones del usuario, y como puede tener varios roles, entonces es necesario hacer un map dentro de otro map
				.flatMap(rol->rol.getOperaciones().stream()
						.map(op->op)).distinct()
				.collect(Collectors.toList());
		
	}
	
	
	private boolean isGranted(String url, String metodoHttp, Authentication auth) {
		if (auth==null || !(auth instanceof UsernamePasswordAuthenticationToken)) {
			throw new AuthenticationCredentialsNotFoundException("Usuario no logeado");
		}
		List<Operacion> operacionesDeUsuario=this.obtenerOperacionesDeCliente(auth);
		System.out.println("operaciones del usuario:");
		operacionesDeUsuario.stream().forEach(op->System.out.println(op.getNombre()));
		boolean isGranted=operacionesDeUsuario.stream().anyMatch(getOperacionPredicate(url, metodoHttp));
		System.out.println("está autorizado: " + isGranted);
		return isGranted;
		//si el usuario esta intentando acceder a una operacion a la cual si está autorizado o tiene los permisos segun sus roles, devuelve true, sino, devuelve false
	}
}
