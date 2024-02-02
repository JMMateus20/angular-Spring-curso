import { TestBed } from '@angular/core/testing';

import { DetalleFacturaServiceService } from './detalle-factura-service.service';

describe('DetalleFacturaServiceService', () => {
  let service: DetalleFacturaServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DetalleFacturaServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
