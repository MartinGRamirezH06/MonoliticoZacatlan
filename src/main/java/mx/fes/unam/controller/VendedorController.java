package mx.fes.unam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VendedorController {
    @GetMapping("vendedor/listar-vendedores")
    public String listar_vendores(){
        return "vendedor/listar_vendedores";
    }
}
