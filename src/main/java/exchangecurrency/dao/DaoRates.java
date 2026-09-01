package exchangecurrency.dao;

import exchangecurrency.entity.Rate;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public interface DaoRates {
    void createTable();
    void deleteTable();
    void post(Rate dto);
    Optional<Rate> getById(String id);
    Optional<Rate> getByIds(String baseCurrencyId, String targetCurrencyId);
    Optional<List<Rate>> getAll();
    void update(Rate dto);
    void delete(int id);
}
