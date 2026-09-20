package exchangecurrency.utils;

import exchangecurrency.dto.response.ResponseErrorDto;
import exchangecurrency.exeptons.BaseException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebFilter("/*")
public class ExceptionFilter extends HttpFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionFilter.class);

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        try {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
            chain.doFilter(request, response);
        } catch (Throwable e) {
            int statusCode;
            String message = e.getMessage();

            if (e instanceof BaseException baseEx) {
                statusCode = baseEx.getErrorCode();
            } else {
                LOGGER.error("Неопределенная ошибка: {}: {}", e.getClass(), e.getMessage());
                statusCode = 500;
            }
            ResponseMakerUtil.sendJson(
                    response, statusCode, new ResponseErrorDto(statusCode, e.getMessage()));

        }
    }
}
