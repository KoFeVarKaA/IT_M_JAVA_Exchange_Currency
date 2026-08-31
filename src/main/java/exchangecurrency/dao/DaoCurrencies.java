package exchangecurrency.dao;

import exchangecurrency.entity.Currency;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public interface DaoCurrencies {
    void createTable();
    void deleteTable();
    void post(Currency dto);
    Optional<Currency> getById(String id);
    Optional<Currency> getByCode(String code);
    Optional<List<Currency>> getAll();
    OptionalInt get_id_by_code(String code);
    void update(Currency dto);
    void delete(long id);
}
