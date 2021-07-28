package net.codejava.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Document
public class TempReplaceProduct {
    @Id
    private String id;
    private Product product;
    private String name;
    private double count;
    private double oldCount;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date replaceDate;
    private String place;
    private String unit;
}
