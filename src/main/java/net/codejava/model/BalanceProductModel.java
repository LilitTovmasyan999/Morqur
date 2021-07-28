package net.codejava.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class BalanceProductModel {
    private String id;
    private String productId;
    private Product product;
    private double count;
    private double consumeCount;
    private String balanceDate;
    private int consumePeopleCount;
    private String consumeStartDate;
    private String consumeEndDate;
    private String storageHistoryId;
}
