package exchangecurrency.dao.jdbc;

import exchangecurrency.config.DatabaseManager;
import exchangecurrency.dao.DaoRates;
import exchangecurrency.dao.jdbc.mappers.CurrencyRowMapper;
import exchangecurrency.dao.jdbc.mappers.RateRowMapper;
import exchangecurrency.entity.Currency;
import exchangecurrency.entity.Rate;
import exchangecurrency.exeptons.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcDaoRates implements DaoRates {
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcDaoRates.class);

    private static final String CREATE_TABLE = """
                CREATE TABLE rates(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                baseCurrencyId INTEGER,
                targetCurrencyId INTEGER,
                rate Decimal(6),
                );
                """;
    private static final String DELETE_TABLE = "DROP TABLE rates;";
    private static final String POST = """
                INSERT INTO rates (baseCurrencyId, targetCurrencyId, rate)
                VALUES (?, ?, ?);
                """;
    private static final String GET_BY_ID = "SELECT * FROM rates WHERE id = ?";
    private static final String GET_BY_IDS = """
                SELECT * FROM rates
                WHERE baseCurrencyId = ? AND targetCurrencyId = ?
                """;
    private static final String GET_ALL = "SELECT * FROM rates";
    private static final String UPDATE = """
                UPDATE rates
                SET rate = ?
                WHERE id = ?;
                """;
    private static final String DELETE = """
                DELETE FROM rates
                WHERE id = ?;
                """;

    @Override
    public void createTable() {
        try (Connection conn = DatabaseManager.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(CREATE_TABLE)) {
            stmt.executeUpdate();
            LOGGER.debug("Таблица Rates успешно создана");
        } catch (SQLException exception) {
            String message = "Ошибка создания таблицы Rates";
            LOGGER.error(message);
            throw new DatabaseException(message);
        }
    }

    @Override
    public void deleteTable() {
        try (Connection conn = DatabaseManager.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_TABLE)) {
            stmt.executeUpdate();
            LOGGER.debug("Таблица Rates успешно удалена");
        } catch (SQLException exception) {
            String message = "Ошибка удаления таблицы Rates:" + exception.getMessage();
            LOGGER.error(message);
            throw new DatabaseException(message);
        }
    }

    @Override
    public void post(Rate dto) {
        try (Connection conn = DatabaseManager.getDataSource().getConnection();
             PreparedStatement statement = conn.prepareStatement(POST);){

            statement.setLong(1, dto.baseCurrencyId());
            statement.setLong(2, dto.targetCurrencyId());
            statement.setBigDecimal(3, dto.rate());
            statement.executeQuery();
        } catch (SQLException exception) {
            String message = "Ошибка созранения курса обсмена валюты с baseCurrencyId = "
               + dto.baseCurrencyId() + " в targetCurrencyId = " + dto.targetCurrencyId();
            LOGGER.error(message);
            throw new DatabaseException(message);
        }
    }

    @Override
    public Optional<Rate> getById(String id) {
        try (Connection conn = DatabaseManager.getDataSource().getConnection();
             PreparedStatement statement = conn.prepareStatement(GET_BY_ID);){
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery();) {
                if (resultSet.next()) {
                    return Optional.of(RateRowMapper.mapRow(resultSet)); }
            }
            return Optional.empty();
        } catch (SQLException exception) {
            String message = "Ошибка получения курса id = " + id;
            LOGGER.error(message);
            throw new DatabaseException(message);
        }
    }

    @Override
    public Optional<Rate> getByIds(String baseCurrencyId, String targetCurrencyId) {
        try (Connection conn = DatabaseManager.getDataSource().getConnection();
             PreparedStatement statement = conn.prepareStatement(GET_BY_ID);){

            statement.setString(1, baseCurrencyId);
            statement.setString(2, targetCurrencyId);
            try (ResultSet resultSet = statement.executeQuery();) {
                if (resultSet.next()) {
                    return Optional.of(RateRowMapper.mapRow(resultSet)); }
            }
            return Optional.empty();
        } catch (SQLException exception) {
            String message = "Ошибка получения курса обсмена валюты с baseCurrencyId = "
               + baseCurrencyId + " в targetCurrencyId = " + targetCurrencyId;
            LOGGER.error(message);
            throw new DatabaseException(message);
        }
    }

    @Override
    public Optional<List<Rate>> getAll() {
        try (Connection conn = DatabaseManager.getDataSource().getConnection();
             PreparedStatement statement = conn.prepareStatement(GET_ALL);
             ResultSet resultSet = statement.executeQuery();){

            List<Rate> rates = new ArrayList<>();
            while (resultSet.next()) {
                rates.add(RateRowMapper.mapRow(resultSet));
            }
            return Optional.of(rates);
        } catch (SQLException exception) {
            String message = "Ошибка получения списка курса валют";
            LOGGER.error(message);
            throw new DatabaseException(message);
        }
    }

    @Override
    public void update(Rate dto) {
        try (Connection conn = DatabaseManager.getDataSource().getConnection();
             PreparedStatement statement = conn.prepareStatement(UPDATE);){

            statement.setBigDecimal(1, dto.rate());
            statement.setLong(2, dto.id());
            statement.executeQuery();
        } catch (SQLException exception) {
            String message = "Ошибка обновления курса id = " + dto.id();
            LOGGER.error(message);
            throw new DatabaseException(message);
        }
    }

    @Override
    public void delete(int id) {
        try (Connection conn = DatabaseManager.getDataSource().getConnection();
             PreparedStatement statement = conn.prepareStatement(DELETE);){

            statement.setLong(1, id);
            statement.executeQuery();
        } catch (SQLException exception) {
            String message = "Ошибка удаления курса " + id;
            LOGGER.error(message);
            throw new DatabaseException(message);
        }
    }
}
