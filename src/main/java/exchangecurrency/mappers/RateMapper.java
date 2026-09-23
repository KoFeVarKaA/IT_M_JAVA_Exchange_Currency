package exchangecurrency.mappers;

import exchangecurrency.dto.request.RequestPatchRateDto;
import exchangecurrency.entity.Rate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

@Mapper
public interface RateMapper {
    RateMapper INSTANCE = Mappers.getMapper(RateMapper.class);

    Rate toEntity(long id, long baseCurrencyId, long targetCurrencyId, BigDecimal rate);
}
