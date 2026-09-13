package exchangecurrency.service;

import exchangecurrency.dao.jdbc.JdbcDaoCurrencies;
import exchangecurrency.dto.request.RequestPostCurrencyDto;
import exchangecurrency.dto.response.ResponseCurrencyDto;
import exchangecurrency.entity.Currency;
import exchangecurrency.exeptons.DatabaseException;
import exchangecurrency.exeptons.ObjectAlreadyExistsException;
import exchangecurrency.mappers.CurrencyMapper;
import exchangecurrency.mappers.ResponseCurrencyDtoMapper;
import exchangecurrency.utils.ResponseMakerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class CurrenciesService {
    private final JdbcDaoCurrencies dao;
    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseMakerUtil.class);

    public CurrenciesService (JdbcDaoCurrencies dao) {this.dao = dao;}

    public ResponseCurrencyDto postCurrency (RequestPostCurrencyDto dto) {
        Optional<Currency> currencyOptional = dao.getByCode(dto.code());
        if (currencyOptional.isPresent()) {
            LOGGER.warn("Ошибка - валюта {} уже существует", dto.fullName());
            throw new ObjectAlreadyExistsException("Ошибка - валюта " + dto.fullName() + " уже существует");
        }
        dao.post(CurrencyMapper.INSTANCE.toEntity(dto));

        Optional<Currency> savedCurrencyOptional = dao.getByCode(dto.code());
        if (savedCurrencyOptional.isEmpty()) {
            LOGGER.error("Ошибка создания или получения Id валюты {}", dto.fullName());
            throw new DatabaseException("Ошибка создания или получения Id валюты" + dto.fullName());
        }
        return ResponseCurrencyDtoMapper.INSTANCE.toDto(savedCurrencyOptional.get());
    }

    public Optional<ResponseCurrencyDto> getCurrency(String code) {
        return dao.getByCode(code).map(ResponseCurrencyDtoMapper.INSTANCE::toDto);
    }

    public List<ResponseCurrencyDto> getCurrencies() {
        return dao.getAll()
                .map(ResponseCurrencyDtoMapper.INSTANCE::toDtosList)
                .orElse(List.of());
    }
}