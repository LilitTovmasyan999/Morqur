package net.codejava.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import net.codejava.model.Product;
import net.codejava.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StorageProductService {

	@Autowired
	private ProductRepository repo;
	
	public List<Product> listAll() {
		return repo.findAll();
	}

	public Product findProductByName(String name, String unit) {
		return repo.findByNameAndUnit(name, unit);

	}
	public Product findProductByNameOnly(String name) {
		return repo.findByName(name);

	}

	public List<Product> listAllByDate(Date date) {
		return repo.getAllByDateIsLessThanEqualAndCountNot(date, 0);
	}

//	public List<Product> topFive(int page) {
////		Pageable pageable = new PageRequest(page, 10, Sort.Direction.DESC, "date");
////
////		List<Product> topPage = repo.findAll(pageable).getContent();
////		Collections.reverse(topPage);
//		return repo.();
//
//	}

	public Product save(Product product) {
		return repo.save(product);
	}

	public Optional<Product> get(String id) {
		return repo.findById(id);
	}

	public void delete(String id) {
		repo.deleteById(id);
	}

	public long size() {
		return repo.count();
	}
}
