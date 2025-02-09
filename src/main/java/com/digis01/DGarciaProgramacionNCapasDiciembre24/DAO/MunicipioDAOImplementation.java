package com.digis01.DGarciaProgramacionNCapasDiciembre24.DAO;

import com.digis01.DGarciaProgramacionNCapasDiciembre24.ML.Municipio;
import com.digis01.DGarciaProgramacionNCapasDiciembre24.ML.Result;
import com.digis01.DGarciaProgramacionNCapasDiciembre24.ML.Semestre;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MunicipioDAOImplementation implements IMunicipioDAO{
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Result MunicipioByEstado(int idEstado) {
        Result result = new Result();
        try {
            jdbcTemplate.execute("{CALL MunicipioByEstado(?,?)}",
                    (CallableStatementCallback<Integer>) callableStatement -> {
                        callableStatement.setInt(1, idEstado);
                        callableStatement.registerOutParameter(2, Types.REF_CURSOR);
                        callableStatement.execute();
                        ResultSet resultSet = (ResultSet) callableStatement.getObject(2);

                        result.objects = new ArrayList<>();
                        while (resultSet.next()) {

                            Municipio municipio = new Municipio();

                            municipio.setIdMunicipio(resultSet.getInt("IdMunicipio"));
                            municipio.setNombre(resultSet.getString("Nombre"));
                            
                            result.objects.add(municipio);
                            
                        }
                        result.correct = true;
                        return 1;
                    });

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
            result.objects = null;
        }

        return result;
    }
    
}
