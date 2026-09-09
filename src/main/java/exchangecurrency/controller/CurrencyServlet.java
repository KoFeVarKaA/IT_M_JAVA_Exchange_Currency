package exchangecurrency.controller;

import exchangecurrency.dto.response.ResponseCurrencyDto;
import exchangecurrency.exeptons.ObjectNotFoundException;
import exchangecurrency.exeptons.ValidationException;
import exchangecurrency.service.CurrenciesService;
import exchangecurrency.service.validator.CurrencyValidator;
import exchangecurrency.utils.ResponseMakerUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = "/currency/*",
        description = "Получение конкретной валюты по коду")
public class CurrencyServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyServlet.class);
    private final CurrenciesService service;

    public CurrencyServlet (CurrenciesService service) {this.service = service;}

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String messageRequest = "doGET - /currency";
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            String message = "Ошибка - в адресе запроса отсутвует код валюты";
            LOGGER.warn("{}: {}", messageRequest, message);
            throw new ValidationException(message);
        }
        String code =  pathInfo.replace("/", "").trim();
        CurrencyValidator.validateCode(code);

        LOGGER.info("{}{}", messageRequest, pathInfo);
        Optional<ResponseCurrencyDto> currencyDto = service.getCurrency(code);
        if (currencyDto.isPresent()){
            ResponseMakerUtil.sendJson(response, HttpServletResponse.SC_OK, currencyDto);
            return;
        }
        String messageNotFound = "Ошибка объект currency code='%s'не найден".formatted(code);
        LOGGER.warn("{}: {}", messageRequest, messageNotFound);
        throw new ObjectNotFoundException(messageNotFound);
    }
}
