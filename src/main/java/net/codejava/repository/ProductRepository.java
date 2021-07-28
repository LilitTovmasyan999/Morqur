package net.codejava.repository;


import net.codejava.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.awt.print.Pageable;
import java.util.Date;
import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
        List<Product> getAllByDateIsLessThanEqualAndCountNot(Date date, int zero);
        Product findByNameAndUnit(String name, String unit);
        Product findByName(String name);
}
