package hct.ramirez.fabrizio.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("clienteId")
    private String clienteId;
    private String vehiculoId;
    private Integer dias;
    private String fechaInicio;
    private String fechaFin;
    private Double total;
    private String estado;
    
}
