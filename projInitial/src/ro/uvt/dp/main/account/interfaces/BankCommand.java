package ro.uvt.dp.main.account.interfaces;

import ro.uvt.dp.exceptions.AmountException;
import ro.uvt.dp.exceptions.BlockedAccountException;

public interface BankCommand {
    public void execute() throws AmountException, BlockedAccountException;
}
