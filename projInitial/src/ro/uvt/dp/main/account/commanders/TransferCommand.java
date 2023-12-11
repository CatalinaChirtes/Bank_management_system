package ro.uvt.dp.main.account.commanders;

import ro.uvt.dp.exceptions.AmountException;
import ro.uvt.dp.exceptions.BlockedAccountException;
import ro.uvt.dp.main.account.Account;
import ro.uvt.dp.main.account.interfaces.BankCommand;

public class TransferCommand implements BankCommand {
    private Account sourceAccount;
    private Account destinationAccount;
    private double amount;

    public TransferCommand(Account sourceAccount, Account destinationAccount, double amount) {
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
    }

    @Override
    public void execute() throws AmountException, BlockedAccountException {
        sourceAccount.transfer(destinationAccount, amount);
    }
}
