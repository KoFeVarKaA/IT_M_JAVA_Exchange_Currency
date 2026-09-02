package exchangecurrency.mappers;

import exchangecurrency.dto.request.RequestPostCurrencyDto;
import exchangecurrency.entity.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CurrencyMapper {
    CurrencyMapper INSTANCE = Mappers.getMapper(CurrencyMapper.class);

    Currency toEntity(RequestPostCurrencyDto dto);
}
