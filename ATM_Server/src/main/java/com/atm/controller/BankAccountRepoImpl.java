package com.atm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.atm.util.DBUtil;

@Repository
public class BankAccountRepoImpl implements BankAccountRepo {

	@Autowired
	private JdbcTemplate template;

	@Autowired
	public BankAccountRepoImpl() {
		this.template = DBUtil.getJdbcTemplate();
	}

	@Override
	public boolean verifyAccount(int accountID, String pin) throws Exception {
		try {
			return template.queryForObject("SELECT count(*) FROM TBL_ACCOUNTS WHERE accountid=? and pin=?",
					Integer.class, new Object[] { accountID, pin }) == 1;
		} catch (EmptyResultDataAccessException e) {
			return false;
		} catch (DataAccessException e) {
			throw new Exception("error verifying bank account balance");
		}
	}

	@Override
	public int getBalance(int accountId) throws Exception {
		try {
			return template.queryForObject("SELECT balance FROM TBL_ACCOUNTS WHERE accountid=?", Integer.class,
					new Object[] { accountId });
		} catch (DataAccessException e) {
			throw new Exception("error retrieving bank account balance");
		}
	}

	@Override
	public int getOverdraftBalance(int accountId) throws Exception {
		try {
			return template.queryForObject("SELECT overdraft FROM TBL_ACCOUNTS WHERE accountid=?", Integer.class,
					new Object[] { accountId });
		} catch (DataAccessException e) {
			throw new Exception("error retrieving bank account overdraft");
		}
	}

	@Override
	public boolean withdraw(int accountid, int amount) throws Exception {
		int currentBalance = getBalance(accountid);
		int updateBalance = currentBalance - amount;

		if ((updateBalance) < 0) {// check if it allowed as per overdraft
			int currentOverdraft = getOverdraftBalance(accountid);
			if (updateBalance < -(currentOverdraft)) {
				throw new Exception("Bank Account balance insufficient");
			} else {// overdraft to be used.
				int updateOverdraft = currentOverdraft - (amount - currentBalance);
				String sqlBalance = "UPDATE TBL_ACCOUNTS set balance = 0 WHERE accountid = " + accountid;
				String sqlOverdraft = "UPDATE TBL_ACCOUNTS set overdraft = " + updateOverdraft + "WHERE accountid = " + accountid;
				try {
					template.update(sqlBalance);
					template.update(sqlOverdraft);
				} catch (DataAccessException e) {
					throw new Exception("error updating bank account balance");
				}
				return true;
			}
		} else {// regular scenario

			String sql = "UPDATE TBL_ACCOUNTS set balance = " + updateBalance + "WHERE accountid = " + accountid;
			try {
				template.update(sql);
			} catch (DataAccessException e) {
				throw new Exception("error updating bank account balance");
			}
			return true;
		}
	}
}
