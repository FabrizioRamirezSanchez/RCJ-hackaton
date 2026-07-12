import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { AlquilerService } from './alquiler';

describe('Alquiler', () => {
  let service: AlquilerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(AlquilerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
