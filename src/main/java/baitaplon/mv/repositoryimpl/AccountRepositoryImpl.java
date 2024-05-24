package baitaplon.mv.repositoryimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import baitaplon.mv.entity.Account;
import baitaplon.mv.repository.AccountRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Repository
public class AccountRepositoryImpl implements AccountRepository {
	@Autowired
	EntityManager entityManager;

	@Override
	public List<Account> findAll() {
		String query = "SELECT acc FROM Account acc";
		TypedQuery<Account> listAccounts = entityManager.createQuery(query, Account.class);
		return listAccounts.getResultList();
	}

	@Override
	public Account findById(String id) {
		// TODO Auto-generated method stub
		return entityManager.find(Account.class, id);
	}

	@Override
	public void save(Account model) {
		if (model.getUserName() != null) {
			entityManager.merge(model);
		} else {
			entityManager.persist(model);
		}
	}

	@Override
	public void remove(Long id) {

	}

	@Override
	public Account findAccountByUserName(String userName) {
		TypedQuery<Account> query = entityManager
				.createQuery("SELECT acc FROM Account acc WHERE acc.userName = :userName", Account.class);
		query.setParameter("userName", userName);
		try {
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}

	}

}
