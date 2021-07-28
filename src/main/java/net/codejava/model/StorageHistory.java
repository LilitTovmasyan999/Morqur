package net.codejava.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Document
@Data
public class StorageHistory {
    @Id
    private String id;
    private String productId;
    private String name;
    private String productType;
    private String unit;
    private double count;
    private String company;
    private double price;
    private double totalPrice;
    private boolean in;
    private double oldCount;
    private double oldPrice;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date oldDate;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date date;
}
