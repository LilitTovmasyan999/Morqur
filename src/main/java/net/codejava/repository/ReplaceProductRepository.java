package net.codejava.repository;

import net.codejava.model.BalanceProduct;
import net.codejava.model.ReplaceProduct;
import net.codejava.model.Service;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;


public interface ReplaceProductRepository extends MongoRepository<ReplaceProduct, String> {
//    List<BalanceProduct> findAllByConsumeStartDateGreaterThanEqualAndConsumeEndDateLessThanEqual(Date start, Date end);
    List<ReplaceProduct> findAllByReplaceDate(Date start);
    void deleteAllByProductId(String id);
    ReplaceProduct findTopByOrderByIdDesc();

    @Query("{'replaceDate' :  { $gte: ?0, $lte: ?1 }, 'name' : {$regex: ?2}, 'place' : {$regex: ?3} }")
    List<ReplaceProduct> allByReplaceDateAndNameAndPlace(Date date1, Date date2,  String name, String place);

}
