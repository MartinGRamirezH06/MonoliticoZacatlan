package mx.fes.unam.service.bloque;

import jakarta.persistence.EntityNotFoundException;
import mx.fes.unam.model.entities.bitacora.Accion;
import mx.fes.unam.model.entities.bloque.BloqueDTO;
import mx.fes.unam.model.entities.bloque.BloqueEntity;
import mx.fes.unam.repository.BloqueRepository;
import mx.fes.unam.repository.VendedorRepository;
import mx.fes.unam.service.bitacora.BitacoraService;
import mx.fes.unam.util.ReglaNegocioException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BloqueService {
    private final BloqueMapper bloqueMapper;
    private final BloqueRepository bloqueRepository;
    private final BitacoraService bitacoraService;
    private final VendedorRepository vendedorRepository;

    public BloqueService(BloqueMapper bloqueMapper,
                         BloqueRepository bloqueRepository,
                         BitacoraService bitacoraService,
                         VendedorRepository vendedorRepository
    ) {
        this.bloqueMapper = bloqueMapper;
        this.bloqueRepository = bloqueRepository;
        this.bitacoraService = bitacoraService;
        this.vendedorRepository = vendedorRepository;
    }

    @Transactional(readOnly = true)
    public List<BloqueDTO> listarBloques(boolean soloActivos) {
        List<BloqueEntity> bloques = soloActivos
                ? bloqueRepository.findByStatusTrue()
                : bloqueRepository.findAll();
        return bloques.stream().map(bloqueMapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public BloqueDTO obtenerBloque(Long id) {
        return bloqueMapper.toDTO(bloqueRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Bloque no encontrado: " + id)));
    }

    public BloqueDTO crearBloque(BloqueDTO dto, Long adminId) {
        BloqueEntity bl = bloqueMapper.toEntity(dto);
        BloqueEntity guardado = bloqueRepository.save(bl);

        bitacoraService.registrar(adminId, "BLOQUE", guardado.getId(),
                Accion.CREAR, "Nombre: " + guardado.getNombre());
        return bloqueMapper.toDTO(guardado);
    }

    public BloqueDTO actualizarBloque(Long id, BloqueDTO dto, Long adminId) {
        BloqueEntity bl = bloqueRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Bloque no encontrado: " + id));
        String anterior = "Nombre: " + bl.getNombre() + ", Descripción: " + bl.getDescripcion();
        bl.setNombre(dto.getNombre());
        bl.setDescripcion(dto.getDescripcion());

        bitacoraService.registrar(adminId, "BLOQUE", id, Accion.EDITAR,
                "Antes: " + anterior + " | Después: Nombre: " + dto.getNombre()
                        + ", Descripción: " + dto.getDescripcion());
        return bloqueMapper.toDTO(bl);
    }

    public void desactivarBloque(Long id, Long adminId) {
        BloqueEntity bl = bloqueRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Bloque no encontrado: " + id));
        if (vendedorRepository.existsByBloqueIdAndStatusTrue(id)) {
            throw new ReglaNegocioException(
                    "No se puede desactivar: el bloque tiene vendedores activos");
        }
        bl.setStatus(false);
        bitacoraService.registrar(adminId, "BLOQUE", id, Accion.DESACTIVAR, null);
    }

    public void reactivarBloque(Long id, Long adminId) {
        BloqueEntity bl = bloqueRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Bloque no encontrado: " + id))
        ;
        bl.setStatus(true);
        bitacoraService.registrar(adminId, "BLOQUE", id, Accion.REACTIVAR, null);
    }

}
