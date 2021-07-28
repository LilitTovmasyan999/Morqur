package net.codejava.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Document
public class BalanceProduct {
    @Id
    private String id;
    private Product product;
    private double count;
    private double consumeCount;
    private int consumePeopleCount;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date consumeStartDate;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date consumeEndDate;
    private String storageHistoryId;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date balanceDate;
}
