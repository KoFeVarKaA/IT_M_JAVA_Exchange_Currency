package exchangecurrency.service;

import exchangecurrency.dao.jdbc.JdbcDaoCurrencies;
import exchangecurrency.dao.jdbc.JdbcDaoRates;
import exchangecurrency.dto.request.RequestGetRateDto;
import exchangecurrency.dto.request.RequestPatchRateDto;
import exchangecurrency.dto.response.ResponseRateDto;
import exchangecurrency.entity.Currency;
import exchangecurrency.entity.Rate;
import exchangecurrency.exeptons.DatabaseException;
import exchangecurrency.exeptons.ObjectAlreadyExistsException;
import exchangecurrency.exeptons.ObjectNotFoundException;
import exchangecurrency.mappers.RateMapper;
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
        Optional<Currency> baseCurrencyOpt = daoCurrencies.getByCode(dto.baseCurrencyCode());
        Optional<Currency> targetCurrencyOpt = daoCurrencies.getByCode(dto.targetCurrencyCode());
        if (baseCurrencyOpt.isEmpty() || targetCurrencyOpt.isEmpty()) {
            LOGGER.warn("Валюты с code = '{}' и/или '{}' не найдены",
                    dto.baseCurrencyCode(), dto.targetCurrencyCode());
            throw new ObjectNotFoundException("Валюты с code = '%s' и/или '%s' не найдены"
                    .formatted(dto.baseCurrencyCode(), dto.targetCurrencyCode()));
        }

        String baseCurrencyId = String.valueOf(baseCurrencyOpt.get().id());
        String targetCurrencyId = String.valueOf(targetCurrencyOpt.get().id());

        // Существует валютная пара AB (base-target) - берём её курс
        Optional<Rate> rateAB = daoRates.getByIds(baseCurrencyId, targetCurrencyId);
        if (rateAB.isPresent()) {return ResponseRateDtoMapper.INSTANCE.toDto(
                rateAB.get(), baseCurrencyOpt.get(), targetCurrencyOpt.get());}

        // Существует валютная пара BA (target-base) - берем её курс,
        // и считаем обратный, чтобы получить AB
        Optional<Rate> rateBA = daoRates.getByIds(targetCurrencyId, baseCurrencyId);
        if (rateBA.isPresent()) {
            BigDecimal reverseRate = BigDecimal.ONE.divide(
                    rateBA.get().rate(), 6, RoundingMode.HALF_UP);
            return ResponseRateDtoMapper.INSTANCE.toDto(
                    rateBA.get(), baseCurrencyOpt.get(), targetCurrencyOpt.get(), reverseRate);
        }

        // Существует валютные пары USD-A и USD-B - вычисляем из этих курсов курс AB
        OptionalInt UsdId = daoCurrencies.getIdByCode("USD");
        if (UsdId.isPresent()) {
            Optional<Rate> rateUsdA = daoRates.getByIds(
                    String.valueOf(UsdId), baseCurrencyId);
            Optional<Rate> rateUsdB = daoRates.getByIds(
                    String.valueOf(UsdId), targetCurrencyId);
            if (rateUsdA.isPresent() && rateUsdB.isPresent()) {
                BigDecimal rateA = rateUsdA.get().rate();
                BigDecimal rateB = rateUsdB.get().rate();
                BigDecimal resultRate = rateB.divide(rateA, 6, RoundingMode.HALF_UP);
                return ResponseRateDtoMapper.INSTANCE.toDto(
                        rateUsdA.get(),
                        baseCurrencyOpt.get(),
                        targetCurrencyOpt.get(),
                        resultRate
                );
            }
        } else {LOGGER.error("ВАЛЮТА USD НЕ НАЙДЕНА. Создайте валюту code = 'USD'");}
        String message = """
                Курс обмена для валютных пар с id %s:%s, %s:%s или %s:%s и %s:%s не найден\
                """.formatted(baseCurrencyId, targetCurrencyId, targetCurrencyId,
                baseCurrencyId, UsdId, baseCurrencyId, UsdId, targetCurrencyId);
        LOGGER.warn("{}", message);
        throw new ObjectNotFoundException(message);
    }

    public ResponseRateDto postUpdateRate(RequestPatchRateDto dto, boolean isNew){
        String rateCodesMessage = "с code = '%s' и/или '%s'"
                .formatted(dto.baseCurrencyCode(), dto.targetCurrencyCode());

        Optional<Currency> baseCurrencyOpt = daoCurrencies.getByCode(dto.baseCurrencyCode());
        Optional<Currency> targetCurrencyOpt = daoCurrencies.getByCode(dto.targetCurrencyCode());
        if (baseCurrencyOpt.isEmpty() || targetCurrencyOpt.isEmpty()) {
            LOGGER.warn(" Валюты {} не найдены", rateCodesMessage);
            throw new ObjectNotFoundException("Валюты " +rateCodesMessage+ " не найдены");
        }

        String baseCurrencyId = String.valueOf(baseCurrencyOpt.get().id());
        String targetCurrencyId = String.valueOf(targetCurrencyOpt.get().id());

        if (isNew) {
            Optional<Rate> rateInstance = daoRates.getByIds(baseCurrencyId, targetCurrencyId);
            if (rateInstance.isPresent()) {
                String message = "Обменный курс для валют " + rateCodesMessage + "уже существует";
                LOGGER.warn("{}", message);
                throw new ObjectAlreadyExistsException(message);
            }

            daoRates.post(RateMapper.INSTANCE.toEntity(dto));
        } else {
            daoRates.update(RateMapper.INSTANCE.toEntity(dto));
        }
        Optional<Rate> savedRateOpt = daoRates.getByIds(baseCurrencyId, targetCurrencyId);
        if (savedRateOpt.isEmpty()) {
        String message = "Ошибка создания или получения обменного курса для валют "
                +rateCodesMessage;;
        LOGGER.error("{}", message);
        throw new DatabaseException(message);
        }
        return ResponseRateDtoMapper.INSTANCE.toDto(
                savedRateOpt.get(), baseCurrencyOpt.get(), targetCurrencyOpt.get());
    }
}
