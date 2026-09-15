package exchangecurrency.mappers;

import exchangecurrency.dto.request.RequestPatchRateDto;
import exchangecurrency.entity.Rate;
import org.mapstruct.factory.Mappers;

public interface RateMapper {
    RateMapper INSTANCE = Mappers.getMapper(RateMapper.class);

    Rate toEntity(RequestPatchRateDto dto);
}
