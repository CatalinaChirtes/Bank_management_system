package ro.uvt.dp.main.account.interfaces;

import ro.uvt.dp.exceptions.AmountException;
import ro.uvt.dp.exceptions.BlockedAccountException;
import ro.uvt.dp.main.account.Account;

public interface Transfer {
	public void transfer(Account c, double s) throws AmountException, BlockedAccountException;
}
