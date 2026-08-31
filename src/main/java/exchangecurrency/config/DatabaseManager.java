package exchangecurrency.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import exchangecurrency.dao.jdbc.JdbcDaoCurrencies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseManager.class);
    private static HikariDataSource dataSource;

    private static final String POOL_NAME = "exchange-currency-pool";
    private static final String CONFIG_FILE_NAME = "config.properties";

    static {
        init();
    }

    private static void init() {
        Properties properties = new Properties();

        try (InputStream inputStream = DatabaseManager.class
                .getClassLoader().getResourceAsStream(CONFIG_FILE_NAME)) {
            properties.load(inputStream);

            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl(properties.getProperty("hikari.jdbcUrl"));
            hikariConfig.setUsername(properties.getProperty("hikari.username"));
            hikariConfig.setPassword(properties.getProperty("hikari.password"));
            hikariConfig.setMaximumPoolSize(
                    Integer.parseInt(properties.getProperty("hikari.maximumPoolSize")));
            hikariConfig.setMinimumIdle(1);
            hikariConfig.setConnectionTimeout(
                    Integer.parseInt(properties.getProperty("hikari.connectionTimeout")));
            hikariConfig.setPoolName(POOL_NAME);
            dataSource = new HikariDataSource(hikariConfig);

            LOGGER.debug("Пул соединений с бд {} запущен", POOL_NAME);

        } catch (FileNotFoundException e) {
            LOGGER.error("Файл config.properties не найден");
            System.exit(1);
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage());
            System.exit(1);
        }
    }

    public static DataSource getDataSource() {
        return dataSource;
    }

    public static void close() {
        dataSource.close();
        LOGGER.debug("Пул соединений с бд {} успешно закрыт", POOL_NAME);
    }
}
