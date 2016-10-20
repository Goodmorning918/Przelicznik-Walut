package com.dawidcisowski.Przelicznik_Walut;

public class CurrencyDescription {
    private String name;
    private String code;
    private String country;
    private Double rate;

    public CurrencyDescription(String name, String code,String country, Double rate) {
        this.name = name;
        this.code = code;
        this.rate = rate;
        this.country=country;
    }

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public Double getRate() {

        return (rate);
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }
}

