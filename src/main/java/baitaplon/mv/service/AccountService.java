package baitaplon.mv.service;

import baitaplon.mv.entity.Account;

public interface AccountService extends Service<Account> {
	public Account findAccount(String userName);

}
