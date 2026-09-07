package exchangecurrency.utils;

import exchangecurrency.dto.response.ResponseErrorDto;
import exchangecurrency.exeptons.BaseException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class ExceptionFilter extends HttpFilter {
    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        try {
          chain.doFilter(request, response);
        } catch (Throwable e) {
            int statusCode;
            String message = e.getMessage();

            if (e instanceof BaseException baseEx) {
                statusCode = baseEx.getErrorCode();
            } else {
                statusCode = 500;
            }
            ResponseMakerUtil.sendJson(
                    response, statusCode, new ResponseErrorDto(statusCode, e.getMessage()));

        }
    }
}
