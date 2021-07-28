package net.codejava.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
public class ProductModel {
	private String id;
	private String name;
	private String productType;
	private String unit;
	private double byKg;
	private double count;
	private String company;
	private double price;
	private String date;

}
