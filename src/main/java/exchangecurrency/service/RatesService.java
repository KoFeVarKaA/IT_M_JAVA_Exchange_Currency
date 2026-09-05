package exchangecurrency.service;

import exchangecurrency.dao.jdbc.JdbcDaoCurrencies;
import exchangecurrency.dao.jdbc.JdbcDaoRates;
import exchangecurrency.dto.request.RequestGetRateDto;
import exchangecurrency.dto.request.RequestPostRateDto;
import exchangecurrency.dto.response.ResponseRateDto;
import exchangecurrency.entity.Currency;
import exchangecurrency.entity.Rate;
import exchangecurrency.exeptons.DatabaseException;
import exchangecurrency.exeptons.ObjectAlreadyExistsExceprion;
import exchangecurrency.exeptons.ObjectNotFoundException;
import exchangecurrency.mappers.RateMapper;
import exchangecurrency.mappers.ResponseCurrencyDtoMapper;
import exchangecurrency.mappers.ResponseRateDtoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.OptionalInt;

public class RatesService {
    private final JdbcDaoCurrencies daoCurrencies;
    private final JdbcDaoRates daoRates;
    private static final Logger LOGGER = LoggerFactory.getLogger(RatesService.class);

    public RatesService (JdbcDaoCurrencies daoCurrencies, JdbcDaoRates daoRates) {
        this.daoCurrencies = daoCurrencies;
        this.daoRates = daoRates;
    }

    public ResponseRateDto getRate(RequestGetRateDto dto) {
        // Существует валютная пара AB - берём её курс
        Optional<Rate> rateAB = daoRates.getByIds(dto.baseCurrencyId(), dto.targetCurrencyId());
        if (rateAB.isPresent()) {return ResponseRateDtoMapper.INSTANCE.toDto(rateAB.get());}

        // Существует валютная пара BA - берем её курс, и считаем обратный, чтобы получить AB
        Optional<Rate> rateBA = daoRates.getByIds(dto.targetCurrencyId(), dto.baseCurrencyId());
        if (rateBA.isPresent()) {
            BigDecimal reverseRate = BigDecimal.ONE.divide(
                    rateBA.get().rate(), 6, RoundingMode.HALF_UP);
            return ResponseRateDtoMapper.INSTANCE.toDto(rateBA.get(), reverseRate);
        }

        // Существует валютные пары USD-A и USD-B - вычисляем из этих курсов курс AB
        OptionalInt UsdId = daoCurrencies.getIdByCode("USD");
        if (UsdId.isPresent()) {
            Optional<Rate> rateUsdA = daoRates.getByIds(
                    String.valueOf(UsdId), dto.baseCurrencyId());
            Optional<Rate> rateUsdB = daoRates.getByIds(
                    String.valueOf(UsdId), dto.targetCurrencyId());
            if (rateUsdA.isPresent() && rateUsdB.isPresent()) {
                BigDecimal rateA = rateUsdA.get().rate();
                BigDecimal rateB = rateUsdB.get().rate();
                BigDecimal resultRate = rateB.divide(rateA, 6, RoundingMode.HALF_UP);
                return ResponseRateDtoMapper.INSTANCE.toDto(
                        rateUsdA.get(),
                        rateUsdA.get().targetCurrencyId(),
                        rateUsdB.get().targetCurrencyId(),
                        resultRate
                );
            }
        }
        String idA = dto.baseCurrencyId();
        String idB = dto.targetCurrencyId();
        String message = "Курс обмена для валютный пар с id " + idA+":"+idB +", "+ idB+":"+idA
                +" или "+ UsdId+":"+idA +" и "+ UsdId+":"+idB + " не найден";
        LOGGER.warn(message);
        throw new ObjectNotFoundException(message);
    }

    public ResponseRateDto postRate(RequestPostRateDto dto){
        String rateIds = dto.baseCurrencyId() +" и/или "+ dto.targetCurrencyId();

        Optional<Currency> baseCurrency = daoCurrencies.getById(String.valueOf(
                dto.baseCurrencyId()));
        Optional<Currency> targetCurrency = daoCurrencies.getById(String.valueOf(
                dto.targetCurrencyId()));
        if (baseCurrency.isEmpty() || targetCurrency.isEmpty()) {
            String message = "Валюты с id = " +rateIds+ "не найдены";
            LOGGER.warn(message);
            throw new ObjectNotFoundException(message);
        }

        Optional<Rate> rateInstanse = daoRates.getByIds(String.valueOf(dto.baseCurrencyId()),
                String.valueOf(dto.targetCurrencyId()));
        if (rateInstanse.isPresent()) {
            String message = "Обменный курс для валют с id = " +rateIds+ "уже существует";
            LOGGER.warn(message);
            throw new ObjectAlreadyExistsExceprion(message);
        }

        daoRates.post(RateMapper.INSTANCE.toEntity(dto));
        Optional<Rate> savedRateOptional = daoRates.getByIds(
                String.valueOf(dto.baseCurrencyId()), String.valueOf(dto.targetCurrencyId()));
        if (savedRateOptional.isEmpty()) {
            String message = "Ошибка создания или получения обменного курса для валют с id = "
                    +rateIds;
            LOGGER.error(message);
            throw new DatabaseException(message);
        }
        return ResponseRateDtoMapper.INSTANCE.toDto(savedRateOptional.get());
    }

    public void updateRate(RequestPostRateDto dto){
        daoRates.update(RateMapper.INSTANCE.toEntity(dto));
    }
}
