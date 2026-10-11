package exchangecurrency.mappers;

import exchangecurrency.dto.request.RequestPostCurrencyDto;
import exchangecurrency.entity.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CurrencyMapper {
    CurrencyMapper INSTANCE = Mappers.getMapper(CurrencyMapper.class);

    @Mapping(target = "id", constant = "0L")
    Currency toEntity(RequestPostCurrencyDto dto);
}
