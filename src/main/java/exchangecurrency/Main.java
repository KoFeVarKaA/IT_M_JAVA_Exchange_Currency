package exchangecurrency;

import exchangecurrency.dao.jdbc.JdbcDaoCurrencies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main{
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static final JdbcDaoCurrencies dao = new JdbcDaoCurrencies();

    public static void main() {
        LOGGER.info("Запуск приложения...");
//        dao.createTable();
        System.out.println(dao.getById("0"));
    }
}