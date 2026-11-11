package mx.fes.unam.model.entities.Vendedor;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.fes.unam.model.entities.Bloque.BloqueEntity;

@Entity
@Table(name = "vendedor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendedorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, length = 20)
    private String nombre;

    @Column(nullable = true, length = 50)
    private String apellido_paterno;

    @Column(nullable = true, length = 50)
    private String apellido_materno;

    @Column(nullable = true, length = 50)
    private String correo;

    @Column(nullable = false,length = 10)
    private String telefono;

    @Column(nullable = false, length = 8)
    private String rol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bloque_id",referencedColumnName = "id",nullable = false)
    private BloqueEntity bloque;

    @Column(nullable = false, length = 10)
    private String password;

    @Column(nullable = false)
    private Boolean status;
}
