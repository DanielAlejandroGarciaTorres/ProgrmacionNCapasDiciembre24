package com.digis01.DGarciaProgramacionNCapasDiciembre24.RestController;

import com.digis01.DGarciaProgramacionNCapasDiciembre24.ML.Result;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demoapi")
public class DemoRestController {
    
    // Status Code - Alvar, Angela 
    // Vista - Brenda 
    // Cuerpo de Respuesta - Luis
    // Respuesta HTTP - M.Angel, Miguel
    // Texto plano - Jonathan
    
    @GetMapping
    public String Index(){
        return "AlumnoIndex";
    }
    
    @GetMapping("/saludo")
    public String Index2(){
        return "Saludos";
    }
    
    @GetMapping("/saludo/{nombre}")
    public String Index2(@PathVariable String nombre){
        return "Saludos " + nombre;
    }
    
    @GetMapping("/saludos")
    public String Index3(@RequestParam String nombre){
        return "Saludos " + nombre;
    }
    
    @GetMapping("/result")
    public Result result(@RequestParam String isTrue){
        
        Result result = new Result();
        if (isTrue.equals("true")) {
            result.correct = true;
        } else{
            result.correct = false;
            result.errorMessage = "Error en tu consumo";
        }
        return result;
    }
    
    @GetMapping("/resultEntity")
    public ResponseEntity resultEntity(@RequestParam String isTrue){
        
        Result result = new Result();
        if (isTrue.equals("true")) {
            result.correct = true;
            return ResponseEntity.accepted().body(result);
        } else{
            result.correct = false;
            result.errorMessage = "Error en tu consumo";
            return ResponseEntity.badRequest().body(result);
        }
    }
    @GetMapping("/suma/{numeroUno}/{numeroDos}")
    public int Suma1(@PathVariable int numeroUno, @PathVariable int numeroDos){
        return numeroUno + numeroDos;
    }
    
    @GetMapping("/suma")
    public int Suma2(@RequestParam int numeroUno, @RequestParam int numeroDos){
        return numeroUno + numeroDos;
    }
    
    @PostMapping("/suma")
    public int Suma3(@RequestBody List<Integer> numeros){
        int suma = 0;
        for (Integer numero : numeros) {
            suma += numero;
        }
        
        return suma;
    }
    
}
