package ro.uvt.dp.main.account;

import ro.uvt.dp.exceptions.AmountException;
import ro.uvt.dp.exceptions.BlockedAccountException;
import ro.uvt.dp.main.account.interfaces.DecoratedAccount;
import ro.uvt.dp.main.account.interfaces.Operations;
import ro.uvt.dp.main.account.types.AccountEUR;
import ro.uvt.dp.main.account.types.AccountRON;

public abstract class Account implements Operations, DecoratedAccount {

	protected String accountNumber = null;
	protected boolean blocked = false;
	protected double amount = 0;

	public enum TYPE {
		EUR, RON
	};

	protected Account(String accountNumber, double sum) throws AmountException, BlockedAccountException {
		this.accountNumber = accountNumber;
		depose(sum);
	}

	public double getTotalAmount() {
		return amount + amount * getInterest();
	}

	public double getAmount() {
		return amount;
	}

	public String getAccountType() {
		if (this instanceof AccountEUR) {
			return TYPE.EUR.toString();
		} else if (this instanceof AccountRON) {
			return TYPE.RON.toString();
		} else {
			return null;
		}
	}

	@Override
	public void depose(double sum) throws AmountException, BlockedAccountException {
		if (isBlocked()) {
			throw new BlockedAccountException("The account is blocked");
		}
		if (sum < 0) {
			throw new AmountException("Amount must be positive");
		}
		if (sum == 0) {
			throw new AmountException("Amount must be more than 0");
		}

		this.amount += sum;
	}

	@Override
	public void retrieve(double sum) throws AmountException, BlockedAccountException {
		if (isBlocked()) {
			throw new BlockedAccountException("The account is blocked");
		}
		if (sum < 0) {
			throw new AmountException("Amount must be positive");
		}
		if (sum > amount){
			throw new AmountException("Insufficient funds to retrieve" + sum);
		}
		if (sum == amount){
			throw new AmountException("Amount must be more than 0");
		}

		this.amount -= sum;
	}

	// added a new functionality to block an account
	public void blockAccount(){
		blocked = true;
	}

	public void unblockAccount(){
		blocked = false;
	}

	public boolean isBlocked(){
		return blocked;
	}

	public String toString() {
		return "Account: code=" + accountNumber + ", amount=" + amount;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	@Override
	public void decorateAccount() {
		System.out.print("Basic Account.");
	}
}
