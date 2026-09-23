package exchangecurrency.mappers;

import exchangecurrency.dto.request.RequestPostCurrencyDto;
import exchangecurrency.dto.response.ResponseRateDto;
import exchangecurrency.entity.Currency;
import exchangecurrency.entity.Rate;
import exchangecurrency.entity.RateCurrency;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

@Mapper
public interface ResponseRateDtoMapper {
    ResponseRateDtoMapper INSTANCE = Mappers.getMapper(ResponseRateDtoMapper.class);

    @Mapping(source = "rate.id", target = "id")
    @Mapping(source = "baseCurrency", target = "baseCurrency")
    @Mapping(source = "targetCurrency", target = "targetCurrency")
    ResponseRateDto toDto(Rate rate,
                          Currency baseCurrency,
                          Currency targetCurrency);

    @Mapping(source = "rate.id", target = "id")
    @Mapping(source = "customRate", target = "rate")
    @Mapping(source = "baseCurrency", target = "baseCurrency")
    @Mapping(source = "targetCurrency", target = "targetCurrency")
    ResponseRateDto toDto(Rate rate,
                          Currency baseCurrency,
                          Currency targetCurrency,
                          BigDecimal customRate);

    @Mapping(source = "rateCurrency.id", target = "id")
    @Mapping(source = "rateCurrency.baseCurrency", target = "baseCurrency")
    @Mapping(source = "rateCurrency.targetCurrency", target = "targetCurrency")
    ResponseRateDto toDto(RateCurrency rateCurrency);

}
