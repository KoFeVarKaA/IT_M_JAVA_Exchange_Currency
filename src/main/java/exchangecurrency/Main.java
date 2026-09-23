import exchangecurrency.controller.*;
import exchangecurrency.dao.jdbc.JdbcDaoCurrencies;
import exchangecurrency.dao.jdbc.JdbcDaoRates;
import exchangecurrency.entity.Currency;
import exchangecurrency.entity.Rate;
import exchangecurrency.exeptons.DatabaseException;
import exchangecurrency.service.CurrenciesService;
import exchangecurrency.service.RatesService;
import exchangecurrency.utils.ExceptionFilter;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServlet;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import ch.qos.logback.classic.Level;

import java.io.File;

public static class Main{
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static final JdbcDaoCurrencies daoCurrencies = new JdbcDaoCurrencies();
    private static final JdbcDaoRates daoRates = new JdbcDaoRates();

    static void main() {
//        Разобраться кто отвечает за отдачу всех курсов обмена
//        GET http://localhost:8080//exchangeRates 405 (Method Not Allowed)
        LOGGER.info("Запуск приложения...");
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        initDataBase();

        Tomcat tomcat = new Tomcat();
        tomcat.getConnector().setParseBodyMethods("POST,PUT,PATCH");
        tomcat.setPort(8080);
        tomcat.getConnector();

        String docBase = new File(".").getAbsolutePath();
        Context ctx = tomcat.addContext("", docBase);

        CurrenciesService currenciesService = new CurrenciesService(new JdbcDaoCurrencies());
        RatesService ratesService = new RatesService(new JdbcDaoCurrencies(), new JdbcDaoRates());

        // Регистрация сервлетов и пр.
        registerServlet(ctx, new CurrenciesServlet(currenciesService),
                "CurrenciesServlet", "/currencies");
        registerServlet(ctx, new CurrencyServlet(currenciesService),
                "CurrencyServlet", "/currency/*");
        registerServlet(ctx, new ExchangeServlet(ratesService),
                "ExchangeServlet", "/exchange");
        registerServlet(ctx, new RateServlet(ratesService),
                "RateServlet", "/exchangeRate/*");
        registerServlet(ctx, new RatesServlet(ratesService),
                "RatesServlet", "/exchangeRates");
        registerFilter(ctx, new ExceptionFilter(), "ExceptionFilter", "/*");

        LOGGER.info("Tomcat 11 запускается на порту 8080...");
        try {
            tomcat.start();
            tomcat.getServer().await();
        } catch (Exception e) {
            LOGGER.error("Ошибка запуска сервера {}", e.getMessage());
        }
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

    private static void registerServlet(Context ctx, HttpServlet servlet, String name,
                                        String urlPattern) {
        Tomcat.addServlet(ctx, name, servlet);
        ctx.addServletMapping(urlPattern, name);
    }
}

    private static void registerFilter(Context ctx, Filter filter, String name,
                                       String urlPattern) {
        org.apache.tomcat.util.descriptor.web.FilterDef def = new org.apache.tomcat.util.descriptor.web.FilterDef();
        def.setFilterName(name);
        def.setFilter(filter);
        ctx.addFilterDef(def);

        org.apache.tomcat.util.descriptor.web.FilterMap map = new org.apache.tomcat.util.descriptor.web.FilterMap();
        map.setFilterName(name);
        map.addURLPattern(urlPattern);
        ctx.addFilterMap(map);
    }

void main() {
    Main.main();
}
