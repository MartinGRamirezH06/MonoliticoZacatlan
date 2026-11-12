package mx.fes.unam.service.bitacora;

import mx.fes.unam.model.entities.bitacora.*;
import mx.fes.unam.model.entities.vendedor.VendedorEntity;
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



}