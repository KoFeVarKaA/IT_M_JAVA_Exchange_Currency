package exchangecurrency.dao.jdbc;

import exchangecurrency.entity.Currency;
import exchangecurrency.dao.DaoCurrencies;
import exchangecurrency.exeptons.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
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


    private final DataSource database;
    public JdbcDaoCurrencies(DataSource database){
        this.database = database;
    }

    @Override
    public void createTable() {
        try (Connection conn = database.getConnection();
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
        /*try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CREATE_TABLE)) {

        } catch (SQLException exception) {

        } */
    }

    @Override
    public int post(Currency dto) {
        return 0;
    }

    @Override
    public Optional<Currency> getById(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<Currency> getByCode(String code) {
        return Optional.empty();
    }

    @Override
    public Optional<List<Currency>> getAll() {
        return Optional.empty();
    }

    @Override
    public OptionalInt get_id_by_code(String code) {
        return OptionalInt.empty();
    }

    @Override
    public void update(Currency dto) {

    }

    @Override
    public void delete(int id) {

    }
}
