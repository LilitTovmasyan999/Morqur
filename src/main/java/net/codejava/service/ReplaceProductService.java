package net.codejava.service;

import net.codejava.model.BalanceProduct;
import net.codejava.model.ReplaceProduct;
import net.codejava.repository.BalanceProductRepository;
import net.codejava.repository.ReplaceProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReplaceProductService {
    @Autowired
    private ReplaceProductRepository replaceProductRepository;

    public void saveAll(List<ReplaceProduct> balanceProductList) {
        replaceProductRepository.saveAll(balanceProductList);
    }

    public Date lastBalanceDate() {
        return replaceProductRepository.findTopByOrderByIdDesc().getReplaceDate();
    }

    public long getSize() {
        return replaceProductRepository.count();
    }

    public List<ReplaceProduct> listAll() {
        return replaceProductRepository.findAll();
    }

    public List<ReplaceProduct> listAllByDate(Date date) {
        return replaceProductRepository.findAllByReplaceDate(date);
    }

    public Optional<ReplaceProduct> getById(String id) {
        return replaceProductRepository.findById(id);
    }

    public void save(ReplaceProduct balanceProduct) {
        replaceProductRepository.save(balanceProduct);
    }

    public void deleteById(String id) {
        replaceProductRepository.deleteById(id);
    }

    public void deleteByProductId(String id) {
        replaceProductRepository.deleteAllByProductId(id);
    }

    public List<ReplaceProduct> listAllByDateNamePlace(Date date1,Date date2, String name, String place) {

        return replaceProductRepository.allByReplaceDateAndNameAndPlace(date1, date2, name, place);
    }

}
