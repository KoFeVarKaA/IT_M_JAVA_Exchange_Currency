package exchangecurrency.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import exchangecurrency.exeptons.ValidationException;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public final class ResponseMakerUtil {
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseMakerUtil.class);

    private ResponseMakerUtil() {}

    public static void sendJson(HttpServletResponse response, int status, Object dto) {
        response.setContentType("application/json;charset=UTF-8");

        try {
            response.setStatus(status);
            OBJECT_MAPPER.writeValue(response.getWriter(), dto);
        } catch (JsonProcessingException e) {
            String message = "Ошибка преобразования dto в json - " + dto;
            LOGGER.error(message);
            throw new ValidationException(message + e.getMessage());
        } catch (IOException e) {
            String message = "Ошибка сети (IOExceprion)" + e.getMessage();
            LOGGER.warn(message);
            if (!response.isCommitted()) {
                try {
                    response.reset();
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().print("{\"error\":\"Internal Server Error\"," +
                            "\"message\":\"Не удалось сформировать JSON ответа\"}");
                } catch (IOException ioException) {
                    LOGGER.warn("Не удалось отправить клиенту сообщение об ошибке 500",
                            ioException);
                }
            }
        }
    }
}
