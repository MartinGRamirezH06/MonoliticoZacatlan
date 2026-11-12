package mx.fes.unam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BloqueController {
    @GetMapping("/bloque/listar-bloque")
    public String listar_bloque(){
        return"bloque/lista_bloque";
    }
}
