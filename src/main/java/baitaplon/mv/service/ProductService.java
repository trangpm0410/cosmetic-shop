package baitaplon.mv.service;

import baitaplon.mv.entity.Product;
import baitaplon.mv.form.ProductForm;
import baitaplon.mv.model.ProductInfo;
import baitaplon.mv.pagination.PaginationResult;

public interface ProductService {
	public Product findProduct(String code);

	public ProductInfo findProductInfo(String code);

	public void save(ProductForm productForm);

	public PaginationResult<ProductInfo> queryProducts(int page, int maxResult, int maxNavigationPage);
}
