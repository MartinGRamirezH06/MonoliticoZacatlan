package mx.fes.unam.service.vendedor;

import mx.fes.unam.model.entities.bloque.BloqueEntity;
import mx.fes.unam.model.entities.producto.ProductoDTO;
import mx.fes.unam.model.entities.producto.ProductoEntity;
import mx.fes.unam.model.entities.vendedor.VendedorDTO;
import mx.fes.unam.model.entities.vendedor.VendedorEntity;
import mx.fes.unam.repository.BloqueRepository;
import mx.fes.unam.repository.ProductoRepository;
import mx.fes.unam.repository.VendedorRepository;
import mx.fes.unam.service.producto.ProductoMapper;

import java.util.List;

public class VendedorService {
    private final VendedorMapper vendedorMapper;
    private final VendedorRepository vendedorRepository;
    //private final BloqueRepository bloqueRepository;

    public VendedorService(VendedorMapper vendedorMapper, VendedorRepository vendedorRepository){
        this.vendedorMapper = vendedorMapper;
        this.vendedorRepository = vendedorRepository;
    }

    public List<VendedorDTO> listarVendedores(){
        return vendedorRepository.findAll()
                .stream()
                .map(vnd -> vendedorMapper.toDTO(vnd) )
                .toList();
    }

    public VendedorDTO obtenerVendedor(Long id){return vendedorMapper.toDTO(vendedorRepository.findById(id).orElse(null));}

    public VendedorDTO guardarVendedor(VendedorDTO vendedor){
        VendedorEntity vnd = vendedorRepository.save(vendedorMapper.toEntity(vendedor));
        vendedor = vendedorMapper.toDTO(vnd);
        return vendedor;
    }

    public void eliminarVendedor(Long id){vendedorRepository.deleteById(id);}
}
