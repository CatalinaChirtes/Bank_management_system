package ro.uvt.dp.main.account;

import ro.uvt.dp.exceptions.AmountException;
import ro.uvt.dp.exceptions.BlockedAccountException;
import ro.uvt.dp.main.account.interfaces.BankCommand;

public class BankCommandInvoker {
    public void executeCommand(BankCommand command) throws AmountException, BlockedAccountException{
        try {
            command.execute();
        } catch (AmountException | BlockedAccountException e) {
            throw e;
        }
    }
}
