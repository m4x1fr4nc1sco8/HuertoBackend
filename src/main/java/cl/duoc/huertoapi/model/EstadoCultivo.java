package cl.duoc.huertoapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Tabla de catálogo: estado_cultivo
@Entity
@Table(name = "estado_cultivo")
@Getter
@Setter
@NoArgsConstructor
public class EstadoCultivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado_cultivo")
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 25)
    private String nombre;
}
