package com.atm.controller;

import java.util.List;

import com.atm.model.Cash;

public interface CashRepo {
	
	int getBalance();
	
	List<Cash> withdrawCash(int withdrawAmount) throws Exception;
	
	List<Cash> findAll();

	void input(List<Cash> dispensedCash) throws Exception;
	
}

