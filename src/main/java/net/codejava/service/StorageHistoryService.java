package net.codejava.service;

import net.codejava.model.StorageHistory;
import net.codejava.repository.StorageHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StorageHistoryService {
    @Autowired
    private StorageHistoryRepository storageHistoryRepository;

    public void save(StorageHistory product) {
        storageHistoryRepository.save(product);
    }

    public List<StorageHistory> listAll() {
        return storageHistoryRepository.findAll();
    }

    public List<StorageHistory> listAllByDate(Date start, Date end) {
        return storageHistoryRepository.findByDate(start, end);
    }
    public List<StorageHistory> listAllByDateNameCompanyIn(Date start, Date end, String product, String company, boolean in, String productType) {
        return storageHistoryRepository.findAllByDateNameCompanyIn(start, end, in, product, company, productType);
    }

    public List<StorageHistory> getAllIfConsumedByDate(Date start, Date end) {
        return storageHistoryRepository.findAllByInIsFalse(start, end);
    }


    public List<StorageHistory> findAllByDateIsLessThanEqual(Date date) {
        return storageHistoryRepository.findAllByDateIsLessThanEqual(date);
    }
    public List<StorageHistory> getByProduct(String id) {
        return storageHistoryRepository.findAllByProductId(id);
    }
    public Optional<StorageHistory> getById(String id) {
        return storageHistoryRepository.findById(id);
    }


    public void delete(StorageHistory s) {
        storageHistoryRepository.delete(s);
    }

    public void deleteById(String id) {
        storageHistoryRepository.deleteById(id);
    }


}
