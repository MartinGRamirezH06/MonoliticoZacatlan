package mx.fes.unam.service.vendedor;

import jakarta.persistence.EntityNotFoundException;
import mx.fes.unam.model.entities.bitacora.Accion;
import mx.fes.unam.model.entities.bloque.BloqueEntity;
import mx.fes.unam.model.entities.producto.ProductoDTO;
import mx.fes.unam.model.entities.producto.ProductoEntity;
import mx.fes.unam.model.entities.vendedor.Rol;
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
import java.util.Objects;

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

    /** Lista solo los usuarios de un rol (VENDEDOR o ADMIN). */
    @Transactional(readOnly = true)
    public List<VendedorDTO> listarPorRol(Rol rol) {
        return vendedorRepository.findAll().stream()
                .filter(v -> v.getRol() == rol)
                .map(vendedorMapper::toDTO)
                .toList();
    }

    /** Evita editar/desactivar un usuario desde la sección de otro rol. */
    @Transactional(readOnly = true)
    public void validarRol(Long id, Rol rol) {
        VendedorEntity v = vendedorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + id));
        if (v.getRol() != rol) {
            throw new ReglaNegocioException("Ese usuario no pertenece a esta sección");
        }
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
        if (vendedor.getRol() == Rol.ADMIN) {
            vnd.setBloque(null);   // los administradores no pertenecen a un bloque
        } else {
            ReglaNegocioException.si(vendedor.getBloqueId() == null, "Selecciona un bloque");
            vnd.setBloque(buscarBloqueActivo(vendedor.getBloqueId()));
        }
        vnd.setPassword(vendedor.getPassword());

        VendedorEntity guardado = vendedorRepository.save(vnd);
        bitacoraService.registrar(adminId, "VENDEDOR", guardado.getId(), Accion.CREAR,
                "Correo: " + guardado.getCorreo() + ", Rol: " + guardado.getRol()
                        + (guardado.getBloque() != null ? ", Bloque: " + guardado.getBloque().getId() : ""));
        return vendedorMapper.toDTO(guardado);
    }

    public VendedorDTO actualizarVendedor(Long id, VendedorDTO vendedor, Long adminId) {
        VendedorEntity vnd = vendedorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vendedor no encontrado: " + id));
        if (vendedorRepository.existsByCorreoAndIdNot(vendedor.getCorreo(), id)) {
            throw new ReglaNegocioException("Ya existe un usuario con ese correo");
        }

        StringBuilder cambios = new StringBuilder();
        Long bloqueActual = vnd.getBloque() != null ? vnd.getBloque().getId() : null;
        if (vnd.getRol() == Rol.VENDEDOR && !Objects.equals(bloqueActual, vendedor.getBloqueId())) {
            cambios.append("Bloque: ").append(bloqueActual)
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

    @Transactional(readOnly = true)
    public VendedorDTO autenticar(String correo, String password) {
        VendedorEntity vnd = vendedorRepository.findByCorreoAndStatusTrue(correo)
                .filter(v -> v.getPassword().equals(password))
                .orElseThrow(() -> new ReglaNegocioException("Correo o contraseña incorrectos"));
        return vendedorMapper.toDTO(vnd);
    }

    public void desactivarVendedor(Long id, Long adminId) {
        if (id.equals(adminId)) {
            throw new ReglaNegocioException("No puedes desactivar tu propia cuenta");
        }
        VendedorEntity vendedor =vendedorRepository.findById(id)
               .orElseThrow(() -> new EntityNotFoundException("Vendedor no encontrado: " + id));
        vendedor.setStatus(false);

        bitacoraService.registrar(adminId, "VENDEDOR", id, Accion.DESACTIVAR, null);
    }

    public void reactivarVendedor(Long id, Long adminId) {
        VendedorEntity vendedor = vendedorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vendedor no encontrado: " + id));
        if (vendedor.getBloque() != null && !vendedor.getBloque().getStatus()) {
            throw new ReglaNegocioException("Su bloque está inactivo; reactívalo primero");
        }
        vendedor.setStatus(true);
        bitacoraService.registrar(adminId, "VENDEDOR", id, Accion.REACTIVAR, null);
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
