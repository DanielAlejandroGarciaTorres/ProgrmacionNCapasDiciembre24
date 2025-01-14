package com.digis01.DGarciaProgramacionNCapasDiciembre24.DAO;

import com.digis01.DGarciaProgramacionNCapasDiciembre24.ML.Result;
import com.digis01.DGarciaProgramacionNCapasDiciembre24.ML.Semestre;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository 
public class SemestreDAOImplementation implements ISemestreDAO{

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Override
    public Result GetAll() {
        Result result = new Result();
        try {
            jdbcTemplate.execute("{CALL SemestreGetAll(?)}",
                    (CallableStatementCallback<Integer>) callableStatement -> {
                        callableStatement.registerOutParameter(1, Types.REF_CURSOR);
                        callableStatement.execute();
                        ResultSet resultSet = (ResultSet) callableStatement.getObject(1);

                        result.objects = new ArrayList<>();
                        while (resultSet.next()) {

                            Semestre semestre = new Semestre();

                            semestre.setIdSemestre(resultSet.getInt("IdSemestre"));
                            semestre.setNombre(resultSet.getString("Nombre"));
                            result.objects.add(semestre);
                            
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
    
}
