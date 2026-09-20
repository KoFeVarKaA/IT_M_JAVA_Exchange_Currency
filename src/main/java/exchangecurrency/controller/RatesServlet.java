package exchangecurrency.controller;

import exchangecurrency.dto.request.RequestPatchRateDto;
import exchangecurrency.dto.response.ResponseRateDto;
import exchangecurrency.entity.Currency;
import exchangecurrency.service.RatesService;
import exchangecurrency.service.validator.CurrencyValidator;
import exchangecurrency.utils.ResponseMakerUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

@WebServlet(urlPatterns = "/exchangeRates",
        description = "Добавление нового обменного курса")
public class RatesServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(RatesServlet.class);
    private final RatesService service;

    public RatesServlet (RatesService service) {this.service = service;}

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        String baseCurrencyCode = request.getParameter("baseCurrencyCode");
        String targetCurrencyCode = request.getParameter("targetCurrencyCode");

        LOGGER.info("doPOST - /exchangeRate; baseCurrencyCode={},targetCurrencyCode{},rate={}",
                baseCurrencyCode, targetCurrencyCode,
                request.getParameter("rate"));
        CurrencyValidator.validateCode(baseCurrencyCode);
        CurrencyValidator.validateCode(targetCurrencyCode);
        RequestPatchRateDto requestDto = new RequestPatchRateDto(
                baseCurrencyCode, targetCurrencyCode,
                new BigDecimal(request.getParameter("rate"))
        );
        ResponseRateDto responseDto = service.postUpdateRate(requestDto, true);
        LOGGER.debug("doPOST - /exchangeRate - ответ: {}", responseDto);
        ResponseMakerUtil.sendJson(response, HttpServletResponse.SC_OK, responseDto);
    }
}
