package com.atm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atm.controller.ATMService;

@Controller
@RequestMapping("/atm")
public class ServiceController {

	@Autowired
	private ATMService atmService;
	

	@RequestMapping("/")
	public @ResponseBody String welcome() {
		return ("Welcome to ATM.    ||||| /getAccountBalance/{accountID}/{pin}     |||||/withdrawFromAccount/{accountID}/{pin}/{amount}       |||||/getATMBalance");

	}

	@RequestMapping("/getAccountBalance/{accountID}/{pin}")
	public @ResponseBody String getBalance(@PathVariable(value = "accountID") int accountID,
			@PathVariable(value = "pin") String pin) {
		try {
			int balance = atmService.getCustomerBalance(accountID, pin);
			int overdraft = atmService.getCustomerOverdraft(accountID,pin);
			return ("Balance of Account " + accountID + ": €" + balance + " (€" + overdraft + " OD)");
		} catch (Exception e) {
			// e.printStackTrace();
			return e.getMessage();
		}
	}

	@RequestMapping("/withdrawFromAccount/{accountID}/{pin}/{amount}")
	public @ResponseBody String withdraw(@PathVariable(value = "accountID") int accountID,
			@PathVariable(value = "pin") String pin, @PathVariable(value = "amount") int amount) {

		try {
			return atmService.withdraw(accountID, pin, amount);
			
		} catch (Exception e) { 
			// e.printStackTrace();
			return ("Withdrawal of " + amount + " from Account " + accountID + " not successful. " + e.getMessage());
		}
	}

	@RequestMapping("/getATMBalance")
	public @ResponseBody String getATMBalance() {
		return "Balance of ATM: " + (atmService.getATMBalance());
	}

}
