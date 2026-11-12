package mx.fes.unam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ProductoController {
    @GetMapping("producto/listar-productos")
    public String listar_productos(){
        return "producto/lista_producto";
    }

}
