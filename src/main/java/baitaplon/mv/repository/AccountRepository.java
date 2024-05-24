package baitaplon.mv.repository;

import baitaplon.mv.entity.Account;

public interface AccountRepository extends Repository<Account> {
	public Account findAccountByUserName(String userName);
}
