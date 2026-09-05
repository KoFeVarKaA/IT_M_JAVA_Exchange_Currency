package exchangecurrency.mappers;

import exchangecurrency.dto.request.RequestPostCurrencyDto;
import exchangecurrency.dto.request.RequestPostRateDto;
import exchangecurrency.entity.Currency;
import exchangecurrency.entity.Rate;
import org.mapstruct.factory.Mappers;

public interface RateMapper {
    RateMapper INSTANCE = Mappers.getMapper(RateMapper.class);

    Rate toEntity(RequestPostRateDto dto);
}
