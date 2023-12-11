package ro.uvt.dp.main.account.interfaces;

import ro.uvt.dp.exceptions.AmountException;
import ro.uvt.dp.exceptions.BlockedAccountException;
import ro.uvt.dp.main.account.Account;
import ro.uvt.dp.main.account.types.AccountEUR;
import ro.uvt.dp.main.account.types.AccountRON;

public interface AccountFactory {
    Account createAccount(String accountNumber, double sum) throws AmountException, BlockedAccountException;

    public class AccountEURFactory implements AccountFactory {
        @Override
        public Account createAccount(String accountNumber, double sum) throws AmountException, BlockedAccountException {
            return new AccountEUR(accountNumber, sum);
        }
    }

    public class AccountRONFactory implements AccountFactory {
        @Override
        public Account createAccount(String accountNumber, double sum) throws AmountException, BlockedAccountException {
            return new AccountRON(accountNumber, sum);
        }
    }
}
