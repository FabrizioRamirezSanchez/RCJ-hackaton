package hct.ramirez.fabrizio.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "alquileres")
public class AlquilerModel {
    @Id
    private String id;
    private String clienteId;
    private String vehiculoId;
    private Integer dias;
    private String fechaInicio;
    private String fechaFin;
    private Double total;
    private String estado;
    
}
