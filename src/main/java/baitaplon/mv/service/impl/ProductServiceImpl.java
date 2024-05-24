package baitaplon.mv.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import baitaplon.mv.entity.Product;
import baitaplon.mv.form.ProductForm;
import baitaplon.mv.model.ProductInfo;
import baitaplon.mv.pagination.PaginationResult;
import baitaplon.mv.repositoryimpl.ProductRepositoryImpl;
import baitaplon.mv.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	ProductRepositoryImpl repo;

	@Override
	public Product findProduct(String code) {
		return repo.findProduct(code);
	}

	@Override
	public ProductInfo findProductInfo(String code) {
		return repo.findProductInfo(code);
	}

	@Override
	public void save(ProductForm productForm) {
		repo.save(productForm);
	}

	@Override
	public PaginationResult<ProductInfo> queryProducts(int page, int maxResult, int maxNavigationPage) {

		return repo.queryProducts(page, maxResult, maxNavigationPage);
	}

}
