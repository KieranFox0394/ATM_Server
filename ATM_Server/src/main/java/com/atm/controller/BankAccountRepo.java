package com.atm.controller;

public interface BankAccountRepo {
	
	boolean verifyAccount(int accountID, String pin)throws Exception;
	
	int getBalance(int accountId) throws Exception;
	
	boolean withdraw(int accountid, int amount) throws Exception;

	int getOverdraftBalance(int accountId) throws Exception;

}
