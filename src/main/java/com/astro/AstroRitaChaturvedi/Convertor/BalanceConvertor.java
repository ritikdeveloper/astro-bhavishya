package com.astro.AstroRitaChaturvedi.Convertor;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class BalanceConvertor implements AttributeConverter<Double, String> {

    @Override
    public String convertToDatabaseColumn(Double balance) {
        return balance != null ? AESEncryptionUtil.encrypt(balance.toString()) : null;
    }

    @Override
    public Double convertToEntityAttribute(String encryptedBalance) {
        return encryptedBalance != null ? Double.parseDouble(AESEncryptionUtil.decrypt(encryptedBalance)) : null;
    }
}
