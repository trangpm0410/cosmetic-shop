package baitaplon.mv.service;

import java.util.List;

public interface Service<T> {
	List<T> findAll();

	T findById(String id);

	void save(T model);

	void remove(Long id);
}
