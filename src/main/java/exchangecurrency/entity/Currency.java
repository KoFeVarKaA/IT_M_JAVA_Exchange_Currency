package exchangecurrency.entity;

public record Currency (
    long id,
    String code,
    String fullName,
    String sign
){}
