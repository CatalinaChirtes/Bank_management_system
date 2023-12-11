package ro.uvt.dp.main.account.types;

import ro.uvt.dp.exceptions.AmountException;
import ro.uvt.dp.exceptions.BlockedAccountException;
import ro.uvt.dp.main.account.Account;
import ro.uvt.dp.main.account.interfaces.Transfer;

public class AccountRON extends Account implements Transfer {

	public AccountRON(String accountNumber, double sum) throws AmountException, BlockedAccountException {
		super(accountNumber, sum);
	}

	public double smallRonInterest = 0.03;
	public double largeRonInterest = 0.08;

	public double getInterest() {
		if (amount < 500)
			return smallRonInterest;
		else
			return largeRonInterest;

	}

	@Override
	public String toString() {
		return "Account RON [" + super.toString() + "]";
	}

	@Override
	public void transfer(Account c, double s) throws AmountException, BlockedAccountException {
		c.retrieve(s);
		depose(s);
	}
}
