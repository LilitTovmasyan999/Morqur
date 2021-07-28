package net.codejava.model;

import lombok.Data;

@Data
public class ReplaceProductModel {
    private String id;
    private String productId;
    private Product product;
    private String name;
    private double count;
    private double oldCount;
    private String replaceDate;
    private String place;
    private String unit;

}
