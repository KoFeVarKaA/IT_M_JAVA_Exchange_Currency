package exchangecurrency.controller;

import exchangecurrency.dto.request.RequestGetRateDto;
import exchangecurrency.dto.request.RequestPatchRateDto;
import exchangecurrency.dto.response.ResponseRateDto;
import exchangecurrency.exeptons.ValidationException;
import exchangecurrency.service.RatesService;
import exchangecurrency.utils.ResponseMakerUtil;
import jakarta.annotation.Nonnull;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

@WebServlet(urlPatterns = "/exchangeRate/*",
        description = "Получение/обновление конкретного обменного курса")
public class RateServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(RateServlet.class);
    private final RatesService service;

    public RateServlet (RatesService service) {this.service = service;}

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        String messageRequest = "doGET - /exchangeRate";
        String pathInfo = request.getPathInfo();
        String codesStr = getValidCodesStr(messageRequest, pathInfo);
        RequestGetRateDto requestDto = new RequestGetRateDto(
                codesStr.substring(0, 3),
                codesStr.substring(3, 6)
        );
        ResponseRateDto responseDto = service.getRate(requestDto);
        ResponseMakerUtil.sendJson(response, HttpServletResponse.SC_OK, responseDto);
    }

    @Override
    protected void doPatch(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        String messageRequest = "doGET - /exchangeRate";
        String pathInfo = request.getPathInfo();
        String codesStr = getValidCodesStr(messageRequest, pathInfo);
        RequestPatchRateDto requestDto = new RequestPatchRateDto(
                codesStr.substring(0, 3),
                codesStr.substring(3, 6),
                BigDecimal.valueOf(Long.parseLong(
                        request.getParameter("rate")))
                );
        ResponseRateDto responseDto = service.postUpdateRate(requestDto, false);
        ResponseMakerUtil.sendJson(response, HttpServletResponse.SC_OK, responseDto);
    }

    private String getValidCodesStr(String messageRequest, String pathInfo) {
        LOGGER.info("{}/{}", messageRequest, pathInfo);
        if (pathInfo == null || pathInfo.equals("/")) {
            String message = "Коды валют не указаны в URL";
            LOGGER.warn("{}/{}: {}", messageRequest, pathInfo, message);
            throw new ValidationException(message);
        }
        String codesStr = pathInfo.replace("/", "").trim().toUpperCase();
        if (codesStr.length() != 6) {
            String message = "Не верный формат ввода. Должно быть " +
                    "ровно 6 английских заглавных букв (например, USDRUB)";
            LOGGER.warn("{}/{}: {}", messageRequest, pathInfo, message);
            throw new ValidationException(message);
        }
        return codesStr;
    }
}
