package exchangecurrency.entity;

import java.math.BigDecimal;

public class Rate {
    long id;
    long baseCurrencyId;
    long targetCurrencyId;
    BigDecimal rate;
}
