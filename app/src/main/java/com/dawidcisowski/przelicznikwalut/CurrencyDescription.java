package com.dawidcisowski.przelicznikwalut;

public class CurrencyDescription {
    private long id;
    private String name;
    private int conversion;
    private String code;
    private double averagePrice;
    public CurrencyDescription(long id, String name, int conversion,
                                String code, double averagePrice) {
        this.id = id;
        this.name = name;
        this.conversion = conversion;
        this.code = code;
        this.averagePrice = averagePrice;
    }


    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getConversion() {return conversion;}
    public void setConversion(int conversion) {
        this.conversion = conversion;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public double getAveragePrice() {
        return averagePrice;
    }
    public void setAveragePrice(float AveragePrice) {
        this.averagePrice = averagePrice;
    }
}

