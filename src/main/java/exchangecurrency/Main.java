package exchangecurrency;

import exchangecurrency.controller.HelloWorldServler;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.io.File;

public class Main{
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

        HelloWorldServler helloServlet = new HelloWorldServler();
        Tomcat.addServlet(ctx, "helloWorldServlet", helloServlet);

        ctx.addServletMapping("/", "helloWorldServlet");

        LOGGER.info("Tomcat 11 запускается на порту 8080...");
        try {
            tomcat.start();
            tomcat.getServer().await();
        } catch (Exception e) {
            String message = "Ошибка запуска сервера" + e.getMessage();
            LOGGER.error(message);
        }
    }
}
