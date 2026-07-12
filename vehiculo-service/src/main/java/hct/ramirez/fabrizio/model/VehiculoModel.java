package hct.ramirez.fabrizio.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "vehiculos")
public class VehiculoModel {
    @Id
    private String id;
    private String placa;
    private String marca;
    private String modelo;
    @JsonProperty("anio")
    private Integer anio;
    private String color;
    private Double precioPorDia;
    private String estado;

    public VehiculoModel() {}

    public VehiculoModel(String id, String placa, String marca, String modelo, Integer anio, String color, Double precioPorDia, String estado) {
        this.id = id;
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.color = color;
        this.precioPorDia = precioPorDia;
        this.estado = estado;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public Integer getAnio() { return anio; }
    public void setAnio(Integer anio) { this.anio = anio; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public Double getPrecioPorDia() { return precioPorDia; }
    public void setPrecioPorDia(Double precioPorDia) { this.precioPorDia = precioPorDia; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
