package cl.duoc.huertoapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "cultivo")
@Getter
@Setter
@NoArgsConstructor
public class Cultivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cultivo")
    private Long id;

    @Column(name = "nombre", nullable = false, length = 60)
    private String nombre;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_sector", nullable = false)
    private Sector sector;

    // Estudiante al que se le asigna el cultivo (puede estar sin asignar)
    @ManyToOne
    @JoinColumn(name = "id_estudiante")
    private Usuario estudiante;

    @Column(name = "fecha_siembra", nullable = false)
    private LocalDate fechaSiembra;

    @Column(name = "fecha_cosecha", nullable = false)
    private LocalDate fechaCosecha;

    @Column(name = "fecha_limite")
    private LocalDate fechaLimite;

    @Column(name = "frecuencia_riego_dias", nullable = false)
    private Integer frecuenciaRiegoDias;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_estado_cultivo", nullable = false)
    private EstadoCultivo estado;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_estado_tarea", nullable = false)
    private EstadoTarea estadoTarea;

    @Column(name = "foto_url", length = 255)
    private String fotoUrl;

    @Column(name = "comentario_estudiante", length = 500)
    private String comentarioEstudiante;

    @Column(name = "motivo_rechazo", length = 255)
    private String motivoRechazo;
}
