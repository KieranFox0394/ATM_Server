package com.atm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atm.model.Cash;
import com.atm.util.CashUtil;

@Service
public class ATMServiceImpl implements ATMService {

	@Autowired
	private BankAccountRepo bankAccountRepo;

	@Autowired
	private CashRepo cashRepo;

	@Override
	public int getCustomerBalance(int accountID, String pin) throws Exception {
		if (bankAccountRepo.verifyAccount(accountID, pin)) {
			// pin and account has been verified, call to gather balance
			return bankAccountRepo.getBalance(accountID);
		}
		throw new Exception("Account not found or Incorrect PIN");
	}

	@Override
	public int getCustomerOverdraft(int accountID, String pin) throws Exception {
		if (bankAccountRepo.verifyAccount(accountID, pin)) {
			// pin and account has been verified, call to gather balance
			return bankAccountRepo.getOverdraftBalance(accountID);
		}
		throw new Exception("Account not found or Incorrect PIN");
	}

	@Override
	public String getATMBalance() {
		return CashUtil.getOutputCash(cashRepo.findAll());
	}

	@Override
	public List<Cash> getCashDenominations() {
		return cashRepo.findAll();
	}

	@Override
	public String withdraw(int accountID, String pin, int withdrawAmount) throws Exception {
		if (bankAccountRepo.verifyAccount(accountID, pin) == false) { // ensure that the customer has the credentials to
																		// make a withdrawal
			throw new Exception("Account not found or Incorrect PIN");
		}
//		if (bankAccountRepo.getBalance(accountID) < withdrawAmount) { // ensure that the customer has funds to make withdrawal
//			throw new Exception("Account balance not sufficient to make withdrawal");
//		}
		// now we proceed with the withdrawal from the cash reserves and if successful
		// we withdraw from the BankAccount

		List<Cash> dispensedCash = cashRepo.withdrawCash(withdrawAmount);
		if (dispensedCash != null) {// if it fails it should be handled, but in case we check if null is
									// returned
			try {
				if (bankAccountRepo.withdraw(accountID, withdrawAmount)) {// if the withdrawal to the bank account was
																			// successful

					return CashUtil.getOutputCash(dispensedCash) + ". New Balance: €"
							+ getCustomerBalance(accountID, pin) + " (€" + getCustomerOverdraft(accountID, pin)
							+ " OD) ";
				}
			} catch (Exception e)// something went wrong with the account withdrawal, set the cash reserves back
			{
				cashRepo.input(dispensedCash);
				return e.getMessage();

			}
		}
		return null;

	}
}
