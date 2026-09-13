package exchangecurrency.service.validator;

import exchangecurrency.dto.request.RequestPostCurrencyDto;
import exchangecurrency.exeptons.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CurrencyValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyValidator.class);

    private static final String CURRENCY_CODE_REGEX  = "^[A-Z]+$";
    private static final String CURRENCY_NAME_REGEX = "^[a-zA-Z]$";

    public static void validate(RequestPostCurrencyDto dto) {
        if (dto.sign().length() != 1) {
            throwInputError("Ошибка ввода. Знак валюты должен состоять из одного символа");}
        validateCode(dto.code());
        if (dto.fullName().isEmpty() || !dto.fullName().matches(CURRENCY_NAME_REGEX)) {
            throwInputError("Ошибка ввода. Имя валюты может содержать только английские буквы");}
    }

    public static void validateCode(String code){
        if (code.length() != 3) {
            throwInputError("Ошибка ввода. Длина кода валюты должна составлять 3 символа");}
        if (code.isEmpty() || !code.matches(CURRENCY_CODE_REGEX)) {
            throwInputError("Ошибка ввода. Код может состоять только из английский заглавных букв");}
    }

    private static void throwInputError(String message) {
        LOGGER.warn("doPost - /currencies: {}", message);
        throw new ValidationException(message);
    }

}
