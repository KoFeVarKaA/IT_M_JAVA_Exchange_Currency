package exchangecurrency.mappers;

import exchangecurrency.dto.request.RequestPostCurrencyDto;
import exchangecurrency.dto.response.ResponseRateDto;
import exchangecurrency.entity.Currency;
import exchangecurrency.entity.Rate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

@Mapper
public interface ResponseRateDtoMapper {
    ResponseRateDtoMapper INSTANCE = Mappers.getMapper(ResponseRateDtoMapper.class);

    ResponseRateDto toDto(Rate rate);

    @Mapping(source = "customRate", target = "rate")
    ResponseRateDto toDto(Rate rate, BigDecimal customRate);

    @Mapping(source = "customRate", target = "rate")
    @Mapping(source = "baseCurrencyId", target = "baseCurrencyId")
    @Mapping(source = "targetCurrencyId", target = "targetCurrencyId")
    ResponseRateDto toDto(Rate rate,
                          long baseCurrencyId,
                          long targetCurrencyId,
                          BigDecimal customRate);

}
