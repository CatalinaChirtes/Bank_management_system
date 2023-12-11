package ro.uvt.dp.main.account.commanders;

import ro.uvt.dp.exceptions.AmountException;
import ro.uvt.dp.exceptions.BlockedAccountException;
import ro.uvt.dp.main.account.Account;
import ro.uvt.dp.main.account.interfaces.BankCommand;

public class DepositCommand implements BankCommand {
    private Account account;
    private double amount;

    public DepositCommand(Account account, double amount) {
        this.account = account;
        this.amount = amount;
    }

    @Override
    public void execute() throws AmountException, BlockedAccountException {
        account.depose(amount);
    }
}
