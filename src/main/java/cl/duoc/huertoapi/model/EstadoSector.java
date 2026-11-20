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

// Tabla de catálogo: estado_sector
@Entity
@Table(name = "estado_sector")
@Getter
@Setter
@NoArgsConstructor
public class EstadoSector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado_sector")
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 20)
    private String nombre;
}
