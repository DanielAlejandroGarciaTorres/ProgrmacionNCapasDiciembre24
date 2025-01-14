package com.digis01.DGarciaProgramacionNCapasDiciembre24.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller // atrapar interaccino de usuario 
@RequestMapping("/demo")
public class DemoController {
    
    @GetMapping
    public String HolaMundo(@RequestParam("nombre") String name, Model model){
        model.addAttribute("saludo", "Hola mundo, soy " + name);
        return "HolaMundo";
    }
    
    @GetMapping("/suma")
    public String Suma(@RequestParam int numero1, @RequestParam int numero2, Model model){
        model.addAttribute("operacion", "Suma");
        model.addAttribute("resultado",numero1 + numero2);
        return "Operaciones";// Operaciones.html
    }
    
}
