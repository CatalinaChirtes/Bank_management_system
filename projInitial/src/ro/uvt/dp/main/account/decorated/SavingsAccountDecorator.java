package ro.uvt.dp.main.account.decorated;

import ro.uvt.dp.exceptions.AmountException;
import ro.uvt.dp.exceptions.BlockedAccountException;
import ro.uvt.dp.main.account.Account;
import ro.uvt.dp.main.account.AccountDecorator;

public class SavingsAccountDecorator extends AccountDecorator {
    private Account decoratedAccount;
    public SavingsAccountDecorator(Account decoratedAccount) throws AmountException, BlockedAccountException {
        super(decoratedAccount);
        this.decoratedAccount = decoratedAccount;
    }

    public void addInterest() throws AmountException, BlockedAccountException {
        double interest = 0.05; // 5% interest rate
        double currentAmount = decoratedAccount.getAmount();
        double interestAmount = currentAmount * interest;
        this.depose(interestAmount);
    }

    @Override
    public void decorateAccount(){
        super.decorateAccount();
        System.out.print(" Adding Savings Account feature.");
    }

    @Override
    public double getInterest() {
        return decoratedAccount.getInterest();
    }
}