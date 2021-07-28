package net.codejava.service;

import net.codejava.model.StorageHistory;
import net.codejava.repository.ServiceProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ServiceProductService {
    @Autowired
    private ServiceProductRepository serviceProductRepository;

    public List<net.codejava.model.Service> listAll() {
        return serviceProductRepository.findAll();
    }
    public void save(net.codejava.model.Service service) {
        serviceProductRepository.save(service);
    }
    public List<net.codejava.model.Service> listAllByDate(Date start, Date end) {
        return serviceProductRepository.findByDate(start, end);
    }
    public List<net.codejava.model.Service> listAllByDateName(Date start, Date end, String name) {
        return serviceProductRepository.findAllByDateName(start, end, name);
    }
    public Optional<net.codejava.model.Service> getById(String id) {
        return serviceProductRepository.findById(id);
    }

    public void delete(net.codejava.model.Service s) {
        serviceProductRepository.delete(s);
    }



}
