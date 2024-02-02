import { Producto } from "./producto"

export class ItemFacturaAux {

    cantidad:number=1;
    producto:Producto;


    calcularImporte():number{
        return this.cantidad*this.producto.precio;
    }


}
