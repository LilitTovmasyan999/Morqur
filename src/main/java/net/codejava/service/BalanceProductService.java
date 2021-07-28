package net.codejava.service;

import net.codejava.model.BalanceProduct;
import net.codejava.model.StorageHistory;
import net.codejava.repository.BalanceProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BalanceProductService {
    @Autowired
    private BalanceProductRepository balanceProductRepository;

    public void saveAll(List<BalanceProduct> balanceProductList) {
        balanceProductRepository.saveAll(balanceProductList);
    }
    public Date lastBalanceDate() {
        return balanceProductRepository.findTopByOrderByIdDesc().getBalanceDate();
    }

    public long getSize() {
        return balanceProductRepository.count();
    }

    public List<BalanceProduct> listAll() {
        return balanceProductRepository.findAll();
    }
    public List<BalanceProduct> listAllByDate(Date date) {
        return balanceProductRepository.findAllByBalanceDate(date);
    }

    public Optional<BalanceProduct> getById(String id) {
        return balanceProductRepository.findById(id);
    }
    public void save(BalanceProduct balanceProduct) {
        balanceProductRepository.save(balanceProduct);
    }

    public void deleteById(String id) {
        balanceProductRepository.deleteById(id);
    }

    public void deleteByProductId(String id) {
        balanceProductRepository.deleteAllByProductId(id);
    }
    public List<BalanceProduct> getAllIfConsumedByDate(Date start, Date end) {
        return balanceProductRepository.findAllByConsumeStartDateGreaterThanEqualAndConsumeEndDateLessThanEqual(start, end);
    }
 }
