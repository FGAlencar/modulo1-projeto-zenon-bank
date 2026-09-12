package br.com.zenon.transaction.factories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConnectionFactory {
    private ConnectionFactory(){}

    public static Connection getConnection(){
        try {
            return DriverManager.getConnection("jdbc:postgresql://localhost:5432/java_elite","admin","admin");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T executeWrapper(String sql, PreparedStatementConsumer<PreparedStatement, T> consumer){
        if(consumer == null ){
            throw new IllegalArgumentException("Consumer é obrigatório");
        }
        if(sql == null || sql.isEmpty()){
            throw new IllegalArgumentException("Sql não pode ser null ou vazio");
        }
        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            return consumer.apply(preparedStatement);
        }catch (SQLException e){
            throw  new RuntimeException(e);
        }
    }

    public interface PreparedStatementConsumer<I, O>{
        O apply(I i) throws SQLException;
    }
}
