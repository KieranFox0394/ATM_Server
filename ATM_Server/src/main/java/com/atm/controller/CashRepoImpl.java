package com.atm.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atm.model.Cash;
import com.atm.util.CashUtil;
import com.atm.util.DBUtil;

@Service
public class CashRepoImpl implements CashRepo {

	@Autowired
	private JdbcTemplate template;

	@Autowired
	public CashRepoImpl() {
		this.template = DBUtil.getJdbcTemplate();
	}

	@Transactional
	public int getBalance() {
		List<Cash> allCash = findAll();
		int total = 0;
		for (Cash cash : allCash) {
			total += (cash.getDenomination() * cash.getQuantity());
		}
		return total;
	}

	@Transactional
	public List<Cash> findAll() {
		String sql = "SELECT * FROM TBL_CASH";
		List<Cash> cash = template.query(sql, new BeanPropertyRowMapper<Cash>(Cash.class));
		return cash;
	}

	@Transactional
	public List<Cash> withdrawCash(int withdrawAmount) throws Exception {
		List<Cash> cash = findAll();

		// check if we have a balance to cover the withdrawAmount
		if (getBalance() < withdrawAmount)
			throw new Exception("Withdraw amount greater than ATM funds");
		List<Cash> denominationsToDispense;
		try {
			denominationsToDispense = CashUtil.getDenominationsToDispense(cash, withdrawAmount);
		} catch (Exception e) {
			throw new Exception("cash withdrawal failed to update");
		}

		ArrayList<Cash> newCashBalance = CashUtil.getCashAfterWithdrawal((ArrayList<Cash>) cash,
				(ArrayList<Cash>) denominationsToDispense);

		for (Cash cashUpdate : newCashBalance) {
			String sql = "UPDATE TBL_CASH set quantity = " + cashUpdate.getQuantity() + " WHERE denomination = "
					+ cashUpdate.getDenomination();
			try {
				template.update(sql);
			} catch (DataAccessException e) {
				throw new Exception("update to cash withdrawal failed" + e.getMessage());
			}
		}

		return denominationsToDispense;

	}

	@Transactional
	public void input(List<Cash> cashToLodge) throws Exception {
		// push funds into already lodged funds

		ArrayList<Cash> newCashBalance = CashUtil.getCashAfterLodgement((ArrayList<Cash>)findAll(), (ArrayList<Cash>)cashToLodge);

		for (Cash cashUpdate : newCashBalance) {
			String sql = "UPDATE TBL_CASH set quantity = " + cashUpdate.getQuantity() + " WHERE denomination = "
					+ cashUpdate.getDenomination();
			try {
				template.update(sql);
			} catch (DataAccessException e) {
				throw new Exception("update to cash lodgement failed" + e.getMessage());
			}
		}

	}
}
