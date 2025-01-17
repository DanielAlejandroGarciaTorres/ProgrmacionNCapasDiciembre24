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
import com.digis01.DGarciaProgramacionNCapasDiciembre24.ML.ResultExcel;
import com.digis01.DGarciaProgramacionNCapasDiciembre24.ML.Semestre;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

        if (IdDireccion == null) {
            System.out.println("Editar usuario");
        } else if (IdDireccion == 0) {
            System.out.println("Agregar direccion");
        } else {
            System.out.println("Editar direccion");
        }
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
            } else { // archivo xlsx
                List<AlumnoDireccion> alumnosDireccion = LecturaArchivo(archivo);
                List<ResultExcel> listaErrores = ValidarInformacion(alumnosDireccion);
                
                if (listaErrores.isEmpty()) {
                    /*procesaria mi archivo
                    en vista carga masiva, muestro boton procesar..
                    */
                } else{
                    /*
                        desplegar una tabla con los errores encontrados
                    */
                }
            }

        }

        return "";
    }

    private boolean ProcesarArchivo(MultipartFile archivo) {

        try {

            InputStream inputStream = archivo.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String linea = "";

            while ((linea = bufferedReader.readLine()) != null) {
                // nombre|apaterno| ....
                String[] campos = linea.split("\\|");

                AlumnoDireccion alumnoDireccion = new AlumnoDireccion();
                alumnoDireccion.Alumno = new Alumno();
                alumnoDireccion.Alumno.setNombre(campos[0]);
                /*...*/
//                alumnoDireccion.Alumno.setFechaNacimiento(new Date());

//                alumnoDAOImplementation.Add(alumnoDireccion);
            }

        } catch (Exception ex) {
            return false;
        }

        return true;

    }

    private List<AlumnoDireccion> LecturaArchivo(MultipartFile archivo) {

        List<AlumnoDireccion> listaAlumnosDireccion = new ArrayList<>();
        
        try (XSSFWorkbook workbook = new XSSFWorkbook(archivo.getInputStream())){
            Sheet workSheet = workbook.getSheetAt(0);
            for (Row row : workSheet) {
                AlumnoDireccion alumnoDireccion = new AlumnoDireccion();
                alumnoDireccion.Alumno = new Alumno();
                alumnoDireccion.Alumno.setNombre(row.getCell(0)!= null ? row.getCell(0).toString() : "");
                alumnoDireccion.Alumno.setApellidoPaterno(row.getCell(1).toString());
                /*...*/
                
                listaAlumnosDireccion.add(alumnoDireccion);
            }
        }catch(Exception ex){
            listaAlumnosDireccion = null;
        }
        
        return listaAlumnosDireccion;
    }
    
    
    private List<ResultExcel> ValidarInformacion(List<AlumnoDireccion> alumnosDireccion){
        int fila = 1;
        List<ResultExcel> listaErrores = new ArrayList<>();
        for (AlumnoDireccion alumnoDireccion : alumnosDireccion) {
            
            if(alumnoDireccion.Alumno.getNombre() == null || alumnoDireccion.Alumno.getNombre() == ""){
                
                listaErrores.add(new ResultExcel(fila, alumnoDireccion.Alumno.getNombre(), "Campo obligatorio"));
            }
//            if(alumnoDireccion.Alumno.getNombre() <- no tenga algo que no se una letra  )
            
            fila++;
        }
        
        return listaErrores;
    }

}
