package mx.fes.unam.model.entities.bloque;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bloque")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BloqueEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, length = 50)
    private String nombre;

    @Lob
    @Column(nullable = true)
    private String descripcion;

    @Column(nullable = false )
    private Boolean status;
}
