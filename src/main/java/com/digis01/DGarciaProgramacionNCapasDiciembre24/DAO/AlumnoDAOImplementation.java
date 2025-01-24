package com.digis01.DGarciaProgramacionNCapasDiciembre24.DAO;

import com.digis01.DGarciaProgramacionNCapasDiciembre24.ML.Alumno;
import com.digis01.DGarciaProgramacionNCapasDiciembre24.ML.AlumnoDireccion;
import com.digis01.DGarciaProgramacionNCapasDiciembre24.ML.Colonia;
import com.digis01.DGarciaProgramacionNCapasDiciembre24.ML.Direccion;
import com.digis01.DGarciaProgramacionNCapasDiciembre24.ML.Municipio;
import com.digis01.DGarciaProgramacionNCapasDiciembre24.ML.Result;
import com.digis01.DGarciaProgramacionNCapasDiciembre24.ML.Semestre;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.sql.ResultSet;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository // logica de base de datos
public class AlumnoDAOImplementation implements IAlumnoDAO {

    @Autowired //Inyección dependencias (field, contructor, setter)
    private JdbcTemplate jdbcTemplate; // conexión directa 
    
    @Autowired
    private EntityManager entityManager; // conexión jpa

    @Override
    public Result GetAll() {
        Result result = new Result();
        try {
            jdbcTemplate.execute("{CALL AlumnoGetAll(?)}",
                    (CallableStatementCallback<Integer>) callableStatement -> {
                        callableStatement.registerOutParameter(1, Types.REF_CURSOR);
                        callableStatement.execute();
                        ResultSet resultSet = (ResultSet) callableStatement.getObject(1);

                        List<AlumnoDireccion> alumnos = new ArrayList<>();
                        result.objects = new ArrayList<>();
                        while (resultSet.next()) {

                            int idAlumno = resultSet.getInt("IdAlumno");
                            /*
                            result.objects -- lista de alumno y direcciones
                            result.objects.get(i) -- objeto - alumno y direcciones
                             */
                            if (!result.objects.isEmpty() && idAlumno == ((AlumnoDireccion) (result.objects.get(result.objects.size() - 1))).Alumno.getIdAlumno()) {
                                // cuando ya existe un usuario con más de una direccio

                                Direccion direccion = new Direccion();
                                direccion.setCalle(resultSet.getString("Calle"));
                                direccion.Colonia = new Colonia();
                                direccion.Colonia.setNombre(resultSet.getString("NombreColonia"));
                                direccion.Colonia.Municipio = new Municipio();

                                ((AlumnoDireccion) (result.objects.get(result.objects.size() - 1))).Direcciones.add(direccion);

                            } else { // asigna a uno nuevo
                                AlumnoDireccion alumnoDireccion = new AlumnoDireccion();

                                alumnoDireccion.Alumno = new Alumno();
                                alumnoDireccion.Alumno.setIdAlumno(idAlumno);
                                alumnoDireccion.Alumno.setNombre(resultSet.getString("NombreAlumno"));
                                alumnoDireccion.Alumno.setApellidoPaterno(resultSet.getString("ApellidoPaterno"));
                                alumnoDireccion.Alumno.setApellidoMaterno(resultSet.getString("ApellidoMaterno"));
                                alumnoDireccion.Alumno.Semestre = new Semestre();
                                alumnoDireccion.Alumno.Semestre.setIdSemestre(resultSet.getInt("IdSemestre"));
                                alumnoDireccion.Alumno.Semestre.setNombre(resultSet.getString("NombreSemestre"));
                                alumnoDireccion.Alumno.setTelefono(resultSet.getString("Telefono"));
                                alumnoDireccion.Alumno.setUserName(resultSet.getString("UserName"));
                                alumnoDireccion.Alumno.setPassword(resultSet.getString("Password"));

                                /* Usuario, Rol y Direccion*/
                                alumnoDireccion.Direcciones = new ArrayList<>();
                                Direccion direccion = new Direccion();
                                direccion.setCalle(resultSet.getString("Calle"));
                                direccion.Colonia = new Colonia();
                                direccion.Colonia.setNombre(resultSet.getString("NombreColonia"));
                                direccion.Colonia.Municipio = new Municipio();

                                alumnoDireccion.Direcciones.add(direccion);
                                result.objects.add(alumnoDireccion);
                            }
                        }
                        result.correct = true;
                        return 1;
                    });

//            result.object = listaAlumnos;
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
            result.objects = null;
        }

        return result;
    }

    @Override
    public Result Delete(int idAlumno) {
        return null;
    }

    @Override
    public Result Add(AlumnoDireccion alumnoDirecccion) {

        Result result = new Result();
        try {
            jdbcTemplate.execute("{CALL AlumnoAdd(?,?,?,?,?,?)}",
                    (CallableStatementCallback<Integer>) callableStatement -> {

                        callableStatement.setInt(1, alumnoDirecccion.Alumno.getIdAlumno());
                        callableStatement.setString(2, alumnoDirecccion.Alumno.getNombre());
                        //***
                        callableStatement.setDate(6, new java.sql.Date(alumnoDirecccion.Alumno.getFechaNacimiento().getTime()));

                        return 1;
                    });

//            result.object = listaAlumnos;
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
            result.objects = null;
        }

        return result;
    }

    @Override
    public Result DireccionesByIdAlumno(int idAlumno) {
        Result result = new Result();
        try {
            jdbcTemplate.execute("{CALL AlumnoDirecciones(?,?,?)}",
                    (CallableStatementCallback<Integer>) callableStatement -> {
                        callableStatement.setInt(1, idAlumno);
                        callableStatement.registerOutParameter(2, Types.REF_CURSOR);
                        callableStatement.registerOutParameter(3, Types.REF_CURSOR);
                        callableStatement.execute();
                        ResultSet resultSetAlumno = (ResultSet) callableStatement.getObject(2);
                        ResultSet resultSetDirecciones = (ResultSet) callableStatement.getObject(3);

                        if (resultSetAlumno.next()) {
                            AlumnoDireccion alumnoDireccion = new AlumnoDireccion();

                            alumnoDireccion.Alumno = new Alumno();
                            alumnoDireccion.Alumno.setIdAlumno(resultSetAlumno.getInt("IdAlumno"));
                            alumnoDireccion.Alumno.setNombre(resultSetAlumno.getString("Nombre"));
                            alumnoDireccion.Alumno.setApellidoPaterno(resultSetAlumno.getString("ApellidoPaterno"));
                            alumnoDireccion.Alumno.setApellidoMaterno(resultSetAlumno.getString("ApellidoMaterno"));

                            alumnoDireccion.Direcciones = new ArrayList<>();

                            while (resultSetDirecciones.next()) {
                                Direccion direccion = new Direccion();
                                direccion.setIdDireccion(resultSetDirecciones.getInt("IdDireccion"));
                                direccion.setCalle(resultSetDirecciones.getString("Calle"));
                                direccion.Colonia = new Colonia();
                                direccion.Colonia.setNombre(resultSetDirecciones.getString("NombreColonia"));
                                /* .... */
                                alumnoDireccion.Direcciones.add(direccion);
                            }

                            result.object = alumnoDireccion;
                            result.correct = true;
                        }

                        return 1;
                    });

//            result.object = listaAlumnos;
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
            result.objects = null;
        }

        return result;
    }

    @Override
    public Result AlumnoById(int idAlumno) {
        Result result = new Result();
        try {
            jdbcTemplate.execute("{CALL AlumnoGetById(?,?)}",
                (CallableStatementCallback<Void>) callableStatement -> {
                    callableStatement.setInt(1, idAlumno);
                    callableStatement.registerOutParameter(1, Types.REF_CURSOR);
                    callableStatement.execute();
                    ResultSet resultSet = (ResultSet) callableStatement.getObject(1);

                    if (resultSet.next()) {

                        Alumno alumno = new Alumno();
                        alumno.setIdAlumno(idAlumno);
                        alumno.setNombre(resultSet.getString("NombreAlumno"));
                        alumno.setApellidoPaterno(resultSet.getString("ApellidoPaterno"));
                        alumno.setApellidoMaterno(resultSet.getString("ApellidoMaterno"));
                        alumno.Semestre = new Semestre();
                        alumno.Semestre.setIdSemestre(resultSet.getInt("IdSemestre"));
                        alumno.Semestre.setNombre(resultSet.getString("NombreSemestre"));
                        alumno.setTelefono(resultSet.getString("Telefono"));
                        alumno.setUserName(resultSet.getString("UserName"));
                        alumno.setPassword(resultSet.getString("Password"));

                        result.object = alumno;
                        result.correct = true;
                    }
                return null;
                });
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
            result.objects = null;
        }

        return result;
    }

    @Override
    public Result DireccionAlumnoByIdDireccion(int idDireccion) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Result GetAllJPA() {
        Result result = new Result();
        try {
            // JPQL
            
            result.objects = new ArrayList<>();
            
            TypedQuery<com.digis01.DGarciaProgramacionNCapasDiciembre24.JPA.Alumno> queryAlumno = entityManager.createQuery("FROM Alumno", com.digis01.DGarciaProgramacionNCapasDiciembre24.JPA.Alumno.class);
            List<com.digis01.DGarciaProgramacionNCapasDiciembre24.JPA.Alumno> alumnosJPA = queryAlumno.getResultList();
            
            for (com.digis01.DGarciaProgramacionNCapasDiciembre24.JPA.Alumno alumno : alumnosJPA) {
                AlumnoDireccion alumnoDireccion = new AlumnoDireccion();
                alumnoDireccion.Alumno = new Alumno();
                alumnoDireccion.Alumno.setIdAlumno(alumno.getIdAlumno());
                alumnoDireccion.Alumno.setNombre(alumno.getNombre());
                alumnoDireccion.Alumno.setApellidoPaterno(alumno.getApellidoPaterno());
                alumnoDireccion.Alumno.Semestre = new Semestre();
                alumnoDireccion.Alumno.Semestre.setIdSemestre(alumno.Semestre.getIdSemestre());
                alumnoDireccion.Alumno.Semestre.setNombre(alumno.Semestre.getNombre());
                
//                En caso de solo querer recuperar uno - singleResult
//                TypedQuery<com.digis01.DGarciaProgramacionNCapasDiciembre24.JPA.Direccion> queryDireccionErroneo = entityManager.createQuery("FROM Direccion WHERE Alumno.IdAlumno = :pIdAlumno", com.digis01.DGarciaProgramacionNCapasDiciembre24.JPA.Direccion.class);
//                queryDireccionErroneo.setParameter("pIdAlumno", alumno.getIdAlumno());
//                queryDireccionErroneo.getSingleResult();
                
                TypedQuery<com.digis01.DGarciaProgramacionNCapasDiciembre24.JPA.Direccion> queryDireccion = entityManager.createQuery("FROM Direccion WHERE Alumno.IdAlumno = :pIdAlumno", com.digis01.DGarciaProgramacionNCapasDiciembre24.JPA.Direccion.class);
                queryDireccion.setParameter("pIdAlumno", alumno.getIdAlumno());
                List<com.digis01.DGarciaProgramacionNCapasDiciembre24.JPA.Direccion> direcciones = queryDireccion.getResultList();
                
                alumnoDireccion.Direcciones = new ArrayList<>();
                for (com.digis01.DGarciaProgramacionNCapasDiciembre24.JPA.Direccion direccione : direcciones) {
                    Direccion direccion = new Direccion();
                    direccion.setCalle(direccione.getCalle());
                    
                    alumnoDireccion.Direcciones.add(direccion);
                }
                
                result.objects.add(alumnoDireccion);
                
            }
            
            result.correct = true;
            
        }catch(Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
            result.objects = null;
        }
        
        return result;
    }
    
    @Transactional // DML
    @Override
    public Result AddJPA(AlumnoDireccion alumnoDireccion) {
        
        com.digis01.DGarciaProgramacionNCapasDiciembre24.JPA.Alumno alumnoJPA = new com.digis01.DGarciaProgramacionNCapasDiciembre24.JPA.Alumno();
        alumnoJPA.setNombre("Sergio");
        alumnoJPA.setApellidoPaterno("Perez");
        alumnoJPA.setApellidoMaterno("Perez");
        alumnoJPA.setUserName("checogod");
        alumnoJPA.setPassword("checo123");
        alumnoJPA.setTelefono("1234560789");
        alumnoJPA.Semestre = new com.digis01.DGarciaProgramacionNCapasDiciembre24.JPA.Semestre();
        alumnoJPA.Semestre.setIdSemestre(1);
        
        
        entityManager.persist(alumnoJPA);
        
        return new Result();
    }

}
