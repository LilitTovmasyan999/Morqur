package net.codejava.repository;

import net.codejava.model.TempBalanceProduct;
import net.codejava.model.TempReplaceProduct;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TempReplaceRepository extends MongoRepository<TempReplaceProduct, String> {
}
