package com.atm.util;

import java.util.ArrayList;
import java.util.List;

import com.atm.model.Cash;

public class CashUtil {

	public static int getLowestDenomination(List<Cash> inCash) throws Exception {
		if (inCash == null)
			throw new Exception("Cash List is null");
		if (inCash.isEmpty())
			throw new Exception("Cash List Empty");

		int lowestDenomination = Integer.MAX_VALUE;

		for (Cash cash : inCash) {
			if (cash.getQuantity() != 0 && lowestDenomination > cash.getDenomination()) {
				lowestDenomination = cash.getDenomination();
			}
		}
		return lowestDenomination;
	}

	private static int getHighestDenomination(ArrayList<Cash> inCash) throws Exception {
		if (inCash == null)
			throw new Exception("Cash List is null");
		if (inCash.isEmpty())
			throw new Exception("Cash List Empty");

		int highestDenomination = 0;

		for (Cash cash : inCash) {
			if (cash.getQuantity() != 0 && highestDenomination < cash.getDenomination()) {
				highestDenomination = cash.getDenomination();
			}
		}
		return highestDenomination;
	}

	public static ArrayList<Cash> getDenominationsToDispense(List<Cash> inCash, int withdrawAmount) throws Exception {
		if (inCash == null) {
			throw new Exception("no cash to dispense");
		}
		if (inCash.isEmpty()) {
			throw new Exception("no cash to dispense");
		}
		if (withdrawAmount <= 0) {
			throw new Exception("cannot enter a negative withdrawal amount");
		}
		if (calculateBalance(inCash) < withdrawAmount) {
			throw new Exception("Withdraw amount greater than ATM funds");
		}

		ArrayList<Cash> cash = copyCashList(inCash, false);
		ArrayList<Cash> returnCash = copyCashList(inCash, true);

		while (withdrawAmount > 0 && calculateBalance(cash) > 0 && !cash.isEmpty()) {
			int currentDenomination = getHighestDenomination(cash);
			if (withdrawAmount - currentDenomination < 0) // go to next denomination as this would put us into negative
															// quantity
			{
				removeFromCashListByDenomination(currentDenomination, cash);
				continue;
			}

			// denomination removal will not put the balance to negative so we can take one
			adjustCashQuantity(cash, currentDenomination, -1);
			if (getCashByDenomination(cash, currentDenomination).getQuantity() == 0) {
				removeFromCashListByDenomination(currentDenomination, cash);
			}

			if (withdrawAmount >= currentDenomination) {
				adjustCashQuantity(returnCash, currentDenomination, 1);
				withdrawAmount -= currentDenomination;
			} else {
				break;
			}
		}

		if (withdrawAmount == 0)// successfully noted denominations from the atm to complete transaction
		{
			return returnCash;
		}
		if (withdrawAmount != 0 && cash.isEmpty()) {// atm does not have sufficient funds to carry out the transaction
			throw new Exception(
					"Unable to carry out transaction with denominations available, please try another amount to withdraw");
		}
		return null;
	}

	private static Cash getCashByDenomination(ArrayList<Cash> inCash, int inDenomination) {
		for (Cash cash : inCash) {
			if (cash.getDenomination() == inDenomination) {
				return cash;
			}
		}
		return null;
	}

	private static void adjustCashQuantity(ArrayList<Cash> inCash, int inDenomination, int quantity) {
		Cash cash = getCashByDenomination(inCash, inDenomination);
		if (cash != null) {
			cash.setQuantity(cash.getQuantity() + quantity);
		}
	}

	private static void removeFromCashListByDenomination(int inDenomination, ArrayList<Cash> inCash) {
		Cash cash = getCashByDenomination(inCash, inDenomination);
		if (cash != null) {
			inCash.remove(cash);
		}
	}

	private static ArrayList<Cash> copyCashList(List<Cash> inCash, boolean setQuantZero) {
		ArrayList<Cash> returnCashList = new ArrayList<>();
		for (Cash cash : inCash) {
			if (setQuantZero) {
				returnCashList.add(new Cash(0, cash.getDenomination()));
			} else {
				returnCashList.add(new Cash(cash.getQuantity(), cash.getDenomination()));
			}
		}
		return returnCashList;
	}

	private static int calculateBalance(List<Cash> inCash) {
		int total = 0;
		for (Cash cash : inCash) {
			total += cash.getDenomination() * cash.getQuantity();
		}
		return total;
	}

	public static String getOutputCash(List<Cash> inCash) {
		String output = "";
		for (Cash cash : inCash) {
			output += "(€" + cash.getDenomination() + " x " + cash.getQuantity() + ")";
		}
		output += " = " + calculateBalance(inCash);
		return output;
	}

	public static ArrayList<Cash> getCashAfterWithdrawal(ArrayList<Cash> initialCashBalance,
			ArrayList<Cash> denominationsToDispense) throws Exception {
		ArrayList<Cash> returnBalance = new ArrayList<Cash>();

		for (Cash cash : initialCashBalance) {// current quantity minus the amount to be dispensed
			int newQuantity = cash.getQuantity()
					- (getCashByDenomination(denominationsToDispense, cash.getDenomination()).getQuantity());
			if (newQuantity < 0) {
				throw new Exception("Negative denomination count");
			}
			returnBalance.add(new Cash(newQuantity, cash.getDenomination()));
		}
		return returnBalance;
	}

	public static ArrayList<Cash> getCashAfterLodgement(ArrayList<Cash> initialCashBalance, ArrayList<Cash> cashToLodge)
			throws Exception {
		ArrayList<Cash> returnBalance = new ArrayList<Cash>();

		for (Cash cash : initialCashBalance) {// current quantity plus the amount to be lodged
			int newQuantity = cash.getQuantity()
					+ (getCashByDenomination(cashToLodge, cash.getDenomination()).getQuantity());
			returnBalance.add(new Cash(newQuantity, cash.getDenomination()));
		}
		return returnBalance;
	}

}
