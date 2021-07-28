package net.codejava.repository;

import net.codejava.model.Product;
import net.codejava.model.Service;
import net.codejava.model.StorageHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface ServiceProductRepository extends MongoRepository<Service, String> {
    @Query("{'date' : { $gte: ?0, $lte: ?1 } }")
    List<Service> findByDate(Date start, Date end);
    @Query("{'startDate' : {$gte: ?0 }, 'endDate' : {$lte: ?1},  'name' : {$regex: ?2} }")
    List<Service> findAllByDateName(Date start, Date end, String name);


}
