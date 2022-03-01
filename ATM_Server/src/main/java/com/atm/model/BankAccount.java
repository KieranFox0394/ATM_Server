package com.atm.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BankAccount{
		
		@JsonProperty
		private int accountId;
		
		@JsonProperty
		private String pin;
		
		@JsonProperty
		private int balance;
		
		@JsonProperty
		private int overdraft;

		public int getAccountId() {
			return accountId;
		}

		public void setAccountId(int accountId) {
			this.accountId = accountId;
		}

		public String getPin() {
			return pin;
		}

		public void setPin(String pin) {
			this.pin = pin;
		}

		public int getBalance() {
			return balance;
		}

		public void setBalance(int balance) {
			this.balance = balance;
		}

		public int getOverdraft() {
			return overdraft;
		}

		public void setOverdraft(int overdraft) {
			this.overdraft = overdraft;
		}

		
		
}
