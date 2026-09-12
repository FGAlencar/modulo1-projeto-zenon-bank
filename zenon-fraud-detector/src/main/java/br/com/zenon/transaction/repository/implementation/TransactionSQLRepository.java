package br.com.zenon.transaction.repository.implementation;

import br.com.zenon.transaction.domain.Transaction;
import br.com.zenon.transaction.domain.TransactionAccount;
import br.com.zenon.transaction.domain.TransactionType;
import br.com.zenon.transaction.factories.ConnectionFactory;
import br.com.zenon.transaction.repository.TransactionRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class TransactionSQLRepository implements TransactionRepository {

    @Override
    public Optional<Transaction> findByOriginName(String name) {
        return ConnectionFactory.executeWrapper(
                "SELECT * FROM transactions WHERE name_orig = ?",
            preparedStatement -> {
                preparedStatement.setString(1, name);
                ResultSet resultSet = preparedStatement.executeQuery();
                return resultSet.next() ? Optional.of(this.mapResultSetIntoTransaction(resultSet)): Optional.empty();
            }
        );
    }

    @Override
    public void save(Transaction transaction) {
       ConnectionFactory.executeWrapper("""
            INSERT INTO transactions ( step, type, amount, name_orig, old_balance_orig, new_balance_orig, name_dest, old_balance_dest, new_balance_dest, is_fraud, is_flagged_fraud)
            VALUES ( ?, ?, ?, ?,  ?, ?, ?, ?, ?, ?, ?)
        """, preparedStatement -> {
               preparedStatement.setInt(1, transaction.step());
               preparedStatement.setString(2, transaction.type().name());
               preparedStatement.setBigDecimal(3, transaction.amount());
               preparedStatement.setString(4, transaction.origin().name());
               preparedStatement.setBigDecimal(5, transaction.origin().oldBalance());
               preparedStatement.setBigDecimal(6, transaction.origin().newBalance());
               preparedStatement.setString(7, transaction.recipient().name());
               preparedStatement.setBigDecimal(8, transaction.recipient().oldBalance());
               preparedStatement.setBigDecimal(9, transaction.recipient().newBalance());
               preparedStatement.setBoolean(10, transaction.isFraud());
               preparedStatement.setBoolean(11, transaction.isFlaggedFraud());
               preparedStatement.execute();
               return null;
       });

    }

    private Transaction mapResultSetIntoTransaction(ResultSet resultSet) throws SQLException {
        TransactionAccount origin = new TransactionAccount(
                resultSet.getString("name_orig"),
                resultSet.getBigDecimal("old_balance_orig"),
                resultSet.getBigDecimal("new_balance_orig")
        );

        TransactionAccount recipient = new TransactionAccount(
                resultSet.getString("name_dest"),
                resultSet.getBigDecimal("old_balance_dest"),
                resultSet.getBigDecimal("new_balance_dest")
        );

        return new Transaction(
                resultSet.getInt("step"),
                TransactionType.valueOf(resultSet.getString("type")),
                resultSet.getBigDecimal("amount"),
                origin,
                recipient,
                resultSet.getBoolean("is_fraud"),
                resultSet.getBoolean("is_flagged_fraud")
        );
    }



}
