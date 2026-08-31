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
            LOGGER.debug("Таблица Currency успешно удалена");
        } catch (SQLException exception) {
            String message = "Ошибка удаления таблицы currency:" + exception.getMessage();
            LOGGER.error(message);
            throw new DatabaseException(message);
        }
    }

    @Override
    public void post(Currency dto) {
        try (Connection conn = DatabaseManager.getDataSource().getConnection();
             PreparedStatement statement = conn.prepareStatement(POST);){

            statement.setString(1, dto.getCode());
            statement.setString(2, dto.getFullName());
            statement.setString(3, dto.getSign());
            statement.executeQuery();
        } catch (SQLException exception) {
            String message = "Ошибка созранения валюты " + dto.getFullName();
            LOGGER.error(message);
            throw new DatabaseException(message);
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
            String message = "Ошибка получения пользователя id = " + id;
            LOGGER.error(message);
            throw new DatabaseException(message);
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
            String message = "Ошибка получения пользователя code = " + code;
            LOGGER.error(message);
            throw new DatabaseException(message);
        }
        return Optional.empty();
    }

    @Override
    public OptionalInt get_id_by_code(String code) {
        try (Connection conn = DatabaseManager.getDataSource().getConnection();
             PreparedStatement statement = conn.prepareStatement(GET_ID_BY_CODE);){
            statement.setString(1, code);
            try (ResultSet resultSet = statement.executeQuery();) {
                if (resultSet.next()) {
                    return OptionalInt.of(resultSet.getInt("id")); }
            }
        } catch (SQLException exception) {
            String message = "Ошибка получения id пользователя code = " + code;
            LOGGER.error(message);
            throw new DatabaseException(message);
        }
        return OptionalInt.empty();
    }

    @Override
    public Optional<List<Currency>> getAll() {
//        Нужен пагинатор
        return Optional.empty();
    }

    @Override
    public void update(Currency dto) {
        try (Connection conn = DatabaseManager.getDataSource().getConnection();
             PreparedStatement statement = conn.prepareStatement(UPDATE);){

            statement.setString(1, dto.getCode());
            statement.setString(2, dto.getFullName());
            statement.setString(3, dto.getSign());
            statement.setLong(4, dto.getId());
            statement.executeQuery();
        } catch (SQLException exception) {
            String message = "Ошибка обновления валюты " + dto.getFullName();
            LOGGER.error(message);
            throw new DatabaseException(message);
        }
    }

    @Override
    public void delete(long id) {
        try (Connection conn = DatabaseManager.getDataSource().getConnection();
             PreparedStatement statement = conn.prepareStatement(DELETE);){

            statement.setLong(1, id);
            statement.executeQuery();
        } catch (SQLException exception) {
            String message = "Ошибка удаления валюты " + id;
            LOGGER.error(message);
            throw new DatabaseException(message);
        }
    }
}
