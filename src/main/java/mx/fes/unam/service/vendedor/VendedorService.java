package mx.fes.unam.service.vendedor;

import jakarta.persistence.EntityNotFoundException;
import mx.fes.unam.model.entities.bitacora.Accion;
import mx.fes.unam.model.entities.bloque.BloqueEntity;
import mx.fes.unam.model.entities.producto.ProductoDTO;
import mx.fes.unam.model.entities.producto.ProductoEntity;
import mx.fes.unam.model.entities.vendedor.VendedorDTO;
import mx.fes.unam.model.entities.vendedor.VendedorEntity;
import mx.fes.unam.repository.BloqueRepository;
import mx.fes.unam.repository.ProductoRepository;
import mx.fes.unam.repository.VendedorRepository;
import mx.fes.unam.service.bitacora.BitacoraService;
import mx.fes.unam.service.producto.ProductoMapper;
import mx.fes.unam.util.ReglaNegocioException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class VendedorService {
    private final VendedorMapper vendedorMapper;
    private final VendedorRepository vendedorRepository;
    private final BloqueRepository bloqueRepository;
    private final BitacoraService bitacoraService;


    public VendedorService(VendedorMapper vendedorMapper,
                           VendedorRepository vendedorRepository,
                           BloqueRepository bloqueRepository,
                           BitacoraService bitacoraService){
        this.vendedorMapper = vendedorMapper;
        this.vendedorRepository = vendedorRepository;
        this.bloqueRepository = bloqueRepository;
        this.bitacoraService = bitacoraService;
    }

    @Transactional(readOnly = true)
    public List<VendedorDTO> listarVendedores(boolean soloActivos) {
        List<VendedorEntity> lista = soloActivos ? vendedorRepository.findByStatusTrue()
                : vendedorRepository.findAll();
        return lista.stream().map(vendedorMapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public VendedorDTO obtenerVendedor(Long id){
        return vendedorMapper.toDTO(vendedorRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Vendedor no encontrado: " + id)));
    }

    public VendedorDTO crearVendedor(VendedorDTO vendedor, Long adminId) {
        if (vendedor.getPassword() == null || vendedor.getPassword().isBlank()) {
            throw new ReglaNegocioException("La contraseña es obligatoria");
        }
        if (vendedorRepository.existsByCorreo(vendedor.getCorreo())) {
            throw new ReglaNegocioException("Ya existe un usuario con ese correo");
        }
        VendedorEntity vnd = vendedorMapper.toEntity(vendedor);
        vnd.setBloque(buscarBloqueActivo(vendedor.getBloqueId()));
        vnd.setPassword(vendedor.getPassword());

        VendedorEntity guardado = vendedorRepository.save(vnd);
        bitacoraService.registrar(adminId, "VENDEDOR", guardado.getId(), Accion.CREAR,
                "Correo: " + guardado.getCorreo() + ", Rol: " + guardado.getRol()
                        + ", Bloque: " + guardado.getBloque().getId());
        return vendedorMapper.toDTO(guardado);
    }

    public VendedorDTO actualizarVendedor(Long id, VendedorDTO vendedor, Long adminId) {
        VendedorEntity vnd = vendedorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vendedor no encontrado: " + id));
        if (vendedorRepository.existsByCorreoAndIdNot(vendedor.getCorreo(), id)) {
            throw new ReglaNegocioException("Ya existe un usuario con ese correo");
        }

        StringBuilder cambios = new StringBuilder();
        if (!vnd.getBloque().getId().equals(vendedor.getBloqueId())) {
            cambios.append("Bloque: ").append(vnd.getBloque().getId())
                    .append(" -> ").append(vendedor.getBloqueId()).append("; ");
            vnd.setBloque(buscarBloqueActivo(vendedor.getBloqueId()));
        }
        if (vnd.getRol() != vendedor.getRol()) {
            cambios.append("Rol: ").append(vnd.getRol()).append(" -> ").append(vendedor.getRol()).append("; ");
            vnd.setRol(vendedor.getRol());
        }
        if (!vnd.getCorreo().equals(vendedor.getCorreo())) {
            cambios.append("Correo: ").append(vnd.getCorreo()).append(" -> ").append(vendedor.getCorreo()).append("; ");
            vnd.setCorreo(vendedor.getCorreo());
        }

        vnd.setNombre(vendedor.getNombre());
        vnd.setApellido_paterno(vendedor.getApellido_paterno());
        vnd.setApellido_materno(vendedor.getApellido_materno());
        vnd.setTelefono(vendedor.getTelefono());

        if (vendedor.getPassword() != null && !vendedor.getPassword().isBlank()) {
            vnd.setPassword(vendedor.getPassword());
            cambios.append("Contraseña modificada; ");
        }

        bitacoraService.registrar(adminId, "VENDEDOR", id, Accion.EDITAR,
                cambios.length() > 0 ? cambios.toString() : "Datos personales");
        return vendedorMapper.toDTO(vnd);
    }

    private BloqueEntity buscarBloqueActivo(Long bloqueId) {
        BloqueEntity bloque = bloqueRepository.findById(bloqueId)
                .orElseThrow(() -> new EntityNotFoundException("Bloque no encontrado: " + bloqueId));
        if (!bloque.getStatus()) {
            throw new ReglaNegocioException("El bloque está inactivo");
        }
        return bloque;
    }

}
