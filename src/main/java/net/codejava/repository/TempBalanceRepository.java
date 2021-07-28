package net.codejava.repository;

import net.codejava.model.BalanceProduct;
import net.codejava.model.TempBalanceProduct;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TempBalanceRepository extends MongoRepository<TempBalanceProduct, String> {
}
