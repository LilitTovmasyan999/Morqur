package net.codejava.service;

import net.codejava.model.TempReplaceProduct;
import net.codejava.repository.TempReplaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TempReplaceService {
    @Autowired
    private TempReplaceRepository tempReplaceRepository;

    public void save(TempReplaceProduct replaceProduct) {
        tempReplaceRepository.save(replaceProduct);
    }
    public long size() {
        return tempReplaceRepository.count();
    }

    public List<TempReplaceProduct> getAll() {
        return tempReplaceRepository.findAll();
    }

    public void deleteAll() {
        tempReplaceRepository.deleteAll();
    }

}
