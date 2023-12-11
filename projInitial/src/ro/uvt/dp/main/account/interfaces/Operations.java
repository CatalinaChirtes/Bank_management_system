package ro.uvt.dp.main.account.interfaces;

import ro.uvt.dp.exceptions.AmountException;
import ro.uvt.dp.exceptions.BlockedAccountException;

public interface Operations extends Transfer{
	public double getTotalAmount();

	public double getAmount();

	public double getInterest();

	public void depose(double amount) throws AmountException, BlockedAccountException;

	public void retrieve(double amount) throws AmountException, BlockedAccountException;

}