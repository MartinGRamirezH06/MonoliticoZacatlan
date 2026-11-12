package mx.fes.unam.service.bitacora;

import mx.fes.unam.model.entities.bitacora.Accion;
import mx.fes.unam.model.entities.bitacora.BitacoraDTO;
import mx.fes.unam.model.entities.bitacora.BitacoraEntity;
import mx.fes.unam.repository.BitacoraRepository;
import mx.fes.unam.repository.VendedorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BitacoraService {

    private final BitacoraRepository bitacoraRepository;
    private final VendedorRepository vendedorRepository;
    private final BitacoraMapper bitacoraMapper;

    public BitacoraService(BitacoraRepository bitacoraRepository,
                           VendedorRepository vendedorRepository,
                           BitacoraMapper bitacoraMapper) {
        this.bitacoraRepository = bitacoraRepository;
        this.vendedorRepository = vendedorRepository;
        this.bitacoraMapper = bitacoraMapper;
    }

    @Transactional
    public void registrar(Long adminId, String entidad, Long entidadId,
                          Accion accion, String detalle) {
        BitacoraEntity registro = BitacoraEntity.builder()
                .admin(vendedorRepository.getReferenceById(adminId))
                .entidad(entidad)
                .entidadId(entidadId)
                .accion(accion)
                .detalle(detalle)
                .fecha(LocalDateTime.now())
                .build();
        bitacoraRepository.save(registro);
    }

    @Transactional(readOnly = true)
    public Page<BitacoraDTO> listar(Pageable pageable) {
        return bitacoraRepository.findAllByOrderByFechaDesc(pageable)
                .map(bitacoraMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public List<BitacoraDTO> historialDe(String entidad, Long entidadId) {
        return bitacoraRepository
                .findByEntidadAndEntidadIdOrderByFechaDesc(entidad.toUpperCase(), entidadId)
                .stream().map(bitacoraMapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<BitacoraDTO> porAdmin(Long adminId) {
        return bitacoraRepository.findByAdminIdOrderByFechaDesc(adminId)
                .stream().map(bitacoraMapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<BitacoraDTO> porFechas(LocalDate desde, LocalDate hasta) {
        return bitacoraRepository
                .findByFechaGreaterThanEqualAndFechaLessThanOrderByFechaDesc(
                        desde.atStartOfDay(), hasta.plusDays(1).atStartOfDay())
                .stream().map(bitacoraMapper::toDTO).toList();
    }
}