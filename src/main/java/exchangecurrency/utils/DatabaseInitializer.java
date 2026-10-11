package exchangecurrency.utils;

import exchangecurrency.dao.jdbc.JdbcDaoCurrencies;
import exchangecurrency.dao.jdbc.JdbcDaoRates;
import exchangecurrency.entity.Currency;
import exchangecurrency.entity.Rate;
import exchangecurrency.exeptons.DatabaseException;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;

@WebListener
public class DatabaseInitializer implements ServletContextListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseInitializer.class);
    private static final JdbcDaoCurrencies daoCurrencies = new JdbcDaoCurrencies();
    private static final JdbcDaoRates daoRates = new JdbcDaoRates();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        LOGGER.info("Приложение разворачивается в Smart Tomcat. Инициализация...");

        initDataBase();
    }

    private static void initDataBase(){
        try {
//            daoCurrencies.deleteTable();
//            daoRates.deleteTable();
            daoCurrencies.createTable();
            daoRates.createTable();

            if (daoCurrencies.isEmpty()) {
                daoCurrencies.post(new Currency(
                        0, "USD", "United States dollar", "$"
                ));
                daoCurrencies.post(new Currency(
                        1, "EUR", "Euro", "€"
                ));
                daoCurrencies.post(new Currency(
                        2, "RUB", "Russian Ruble", "₽"
                ));
            }
            if (daoRates.isEmpty()) {
                daoRates.post(new Rate(
                        0,
                        daoCurrencies.getIdByCode("USD").getAsInt(),
                        daoCurrencies.getIdByCode("RUB").getAsInt(),
                        new BigDecimal("1.00")
                ));
            }
        } catch (DatabaseException e) {
            LOGGER.error("Не удалось инициализировать базу данных", e);
            System.exit(500);
        }

    }
}
