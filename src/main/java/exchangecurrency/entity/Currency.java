package exchangecurrency.entity;

import lombok.Value;

@Value
public class Currency {
    long id;
    String code;
    String fullName;
    String sign;
}
