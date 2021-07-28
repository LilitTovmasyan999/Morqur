package net.codejava.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;


import java.util.Date;
import lombok.Data;


@Document
@Data
public class Product {
	@Id
	private String id;
	private String name;
	private String productType;
	private String unit;
	private double byKg;
	private double count;
	private String company;
	private double price;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date date;

}
