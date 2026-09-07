package exchangecurrency.service.validator;

import exchangecurrency.dto.request.RequestPostCurrencyDto;
import exchangecurrency.exeptons.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CurrencyPostValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyPostValidator.class);

    private static final String CURRENCY_CODE_REGEX  = "^[A-Z]+$";
    private static final String CURRENCY_NAME_REGEX = "^[a-zA-Z]$";

    public static void validate(RequestPostCurrencyDto dto) {
        if (dto.sign().length() != 1) {
            throwInputError("Ошибка ввода. Знак валюты должен состоять из одного символа");}
        if (dto.code().length() != 1) {
            throwInputError("Ошибка ввода. Длина кода валюты должна составлять 3 символа");}
        if (dto.code().isEmpty() || !dto.code().matches(CURRENCY_CODE_REGEX)) {
            throwInputError("Ошибка ввода. Код может состоять только из английский заглавных букв");}
        if (dto.fullName().isEmpty() || !dto.fullName().matches(CURRENCY_NAME_REGEX)) {
            throwInputError("Ошибка ввода. Имя валюты может содержать только английские буквы");}
    }

    private static void throwInputError(String message) {
        String loggerMessage = "doPost - /currencies: ";
        LOGGER.warn(loggerMessage);
        throw new ValidationException(message);
    }

}
