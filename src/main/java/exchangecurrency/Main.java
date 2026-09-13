import exchangecurrency.controller.HelloWorldServlet;
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

import java.io.File;

public static class Main{
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    static void main() {
        LOGGER.info("Запуск приложения...");
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.getConnector();

        String docBase = new File(".").getAbsolutePath();
        Context ctx = tomcat.addContext("", docBase);

        // Регистрация сервлетов и пр.
        registerServlet(ctx, new HelloWorldServlet(), "helloWorldServlet", "/");
        registerFilter(ctx, new ExceptionFilter(), "ExceptionFilter", "/*");

        LOGGER.info("Tomcat 11 запускается на порту 8080...");
        try {
            tomcat.start();
            tomcat.getServer().await();
        } catch (Exception e) {
            LOGGER.error("Ошибка запуска сервера {}", e.getMessage());
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
