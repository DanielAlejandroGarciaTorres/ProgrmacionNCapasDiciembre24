package com.digis01.DGarciaProgramacionNCapasDiciembre24.Controller;

import com.digis01.DGarciaProgramacionNCapasDiciembre24.DAO.AlumnoDAOImplementation;
import com.digis01.DGarciaProgramacionNCapasDiciembre24.DAO.EstadoDAOImplementation;
import com.digis01.DGarciaProgramacionNCapasDiciembre24.DAO.MunicipioDAOImplementation;
import com.digis01.DGarciaProgramacionNCapasDiciembre24.DAO.SemestreDAOImplementation;
import com.digis01.DGarciaProgramacionNCapasDiciembre24.ML.Alumno;
import com.digis01.DGarciaProgramacionNCapasDiciembre24.ML.AlumnoDireccion;
import com.digis01.DGarciaProgramacionNCapasDiciembre24.ML.Colonia;
import com.digis01.DGarciaProgramacionNCapasDiciembre24.ML.Direccion;
import com.digis01.DGarciaProgramacionNCapasDiciembre24.ML.Result;
import com.digis01.DGarciaProgramacionNCapasDiciembre24.ML.Semestre;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/Alumno")
public class AlumnoController {

    @Autowired
    private AlumnoDAOImplementation alumnoDAOImplementation;

    @Autowired
    private SemestreDAOImplementation semestreDAOImplementation;

    @Autowired
    private EstadoDAOImplementation estadoDAOImplementation;

    @Autowired
    private MunicipioDAOImplementation municipioDAOImplementation;

    @GetMapping
    public String Index(Model model) {

        Result result = alumnoDAOImplementation.GetAll();

        Alumno alumnoBusqueda = new Alumno();
        alumnoBusqueda.Semestre = new Semestre();
        model.addAttribute("alumnoBusqueda", alumnoBusqueda);

        if (result.correct) {
            model.addAttribute("listaAlumno", result.objects);
        } else {
            model.addAttribute("listaAlumno", null);
        }
        return "AlumnoIndex";
    }

    @PostMapping
    public String Index(@ModelAttribute Alumno alumnoBusqueda, Model model) {
        model.addAttribute("alumnoBusqueda", alumnoBusqueda);
        //alumnoDAOImplementation.GetAllDinamico(alumnoBusqueda);
        return "AlumnoIndex";
    }

    @GetMapping("/form/{idAlumno}")
    public String Form(@PathVariable int idAlumno, Model model) {

        Result resultSemestre = semestreDAOImplementation.GetAll();
        model.addAttribute("semestres", resultSemestre.objects);

        model.addAttribute("estados", estadoDAOImplementation.GetAll().objects);

        if (idAlumno == 0) { // agrearlo
            AlumnoDireccion alumnoDireccion = new AlumnoDireccion();
            alumnoDireccion.Alumno = new Alumno();
            alumnoDireccion.Alumno.Semestre = new Semestre();
//            alumnoDireccion.Direcciones = new Direccion();
//            alumnoDireccion.Direcciones.Colonia = new Colonia();

            model.addAttribute("alumnoDireccion", alumnoDireccion);

            return "AlumnoForm";

        } else { // lo edito
            Result result = alumnoDAOImplementation.DireccionesByIdAlumno(idAlumno);

            model.addAttribute("alumnoDireccion", result.object);
            return "AlumnoDirecciones";
        }

    }

    @PostMapping("/form")
    public String Form(@ModelAttribute AlumnoDireccion alumnoDireccion) {
        //idAlumno == 0  && IdDireccion == 0
        alumnoDAOImplementation.Add(alumnoDireccion);

        return "redirect:/Alumno";
    }

    @GetMapping("/formEditable") //idUsuario y idDireccion
    public String Form(@RequestParam Integer IdAlumno, @RequestParam(required = false) Integer IdDireccion, Model model) {

        if (IdDireccion != null) {
            if (IdAlumno > 0 && IdDireccion > 0) {
                /*consumir GetbyidDireccion */
                //alumnoDAOImplementation.DireccionAlumnoByIdDireccion(IdDireccion)

                AlumnoDireccion alumnoDireccion = new AlumnoDireccion();
                alumnoDireccion.Alumno = new Alumno();
                alumnoDireccion.Alumno.setIdAlumno(14);

                alumnoDireccion.Direccion = new Direccion();
                alumnoDireccion.Direccion.setIdDireccion(4);
                alumnoDireccion.Direccion.setCalle("calle x");
                alumnoDireccion.Direccion.setNumeroInterior("32A");
                model.addAttribute("alumnoDireccion", alumnoDireccion);

                // lleno un alumnoDireccion
                System.out.println(".");
            } else if (IdAlumno > 0 && IdDireccion == 0) {
                /*nueva direccion*/
                //lleno un alumnoDireccion
                System.out.println(".");
            }
        } else {
            // editar un usaurio

        }

//        2.Editar usaurio (direccion nula)
//        3.Agregar nueva dirección
        return "AlumnoForm";
    }

//    @GetMapping("/formEdit?idAlumno=x")
//    public String Form (@ModelAttribute AlumnoDireccion alumnoDireccion){
//        
//        alumnoDAOImplementation.UpdateDireccion(alumnoDireccion);
//        alumnoDAOImplementation.UpdateAlumno
//        return "redirect:/Alumno";
//    }
    @GetMapping("/Delete/{idAlumno}")
    public String Delete(@PathVariable int idAlumno) {

//        alumnoDAOImplementation.Delete()
        return "algo"; // redireccionamiento a mi metodo Index
    }

    @GetMapping("/Direccion/MunicipioByEstado/{IdEstado}")
    @ResponseBody //
    public Result MunicipioByEstado(@PathVariable int IdEstado) {
        Result result = municipioDAOImplementation.MunicipioByEstado(IdEstado);

        return result;
    }

    @GetMapping("/CargaMasiva")
    public String Inicio() {
        return "CargaMasivaIndex";
    }

    @PostMapping("/CargaMasiva")
    public String CargaMasiva(@RequestParam MultipartFile archivo) {

        if (archivo != null && !archivo.isEmpty()) {
            
            // CargaMasiva.txt
            // split -> ["CargaMasiva", "txt"]
            String fileExension = archivo.getOriginalFilename().split("\\.")[1];

            if (fileExension.equals("txt")) {
                ProcesarArchivo(archivo);
            }

        }

        return "";
    }
    
    private boolean ProcesarArchivo(MultipartFile archivo){
        
        try{
            
            InputStream inputStream = archivo.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            
            String linea = "";
            
            while ( (linea = bufferedReader.readLine()) != null ) {                
                // nombre|apaterno| ....
                String[] campos = linea.split("\\|");
                
                AlumnoDireccion alumnoDireccion = new AlumnoDireccion();
                alumnoDireccion.Alumno = new Alumno();
                alumnoDireccion.Alumno.setNombre(campos[0]);
                /*...*/
//                alumnoDireccion.Alumno.setFechaNacimiento(new Date());


//                alumnoDAOImplementation.Add(alumnoDireccion);
            }
            
        }catch (Exception ex){
            return false;
        }
        
        return true;
        
    }

}
