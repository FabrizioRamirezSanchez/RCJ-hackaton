package hct.ramirez.fabrizio.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "clientes")
public class ClienteModel {
    @Id
    private String id;
    private String dni;
    private String nombres;
    private String apellidos;
    private String celular;
    private String correo;
    private String licencia;
    private String estado;
}
