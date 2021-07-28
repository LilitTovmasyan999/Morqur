package net.codejava.repository;

import net.codejava.model.BalanceProduct;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;


public interface BalanceProductRepository extends MongoRepository<BalanceProduct, String> {
    List<BalanceProduct> findAllByConsumeStartDateGreaterThanEqualAndConsumeEndDateLessThanEqual(Date start, Date end);
    List<BalanceProduct> findAllByBalanceDate(Date start);
    void deleteAllByProductId(String id);
    BalanceProduct findTopByOrderByIdDesc();
}
