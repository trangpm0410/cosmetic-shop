package baitaplon.mv.repositoryimpl;

import java.io.IOException;
import java.util.Date;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import baitaplon.mv.entity.Product;
import baitaplon.mv.form.ProductForm;
import baitaplon.mv.model.ProductInfo;
import baitaplon.mv.pagination.PaginationResult;
import baitaplon.mv.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

@Transactional
@Repository
public class ProductRepositoryImpl implements ProductRepository {
	@Autowired
	EntityManager entity;

	@Override
	public Product findProduct(String code) {
		String sql = "Select e from " + Product.class.getName() + " e Where e.code =:code ";
		TypedQuery<Product> productQuery = entity.createQuery(sql, Product.class);
		productQuery.setParameter("code", code);
		Product product = new Product();
		try {
			product = productQuery.getSingleResult();
		} catch (NoResultException e) {
			// neu khong co ban ghi nao product = null
			product = null;
		}
		return product;
	}

	@Override
	public ProductInfo findProductInfo(String code) {
		Product product = this.findProduct(code);
		if (product == null) {
			return null;
		}
		return new ProductInfo(product.getCode(), product.getName(), product.getPrice());
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	@Override
	public void save(ProductForm productForm) {
		String code = productForm.getCode();

		Product product = null;

		boolean isNew = false;
		if (code != null) {
			product = this.findProduct(code);
		}
		if (product == null) {
			isNew = true;
			product = new Product();
			product.setCreateDate(new Date());
		}
		product.setCode(code);
		product.setName(productForm.getName());
		product.setPrice(productForm.getPrice());

		if (productForm.getFileData() != null) {
			byte[] image = null;
			try {
				image = productForm.getFileData().getBytes();
			} catch (IOException e) {
			}
			if (image != null && image.length > 0) {
				product.setImage(image);
			}
		}
		if (isNew) {
			entity.persist(product);
		}
		// Nếu có lỗi tại DB, ngoại lệ sẽ ném
		entity.flush();
	}

	@Override
	public PaginationResult<ProductInfo> queryProducts(int page, int maxResult, int maxNavigationPage) {
		return queryProducts(page, maxResult, maxNavigationPage, null);
	}

	@Override
	public PaginationResult<ProductInfo> queryProducts(int page, int maxResult, int maxNavigationPage,
			String likeName) {
		String sql = "Select new " + ProductInfo.class.getName() //
				+ "(p.code, p.name, p.price) " + " from "//
				+ Product.class.getName() + " p ";
		if (likeName != null && likeName.length() > 0) {
			sql += " Where lower(p.name) like :likeName ";
		}
		sql += " order by p.createDate desc ";

		TypedQuery<ProductInfo> typequery = entity.createQuery(sql, ProductInfo.class);

		if (likeName != null && likeName.length() > 0) {
			typequery.setParameter("likeName", "%" + likeName.toLowerCase() + "%");
		}
		Query<ProductInfo> query = typequery.unwrap(Query.class);

		return new PaginationResult<ProductInfo>(query, page, maxResult, maxNavigationPage);
	}

}
