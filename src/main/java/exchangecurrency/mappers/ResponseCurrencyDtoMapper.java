package exchangecurrency.mappers;

import exchangecurrency.dto.response.ResponseCurrencyDto;
import exchangecurrency.entity.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ResponseCurrencyDtoMapper {
    ResponseCurrencyDtoMapper INSTANCE = Mappers.getMapper(ResponseCurrencyDtoMapper.class);

    ResponseCurrencyDto toDto(Currency currency);
    List<ResponseCurrencyDto> toDtosList(List<Currency> currencies);
}
