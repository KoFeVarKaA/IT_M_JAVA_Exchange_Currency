package exchangecurrency.controller;

import exchangecurrency.dto.request.RequestPostCurrencyDto;
import exchangecurrency.dto.response.ResponseCurrencyDto;
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

@WebServlet(urlPatterns = "/currencies",
        description = "Получение списка валют; Добавление новой валюты в базу")
public class CurrenciesServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(CurrenciesServlet.class);
    private final CurrenciesService service;

    public CurrenciesServlet (CurrenciesService service) {this.service = service;}

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        LOGGER.info("doGET - /currencies");
        List<ResponseCurrencyDto> currencyDtos = service.getCurrencies();
        LOGGER.debug("doGET - /currencies - ответ: {}", currencyDtos.toString());
        ResponseMakerUtil.sendJson(response, HttpServletResponse.SC_OK, currencyDtos);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ValidationException {
        request.setCharacterEncoding("UTF-8");

        RequestPostCurrencyDto requestDto = new RequestPostCurrencyDto(
                request.getParameter("code"),
                request.getParameter("name"),
                request.getParameter("sign")
        );
        LOGGER.info("doPost - /currencies data = {}", requestDto);
        CurrencyValidator.validate(requestDto);
        ResponseCurrencyDto responseDto = service.postCurrency(requestDto);
        LOGGER.info("doPost - /currencies - ответ: {}", responseDto.toString());
        ResponseMakerUtil.sendJson(response, HttpServletResponse.SC_OK, responseDto);
    }
}
