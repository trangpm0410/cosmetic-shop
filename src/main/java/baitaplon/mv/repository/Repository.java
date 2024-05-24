package baitaplon.mv.repository;

import java.util.List;

public interface Repository<T> {
	List<T> findAll();

	T findById(String id);

	void save(T model);

	void remove(Long id);
}