package exchangecurrency.controller;

import exchangecurrency.dto.request.RequestGetRateDto;
import exchangecurrency.dto.response.ResponseExchangeDto;
import exchangecurrency.dto.response.ResponseRateDto;
import exchangecurrency.mappers.ResponseExchangeDtoMapper;
import exchangecurrency.service.RatesService;
import exchangecurrency.utils.ResponseMakerUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

@WebServlet(urlPatterns = "/exchange/*",
        description = "Получение конкретной валюты по коду")
public class ExchangeServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeServlet.class);
    private final RatesService service;

    public ExchangeServlet (RatesService service) {this.service = service;}

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");

        RequestGetRateDto requestDto = new RequestGetRateDto(
                request.getParameter("from"),
                request.getParameter("to")
        );
        ResponseRateDto RateDto = service.getRate(requestDto);

        BigDecimal amount = BigDecimal.valueOf(Long.parseLong(
                request.getParameter("amount")));
        BigDecimal convertedAmount = amount.multiply(RateDto.rate());

        ResponseExchangeDto responseDto = ResponseExchangeDtoMapper.INSTANCE.toDto(
            RateDto, amount, convertedAmount
        );
        ResponseMakerUtil.sendJson(response, HttpServletResponse.SC_OK, responseDto);
    }
}
