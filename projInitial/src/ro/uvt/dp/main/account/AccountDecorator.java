package ro.uvt.dp.main.account;

import ro.uvt.dp.exceptions.AmountException;
import ro.uvt.dp.exceptions.BlockedAccountException;
import ro.uvt.dp.main.account.Account;
import ro.uvt.dp.main.account.interfaces.DecoratedAccount;
import ro.uvt.dp.main.account.interfaces.Operations;

public abstract class AccountDecorator extends Account implements DecoratedAccount {
    protected Account decoratedAccount;

    public AccountDecorator(Account decoratedAccount) throws AmountException, BlockedAccountException {
        super(decoratedAccount.accountNumber, decoratedAccount.amount);
        this.decoratedAccount = decoratedAccount;
    }

    @Override
    public void transfer(Account c, double s) throws AmountException, BlockedAccountException{
        c.retrieve(s);
        depose(s);
    }

    @Override
    public void decorateAccount() {
        this.decoratedAccount.decorateAccount();
    }
}