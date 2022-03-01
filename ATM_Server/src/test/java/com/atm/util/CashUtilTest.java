package com.atm.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.atm.model.Cash;

@SpringBootTest
class CashUtilTest {
	ArrayList<Cash> cash;
	int withdrawalAmount;

	
	@Test
	void testWithdrawal1() throws Exception {
		cash =  new ArrayList<Cash>();
		cash.add(new Cash(10, 50));
		cash.add(new Cash(30, 20));
		cash.add(new Cash(20, 10));
		cash.add(new Cash(30, 5));//1500
		
		withdrawalAmount = 450;
		
		ArrayList<Cash> dispensedCash = CashUtil.getDenominationsToDispense(cash, withdrawalAmount);
		
		ArrayList<Cash> expectedDispensedCash = new ArrayList<>();
		expectedDispensedCash.add(new Cash(9, 50));
		expectedDispensedCash.add(new Cash(0, 20));
		expectedDispensedCash.add(new Cash(0, 10));
		expectedDispensedCash.add(new Cash(0, 5));
				
		assertEquals(dispensedCash.get(0).getDenomination(), expectedDispensedCash.get(0).getDenomination());
		assertEquals(dispensedCash.get(1).getDenomination(), expectedDispensedCash.get(1).getDenomination());
		assertEquals(dispensedCash.get(2).getDenomination(), expectedDispensedCash.get(2).getDenomination());
		assertEquals(dispensedCash.get(3).getDenomination(), expectedDispensedCash.get(3).getDenomination());

		assertEquals(dispensedCash.get(0).getQuantity(), expectedDispensedCash.get(0).getQuantity());
		assertEquals(dispensedCash.get(1).getQuantity(), expectedDispensedCash.get(1).getQuantity());
		assertEquals(dispensedCash.get(2).getQuantity(), expectedDispensedCash.get(2).getQuantity());
		assertEquals(dispensedCash.get(3).getQuantity(), expectedDispensedCash.get(3).getQuantity());
	
	}
	
	@Test
	void testWithdrawal2() throws Exception {
		cash =  new ArrayList<Cash>();
		cash.add(new Cash(10, 50));
		cash.add(new Cash(30, 20));
		cash.add(new Cash(20, 10));
		cash.add(new Cash(30, 5));
		
		withdrawalAmount = 525;
		
		ArrayList<Cash> dispensedCash = CashUtil.getDenominationsToDispense(cash, withdrawalAmount);
		
		ArrayList<Cash> expectedDispensedCash = new ArrayList<>();
		expectedDispensedCash.add(new Cash(10, 50));
		expectedDispensedCash.add(new Cash(1, 20));
		expectedDispensedCash.add(new Cash(0, 10));
		expectedDispensedCash.add(new Cash(1, 5));
				
		assertEquals(dispensedCash.get(0).getDenomination(), expectedDispensedCash.get(0).getDenomination());
		assertEquals(dispensedCash.get(1).getDenomination(), expectedDispensedCash.get(1).getDenomination());
		assertEquals(dispensedCash.get(2).getDenomination(), expectedDispensedCash.get(2).getDenomination());
		assertEquals(dispensedCash.get(3).getDenomination(), expectedDispensedCash.get(3).getDenomination());

		assertEquals(dispensedCash.get(0).getQuantity(), expectedDispensedCash.get(0).getQuantity());
		assertEquals(dispensedCash.get(1).getQuantity(), expectedDispensedCash.get(1).getQuantity());
		assertEquals(dispensedCash.get(2).getQuantity(), expectedDispensedCash.get(2).getQuantity());
		assertEquals(dispensedCash.get(3).getQuantity(), expectedDispensedCash.get(3).getQuantity());
	
	}
	
	@Test
	void testWithdrawal3() throws Exception {
		cash =  new ArrayList<Cash>();
		cash.add(new Cash(10, 50));
		cash.add(new Cash(30, 20));
		cash.add(new Cash(30, 10));
		cash.add(new Cash(20, 5));//1500
		
		withdrawalAmount = 1450;
		
		ArrayList<Cash> dispensedCash = CashUtil.getDenominationsToDispense(cash, withdrawalAmount);
		
		ArrayList<Cash> expectedDispensedCash = new ArrayList<>();
		expectedDispensedCash.add(new Cash(10, 50));
		expectedDispensedCash.add(new Cash(30, 20));
		expectedDispensedCash.add(new Cash(30, 10));
		expectedDispensedCash.add(new Cash(10, 5));
				
		assertEquals(dispensedCash.get(0).getDenomination(), expectedDispensedCash.get(0).getDenomination());
		assertEquals(dispensedCash.get(1).getDenomination(), expectedDispensedCash.get(1).getDenomination());
		assertEquals(dispensedCash.get(2).getDenomination(), expectedDispensedCash.get(2).getDenomination());
		assertEquals(dispensedCash.get(3).getDenomination(), expectedDispensedCash.get(3).getDenomination());

		assertEquals(dispensedCash.get(0).getQuantity(), expectedDispensedCash.get(0).getQuantity());
		assertEquals(dispensedCash.get(1).getQuantity(), expectedDispensedCash.get(1).getQuantity());
		assertEquals(dispensedCash.get(2).getQuantity(), expectedDispensedCash.get(2).getQuantity());
		assertEquals(dispensedCash.get(3).getQuantity(), expectedDispensedCash.get(3).getQuantity());
	
	}
	
	@Test
	void testWithdrawal4() throws Exception{
		cash =  new ArrayList<Cash>();
		cash.add(new Cash(10, 50));
		cash.add(new Cash(30, 20));
		cash.add(new Cash(30, 10));
		cash.add(new Cash(20, 5));//1500
		
		withdrawalAmount = 1505;
				
		Exception exception = assertThrows(Exception.class,
				() -> {CashUtil.getDenominationsToDispense(cash, withdrawalAmount);});
		
		String expectedMessage = "Withdraw amount greater than ATM funds";
		String actualMessage = exception.getMessage();
		
		assertTrue(actualMessage.contains(expectedMessage));
		
	}
	
	@Test
	void testWithdrawal5() throws Exception{
		cash =  new ArrayList<Cash>();
		cash.add(new Cash(10, 50));//500
		cash.add(new Cash(3, 5));//15
		
		withdrawalAmount = 20;
				
		Exception exception = assertThrows(Exception.class,
				() -> {CashUtil.getDenominationsToDispense(cash, withdrawalAmount);});
		
		String expectedMessage = "Unable to carry out transaction with denominations available, please try another amount to withdraw";
		String actualMessage = exception.getMessage();
		
		assertTrue(actualMessage.contains(expectedMessage));
		
	}
	
}
