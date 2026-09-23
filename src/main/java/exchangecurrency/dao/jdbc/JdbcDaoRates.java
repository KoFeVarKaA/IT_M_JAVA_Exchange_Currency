package exchangecurrency.dao.jdbc;

import exchangecurrency.config.DatabaseManager;
import exchangecurrency.dao.DaoRates;
import exchangecurrency.dao.jdbc.mappers.CurrencyRowMapper;
import exchangecurrency.dao.jdbc.mappers.RateRowMapper;
import exchangecurrency.entity.Currency;
import exchangecurrency.entity.Rate;
import exchangecurrency.entity.RateCurrency;
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

    private static final String IS_EMPTY = "SELECT 1 FROM rates LIMIT 1";
    private static final String CREATE_TABLE = """
                CREATE TABLE IF NOT EXISTS rates(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                baseCurrencyId INTEGER,
                targetCurrencyId INTEGER,
                rate NUMERIC(12, 6)
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
    // Т.к. нет ORM пишем пришлось писать все вручную
    private static final String GET_ALL = """
    SELECT
        r.id AS rate_id,
        r.rate,
        bc.id AS base_currency_id,
        bc.code AS base_currency_code,
        bc.fullName AS base_currency_name,
        bc.sign AS base_currency_sing,
        tc.id AS target_currency_id,
        tc.code AS target_currency_code,
        tc.fullName AS target_currency_name,
        tc.sign AS target_currency_sing
    FROM rates r
    JOIN currencies bc ON r.baseCurrencyId = bc.id
    JOIN currencies tc ON r.targetCurrencyId = tc.id
    """;
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
    public boolean isEmpty() {
        try(Connection conn = DatabaseManager.getDataSource().getConnection();
            PreparedStatement statement = conn.prepareStatement(IS_EMPTY);
            ResultSet resultSet = statement.executeQuery();) {
            return !resultSet.next();
        } catch (SQLException exception) {
            LOGGER.error("Ошибка проверки существования записей в таблице rates");
            throw new DatabaseException("Ошибка получения списка валют");
        }
    }

    @Override
    public void createTable() {
        try (Connection conn = DatabaseManager.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(CREATE_TABLE)) {
            stmt.executeUpdate();
            LOGGER.debug("Таблица Rates успешно инициализирована");
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
            LOGGER.error("Ошибка удаления таблицы Rates: {}", exception.getMessage());
            throw new DatabaseException(
                    "Ошибка удаления таблицы Rates:" + exception.getMessage());
        }
    }

    @Override
    public void post(Rate dto) {
        try (Connection conn = DatabaseManager.getDataSource().getConnection();
             PreparedStatement statement = conn.prepareStatement(POST);){

            statement.setLong(1, dto.baseCurrencyId());
            statement.setLong(2, dto.targetCurrencyId());
            statement.setBigDecimal(3, dto.rate());
            statement.executeUpdate();
        } catch (SQLException exception) {
            LOGGER.error("""
                    Ошибка созранения курса обсмена валюты с baseCurrencyId = {} \
                    в targetCurrencyId = {}""", dto.baseCurrencyId(), dto.targetCurrencyId());
            throw new DatabaseException(
                    "Ошибка созранения курса обсмена валюты с baseCurrencyId = "
                    + dto.baseCurrencyId() + " в targetCurrencyId = " + dto.targetCurrencyId());
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
            LOGGER.error("Ошибка получения курса id = {}", id);
            throw new DatabaseException("Ошибка получения курса id = " + id);
        }
    }

    @Override
    public Optional<Rate> getByIds(String baseCurrencyId, String targetCurrencyId) {
        try (Connection conn = DatabaseManager.getDataSource().getConnection();
             PreparedStatement statement = conn.prepareStatement(GET_BY_IDS);){

            statement.setLong(1, Long.parseLong(baseCurrencyId));
            statement.setLong(2, Long.parseLong(targetCurrencyId));
            try (ResultSet resultSet = statement.executeQuery();) {
                if (resultSet.next()) {
                    return Optional.of(RateRowMapper.mapRow(resultSet)); }
            }
            return Optional.empty();
        } catch (SQLException exception) {
            String message = "Ошибка получения курса обсмена валюты с baseCurrencyId = "
               + baseCurrencyId + " в targetCurrencyId = " + targetCurrencyId;
            LOGGER.error("""
                    Ошибка получения курса обсмена валюты с baseCurrencyId = {} \
                    в targetCurrencyId = {} \n: {}""", baseCurrencyId, targetCurrencyId,
                    exception.getMessage());
            throw new DatabaseException(message);
        }
    }

    @Override
    public Optional<List<RateCurrency>> getAll() {
        try (Connection conn = DatabaseManager.getDataSource().getConnection();
             PreparedStatement statement = conn.prepareStatement(GET_ALL);
             ResultSet resultSet = statement.executeQuery();){

            List<RateCurrency> rates = new ArrayList<>();
            while (resultSet.next()) {
                Currency baseCurrency = new Currency(
                        resultSet.getLong("base_currency_id"),
                        resultSet.getString("base_currency_code"),
                        resultSet.getString("base_currency_name"),
                        resultSet.getString("base_currency_sing")
                );

                Currency targetCurrency = new Currency(
                        resultSet.getLong("target_currency_id"),
                        resultSet.getString("target_currency_code"),
                        resultSet.getString("target_currency_name"),
                        resultSet.getString("target_currency_sing")
                );

                rates.add(new RateCurrency(
                        resultSet.getLong("rate_id"),
                        baseCurrency,
                        targetCurrency,
                        resultSet.getBigDecimal("rate")
                ));
            }
            return Optional.of(rates);
        } catch (SQLException exception) {
            LOGGER.error("Ошибка получения списка курса валют {}", exception.getMessage());
            throw new DatabaseException("Ошибка получения списка курса валют");
        }
    }

    @Override
    public void update(Rate dto) {
        try (Connection conn = DatabaseManager.getDataSource().getConnection();
             PreparedStatement statement = conn.prepareStatement(UPDATE);){

            statement.setBigDecimal(1, dto.rate());
            statement.setLong(2, dto.id());
            int res = statement.executeUpdate();
        } catch (SQLException exception) {
            LOGGER.error("Ошибка обновления курса id = {}", dto.id());
            throw new DatabaseException("Ошибка обновления курса id = " + dto.id());
        }
    }

    @Override
    public void delete(int id) {
        try (Connection conn = DatabaseManager.getDataSource().getConnection();
             PreparedStatement statement = conn.prepareStatement(DELETE);){

            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException exception) {
            LOGGER.error("Ошибка удаления курса {}", id);
            throw new DatabaseException("Ошибка удаления курса " + id);
        }
    }
}
