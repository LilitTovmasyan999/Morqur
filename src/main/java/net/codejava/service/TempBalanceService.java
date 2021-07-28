package net.codejava.service;

import net.codejava.model.BalanceProduct;
import net.codejava.model.TempBalanceProduct;
import net.codejava.repository.TempBalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TempBalanceService {
    @Autowired
    private TempBalanceRepository tempBalanceRepository;

    public void save(TempBalanceProduct balanceProduct) {
        tempBalanceRepository.save(balanceProduct);
    }
    public long size() {
        return tempBalanceRepository.count();
    }

    public List<TempBalanceProduct> getAll() {
        return tempBalanceRepository.findAll();
    }

    public void deleteAll() {
        tempBalanceRepository.deleteAll();
    }

}
