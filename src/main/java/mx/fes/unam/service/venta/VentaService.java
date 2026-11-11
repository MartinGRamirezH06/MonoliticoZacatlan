package mx.fes.unam.service.venta;

import mx.fes.unam.model.entities.vendedor.VendedorDTO;
import mx.fes.unam.model.entities.vendedor.VendedorEntity;
import mx.fes.unam.model.entities.venta.VentaDTO;
import mx.fes.unam.model.entities.venta.VentaEntity;
import mx.fes.unam.repository.VendedorRepository;
import mx.fes.unam.repository.VentaRepository;
import mx.fes.unam.service.vendedor.VendedorMapper;

import java.util.List;

public class VentaService {
    private final VentaMapper ventaMapper;
    private final VentaRepository ventaRepository;
    //private final BloqueRepository bloqueRepository;

    public VentaService(VentaMapper ventaMapper, VentaRepository ventaRepository){
        this.ventaMapper = ventaMapper;
        this.ventaRepository = ventaRepository;
    }

    public List<VentaDTO> listarVentas(){
        return ventaRepository.findAll()
                .stream()
                .map(vnt -> ventaMapper.toDTO(vnt) )
                .toList();
    }

    public VentaDTO obtenerVenta(Long id){return ventaMapper.toDTO(ventaRepository.findById(id).orElse(null));}

    public VentaDTO guardarVenta(VentaDTO venta){
        VentaEntity vnt = ventaRepository.save(ventaMapper.toEntity(venta));
        venta = ventaMapper.toDTO(vnt);
        return venta;
    }

    public void eliminarVenta(Long id){ventaRepository.deleteById(id);}
}
