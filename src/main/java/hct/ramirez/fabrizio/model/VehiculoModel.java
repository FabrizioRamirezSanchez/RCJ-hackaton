package hct.ramirez.fabrizio.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "vehiculos")
public class VehiculoModel {
    @Id
    private String id;
    private String placa;
    private String marca;
    private String modelo;
    private Integer anio;
    private String color;
    private Double precioPorDia;
    private String estado;
}
