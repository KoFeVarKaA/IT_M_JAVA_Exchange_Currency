package exchangecurrency.dao;

import exchangecurrency.entity.Rate;
import exchangecurrency.entity.RateCurrency;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public interface DaoRates {
    boolean isEmpty();
    void createTable();
    void deleteTable();
    void post(Rate dto);
    Optional<Rate> getById(String id);
    Optional<Rate> getByIds(String baseCurrencyId, String targetCurrencyId);
    Optional<List<RateCurrency>> getAll();
    void update(Rate dto);
    void delete(int id);
}
