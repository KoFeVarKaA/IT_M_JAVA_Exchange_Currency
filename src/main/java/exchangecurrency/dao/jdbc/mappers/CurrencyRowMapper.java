package exchangecurrency.dao.jdbc.mappers;

import exchangecurrency.dao.jdbc.JdbcDaoCurrencies;
import exchangecurrency.entity.Currency;
import exchangecurrency.exeptons.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CurrencyRowMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyRowMapper.class);

    public static Currency mapRow(ResultSet resultSet){
        try {
        return new Currency(
            resultSet.getLong("id"),
            resultSet.getString("code"),
            resultSet.getString("fullName"),
            resultSet.getString("sign")
        );
        } catch (SQLException e) {
            String message = "Ошибка обработки данных из бд" + e.getMessage();
            LOGGER.error(message);
            throw new DatabaseException(message);
        }
    }
}
