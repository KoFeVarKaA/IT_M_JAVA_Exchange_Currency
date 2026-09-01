package exchangecurrency.dao.jdbc.mappers;

import exchangecurrency.entity.Currency;
import exchangecurrency.entity.Rate;
import exchangecurrency.exeptons.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RateRowMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(RateRowMapper.class);

    public static Rate mapRow(ResultSet resultSet){
        try {
            return new Rate(
                    resultSet.getLong("id"),
                    resultSet.getLong("baseCurrencyId"),
                    resultSet.getLong("targetCurrencyId"),
                    resultSet.getBigDecimal("rate")
            );
        } catch (SQLException e) {
            String message = "Ошибка обработки данных из бд" + e.getMessage();
            LOGGER.error(message);
            throw new DatabaseException(message);
        }
    }
}

