package net.codejava.repository;


import net.codejava.model.Product;
import net.codejava.model.StorageHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface StorageHistoryRepository extends MongoRepository<StorageHistory, String> {

    @Query("{'date' : { $gte: ?0, $lte: ?1 } }")
    List<StorageHistory> findByDate(Date start, Date end);


    List<StorageHistory> findAllByDateIsLessThanEqual(Date date);

    List<StorageHistory> findAllByProductId(String id);


    @Query("{'date' : { $gte: ?0, $lte: ?1 }, 'in' : false }")
    List<StorageHistory> findAllByInIsFalse(Date start, Date end);


    @Query("{'date' : { $gte: ?0, $lte: ?1 }, 'in' : ?2 , 'name' : {$regex: ?3},  'company' : {$regex: ?4}, 'productType' : {$regex: ?5} }")
    List<StorageHistory> findAllByDateNameCompanyIn(Date start, Date end, boolean in, String name, String company, String productType);


}
