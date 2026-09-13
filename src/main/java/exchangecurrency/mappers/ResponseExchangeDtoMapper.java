package exchangecurrency.mappers;

import exchangecurrency.dto.response.ResponseExchangeDto;
import exchangecurrency.dto.response.ResponseRateDto;
import exchangecurrency.entity.Currency;
import exchangecurrency.entity.Rate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

@Mapper
public interface ResponseExchangeDtoMapper {
    ResponseExchangeDtoMapper INSTANCE = Mappers.getMapper(ResponseExchangeDtoMapper.class);

    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "convertedAmount", target = "convertedAmount")
    ResponseExchangeDto toDto(ResponseRateDto responseRate,
                              BigDecimal amount,
                              BigDecimal convertedAmount);
}
