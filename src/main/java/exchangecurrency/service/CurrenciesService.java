package exchangecurrency.service;

import exchangecurrency.dao.jdbc.JdbcDaoCurrencies;
import exchangecurrency.dto.request.RequestPostCurrencyDto;
import exchangecurrency.dto.response.ResponseCurrencyDto;
import exchangecurrency.entity.Currency;
import exchangecurrency.exeptons.ObjectAlreadyExistsExceprion;
import exchangecurrency.mappers.CurrencyMapper;
import exchangecurrency.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class CurrenciesService {
    private final JdbcDaoCurrencies dao;
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

    public CurrenciesService (JdbcDaoCurrencies dao) {this.dao = dao;}

    public ResponseCurrencyDto postCurrencies (RequestPostCurrencyDto dto) {
        Optional<Currency> currencyOptional = dao.getByCode(dto.code());
        if (currencyOptional.isPresent()) {
            String message = "Ошибка - валюта " + dto.fullName() + " уже существует";
            LOGGER.warn(message);
            throw new ObjectAlreadyExistsExceprion(message);
        }
        Currency currency = CurrencyMapper.INSTANCE.toEntity(dto);
        dao.post(currency);
    }
}
