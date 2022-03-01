package com.atm.controller;

import java.util.List;

import com.atm.model.Cash;

public interface ATMService {	
	
	int getCustomerBalance(int accountID, String pin) throws Exception;

	String getATMBalance();
	
	List<Cash> getCashDenominations();

	String withdraw(int accountID, String pin, int amount)throws Exception;

	int getCustomerOverdraft(int accountID, String pin) throws Exception; 
}
