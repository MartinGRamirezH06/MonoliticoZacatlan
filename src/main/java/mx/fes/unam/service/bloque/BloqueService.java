package mx.fes.unam.service.bloque;

import mx.fes.unam.model.entities.Bloque.BloqueDTO;
import mx.fes.unam.model.entities.Bloque.BloqueEntity;
import mx.fes.unam.repository.BloqueRepository;

import java.util.List;

public class BloqueService {
    private final BloqueMapper bloqueMapper;
    private final BloqueRepository bloqueRepository;

    public BloqueService(BloqueMapper bloqueMapper, BloqueRepository bloqueRepository){
        this.bloqueMapper = bloqueMapper;
        this.bloqueRepository = bloqueRepository;
    }

    public List<BloqueDTO> listarBloques(){
        return bloqueRepository.findAll()
                .stream()
                .map(bl -> bloqueMapper.toDTO(bl) )
                .toList();
    }

    public BloqueDTO obtenerBloque(Long id){return bloqueMapper.toDTO(bloqueRepository.findById(id).orElse(null));}

    public BloqueDTO guardarBloque(BloqueDTO bloque){
        BloqueEntity blq = bloqueRepository.save(bloqueMapper.toEntity(bloque));
        bloque = bloqueMapper.toDTO(blq);
        return bloque;
    }

    public void eliminarLibro(Long id){bloqueRepository.deleteById(id);}

}
