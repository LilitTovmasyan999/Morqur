package net.codejava.model;

import lombok.Data;

import java.util.Date;

@Data
public class Filter {
    private String filterProductName;
    private String filterCompanyName;
    private String productType;
    private boolean filterProductIn;
    private String date1;
    private String date2;
}
