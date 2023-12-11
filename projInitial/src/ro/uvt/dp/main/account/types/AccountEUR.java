package ro.uvt.dp.main.account.types;

import ro.uvt.dp.exceptions.AmountException;
import ro.uvt.dp.exceptions.BlockedAccountException;
import ro.uvt.dp.main.account.Account;
import ro.uvt.dp.main.account.interfaces.Transfer;

public class AccountEUR extends Account implements Transfer {

	public AccountEUR(String accountNumber, double sum) throws AmountException, BlockedAccountException {
		super(accountNumber, sum);
	}

	public double eurInterest = 0.01;

	public double getInterest() {
		return eurInterest;

	}

	@Override
	public String toString() {
		return "Account EUR [" + super.toString() + "]";
	}

	@Override
	public void transfer(Account c, double s) throws AmountException, BlockedAccountException{
		c.retrieve(s);
		depose(s);
	}
}
