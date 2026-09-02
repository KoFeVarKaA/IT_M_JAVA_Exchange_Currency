package exchangecurrency.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import exchangecurrency.Main;
import exchangecurrency.exeptons.ValidationException;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;

public final class JsonUtil {
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

    private JsonUtil() {}

    public static void sendJson(HttpServletResponse response, Object dto)
            throws IOException {
        response.setContentType("application/json;charset=UTF-8");

        try {
            OBJECT_MAPPER.writeValue(response.getWriter(), dto);
        } catch (JsonProcessingException e) {
            String message = "Ошибка преобразования json в dto" + dto;
            LOGGER.error(message);
            throw new ValidationException(message + e.getMessage());
        }
    }
}
