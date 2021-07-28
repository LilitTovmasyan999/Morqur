package net.codejava.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class ServiceModel {
    private String id;
    private String name;
    private double price;
    private String startDate;
    private String endDate;
}
