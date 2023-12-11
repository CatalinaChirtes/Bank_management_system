package ro.uvt.dp.main.account.decorated;

import ro.uvt.dp.exceptions.AmountException;
import ro.uvt.dp.exceptions.BlockedAccountException;
import ro.uvt.dp.main.account.Account;
import ro.uvt.dp.main.account.AccountDecorator;

public class ChildrenAccountDecorator extends AccountDecorator {
    private Account decoratedAccount;
    public ChildrenAccountDecorator(Account decoratedAccount) throws AmountException, BlockedAccountException {
        super(decoratedAccount);
        this.decoratedAccount = decoratedAccount;
    }

    public void grantAllowance() throws AmountException, BlockedAccountException {
        double allowanceRON = 100.0;
        double allowanceEUR = 20.0;
        if (decoratedAccount.getAccountType().equals(Account.TYPE.RON.toString())) {
            this.depose(allowanceRON);
        } else if (decoratedAccount.getAccountType().equals(Account.TYPE.EUR.toString())) {
            this.depose(allowanceEUR);
        }
    }

    @Override
    public void decorateAccount(){
        super.decorateAccount();
        System.out.print(" Adding Children's Account feature.");
    }

    @Override
    public double getInterest() {
        return decoratedAccount.getInterest();
    }
}