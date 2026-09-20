package exchangecurrency.dao.jdbc;

import exchangecurrency.config.DatabaseManager;
import exchangecurrency.dao.jdbc.mappers.CurrencyRowMapper;
import exchangecurrency.entity.Currency;
import exchangecurrency.dao.DaoCurrencies;
import exchangecurrency.exeptons.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public class JdbcDaoCurrencies implements DaoCurrencies {
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcDaoCurrencies.class);

    private static final String CREATE_TABLE = """
            CREATE TABLE currencies(
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  code VARCHAR(30),
                  fullName VARCHAR(40),
                  sign VARCHAR(5)
                );
            """;
    private static final String DROP_TABLE = "DROP TABLE currencies;";
    private static final String POST = """
            INSERT INTO currencies (code, fullname, sign) 
            VALUES (?, ?, ?);
            """;
    private static final String GET_BY_ID = "SELECT * FROM currencies WHERE id = ?";
    private static final String GET_BY_CODE = "SELECT * FROM currencies WHERE code = ?";
    private static final String GET_ID_BY_CODE = "SELECT id FROM currencies WHERE code = ?";
    private static final String GET_ALL = "SELECT * FROM currencies";
    private static final String UPDATE = """
            UPDATE currencies
            SET code = ?,
                fullname = ?,
                sign = ?
            WHERE id = ?;
            """;
    private static final String DELETE = """
            DELETE FROM currencies
            WHERE id = ?;
            """;

    public JdbcDaoCurrencies(){}

    @Override
    public void createTable() {
        try (Connection conn = DatabaseManager.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(CREATE_TABLE)) {
            stmt.executeUpdate();
            LOGGER.debug("Таблица Currency успешно создана");
        } catch (SQLException exception) {
            String message = "Ошибка создания таблицы Currency";
            LOGGER.error(message);
            throw new DatabaseException(message);
        }
    }

    @Override
    public void deleteTable() {
        try (Connection conn = DatabaseManager.getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(CREATE_TABLE)) {
            stmt.executeUpdate();
            LOGGER.debug("Таблица Currencies успешно удалена");
        } catch (SQLException exception) {
            LOGGER.error("Ошибка удаления таблицы currency: {}", exception.getMessage());
            throw new DatabaseException(
                    "Ошибка удаления таблицы currency:" + exception.getMessage());
        }
    }

    @Override
    public void post(Currency dto) {
        try (Connection conn = DatabaseManager.getDataSource().getConnection();
             PreparedStatement statement = conn.prepareStatement(POST);){

            statement.setString(1, dto.code());
            statement.setString(2, dto.fullName());
            statement.setString(3, dto.sign());
            statement.executeUpdate();
        } catch (SQLException exception) {
            LOGGER.error("Ошибка созранения валюты {}: {}", dto.fullName(),
                    exception.getMessage());
            throw new DatabaseException("Ошибка сохранения валюты " + dto.fullName());
        }
    }

    @Override
    public Optional<Currency> getById(String id) {
        try (Connection conn = DatabaseManager.getDataSource().getConnection();
             PreparedStatement statement = conn.prepareStatement(GET_BY_ID);){
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery();) {
                if (resultSet.next()) {
                    return Optional.of(CurrencyRowMapper.mapRow(resultSet)); }
            }
        } catch (SQLException exception) {
            LOGGER.error("Ошибка получения валюты id = {}", id);
            throw new DatabaseException("Ошибка получения валюты id = " + id);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Currency> getByCode(String code) {
        try (Connection conn = DatabaseManager.getDataSource().getConnection();
             PreparedStatement statement = conn.prepareStatement(GET_BY_CODE);){
            statement.setString(1, code);
            try (ResultSet resultSet = statement.executeQuery();) {
                if (resultSet.next()) {
                    return Optional.of(CurrencyRowMapper.mapRow(resultSet)); }
            }
        } catch (SQLException exception) {
            LOGGER.error("Ошибка получения валюты code = {}", code);
            throw new DatabaseException("Ошибка получения валюты code = " + code);
        }
        return Optional.empty();
    }

    @Override
    public OptionalInt getIdByCode(String code) {
        try (Connection conn = DatabaseManager.getDataSource().getConnection();
             PreparedStatement statement = conn.prepareStatement(GET_ID_BY_CODE);){
            statement.setString(1, code);
            try (ResultSet resultSet = statement.executeQuery();) {
                if (resultSet.next()) {
                    return OptionalInt.of(resultSet.getInt("id")); }
            }
        } catch (SQLException exception) {
            LOGGER.error("Ошибка получения id валюты code = {}", code);
            throw new DatabaseException("Ошибка получения id валюты code = " + code);
        }
        return OptionalInt.empty();
    }

    @Override
    public Optional<List<Currency>> getAll() {
        try (Connection conn = DatabaseManager.getDataSource().getConnection();
             PreparedStatement statement = conn.prepareStatement(GET_ALL);
             ResultSet resultSet = statement.executeQuery();){

            List<Currency> currencies = new ArrayList<>();
            while (resultSet.next()) {
                currencies.add(CurrencyRowMapper.mapRow(resultSet));
            }
            return Optional.of(currencies);
        } catch (SQLException exception) {
            LOGGER.error("Ошибка получения списка валют");
            throw new DatabaseException("Ошибка получения списка валют");
        }
    }

    @Override
    public void update(Currency dto) {
        try (Connection conn = DatabaseManager.getDataSource().getConnection();
             PreparedStatement statement = conn.prepareStatement(UPDATE);){

            statement.setString(1, dto.code());
            statement.setString(2, dto.fullName());
            statement.setString(3, dto.sign());
            statement.setLong(4, dto.id());
            statement.executeUpdate();
        } catch (SQLException exception) {
            LOGGER.error("Ошибка обновления валюты {}", dto.fullName());
            throw new DatabaseException("Ошибка обновления валюты " + dto.fullName());
        }
    }

    @Override
    public void delete(long id) {
        try (Connection conn = DatabaseManager.getDataSource().getConnection();
             PreparedStatement statement = conn.prepareStatement(DELETE);){

            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException exception) {
            LOGGER.error("Ошибка удаления валюты {}", id);
            throw new DatabaseException("Ошибка удаления валюты " + id);
        }
    }
}
