package exchangecurrency.service.validator;

import exchangecurrency.exeptons.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class RateValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(RateValidator.class);

    public static void validateRate(String path, String rate){
        try {
            new BigDecimal(rate);
        } catch (Exception e) {
            throwInputError(path,
                    "Ошибка валидации курса. Проверьте корректность написания (Пример 11 или 0.1)");
        }
    }

    private static void throwInputError(String path, String message) {
        LOGGER.warn("doPATCH - /exchangeRate{}: {}", path, message);
        throw new ValidationException(message);
    }

}
