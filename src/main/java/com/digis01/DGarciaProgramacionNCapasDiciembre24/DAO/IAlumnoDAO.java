package com.digis01.DGarciaProgramacionNCapasDiciembre24.DAO;

import com.digis01.DGarciaProgramacionNCapasDiciembre24.ML.AlumnoDireccion;
import com.digis01.DGarciaProgramacionNCapasDiciembre24.ML.Result;

public interface IAlumnoDAO {
    
    Result GetAll(); //método abstracto 
    
    Result Delete(int idAlumno);
    
    Result Add(AlumnoDireccion alumnoDirecccion);
    
    Result DireccionesByIdAlumno( int idAlumno );
    
    Result AlumnoById(int idAlumno);
    
    Result DireccionAlumnoByIdDireccion(int idDireccion);
    
    Result BajaLogicaJPA(int IdAlumno);
    
    
    Result GetAllJPA();
    
    Result AddJPA(AlumnoDireccion alumnoDireccion);
}
