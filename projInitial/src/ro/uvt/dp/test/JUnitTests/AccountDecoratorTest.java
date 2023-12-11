package ro.uvt.dp.test.JUnitTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ro.uvt.dp.exceptions.AmountException;
import ro.uvt.dp.exceptions.BlockedAccountException;
import ro.uvt.dp.main.account.Account;
import ro.uvt.dp.main.account.BankCommandInvoker;
import ro.uvt.dp.main.account.commanders.DepositCommand;
import ro.uvt.dp.main.account.commanders.RetrieveCommand;
import ro.uvt.dp.main.account.commanders.TransferCommand;
import ro.uvt.dp.main.account.decorated.ChildrenAccountDecorator;
import ro.uvt.dp.main.account.decorated.SavingsAccountDecorator;
import ro.uvt.dp.main.account.interfaces.AccountFactory;
import ro.uvt.dp.main.account.interfaces.BankCommand;
import ro.uvt.dp.main.account.types.AccountEUR;
import ro.uvt.dp.main.account.types.AccountRON;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountDecoratorTest {
    private Account accountEur;
    private ChildrenAccountDecorator childrenEurAcc;
    private Account accountRon1;
    private ChildrenAccountDecorator childrenRonAcc;
    private Account accountRon2;
    private SavingsAccountDecorator savingsAcc;
    private AccountFactory accountFactory;
    private AccountFactory accountFactoryRon;


    @BeforeEach
    void setUp() throws AmountException, BlockedAccountException {
        accountFactory = new AccountFactory.AccountEURFactory();
        accountFactoryRon = new AccountFactory.AccountRONFactory();

        accountEur = accountFactory.createAccount("EUR123", 500.0);
        childrenEurAcc = new ChildrenAccountDecorator(accountEur);
        accountRon1 = accountFactoryRon.createAccount("RON123", 1000.0);
        childrenRonAcc = new ChildrenAccountDecorator(accountRon1);

        accountRon2 = accountFactoryRon.createAccount("RON124", 1000.0);
        savingsAcc = new SavingsAccountDecorator(accountRon2);
    }

    @Test
    void grantAllowanceEur() throws AmountException, BlockedAccountException {
        // initial amount without interest
        assertEquals(500.0, childrenEurAcc.getAmount());
        // initial amount with interest
        assertEquals(505.0, childrenEurAcc.getTotalAmount());
        // grant allowance
        childrenEurAcc.grantAllowance();
        // check amount after allowance
        assertEquals(520.0, childrenEurAcc.getAmount());
        assertEquals(525.2, childrenEurAcc.getTotalAmount());
    }

    @Test
    void grantAllowanceRon() throws AmountException, BlockedAccountException {
        // initial amount without interest
        assertEquals(1000.0, childrenRonAcc.getAmount());
        // initial amount with interest
        assertEquals(1080.0, childrenRonAcc.getTotalAmount());
        // grant allowance
        childrenRonAcc.grantAllowance();
        // check amount after allowance
        assertEquals(1100.0, childrenRonAcc.getAmount());
        assertEquals(1188.0, childrenRonAcc.getTotalAmount());
    }

    @Test
    void addInterest() throws AmountException, BlockedAccountException {
        // initial amount without interest
        assertEquals(1000.0, savingsAcc.getAmount());
        // initial amount with interest
        assertEquals(1080.0, savingsAcc.getTotalAmount());
        // grant interest
        savingsAcc.addInterest();
        // check amount after additional interest
        assertEquals(1050.0, savingsAcc.getAmount());
        assertEquals(1050.0 + 1050.0 * savingsAcc.getInterest(), savingsAcc.getTotalAmount());
    }

    @Test
    void retrievePositiveChildRon() throws AmountException, BlockedAccountException {
        // attempting to retrieve a positive RON amount
        BankCommand retrieveCommand = new RetrieveCommand(childrenRonAcc, 300.0);
        BankCommandInvoker invoker = new BankCommandInvoker();
        invoker.executeCommand(retrieveCommand);
        assertEquals(700.0, childrenRonAcc.getAmount());
    }

    @Test
    void retrievePositiveChildEur() throws AmountException, BlockedAccountException {
        // attempting to retrieve a positive EUR amount
        BankCommand retrieveCommand = new RetrieveCommand(childrenEurAcc, 100.0);
        BankCommandInvoker invoker = new BankCommandInvoker();
        invoker.executeCommand(retrieveCommand);
        assertEquals(400.0, childrenEurAcc.getAmount());
    }

    @Test
    void retrievePositiveSavings() throws AmountException, BlockedAccountException {
        // attempting to retrieve a positive RON amount
        BankCommand retrieveCommand = new RetrieveCommand(savingsAcc, 200.0);
        BankCommandInvoker invoker = new BankCommandInvoker();
        invoker.executeCommand(retrieveCommand);
        assertEquals(800.0, savingsAcc.getAmount());
    }

    @Test
    void deposePositiveChildRon() throws AmountException, BlockedAccountException {
        // attempting to deposit a positive RON amount
        BankCommand depositCommand = new DepositCommand(childrenRonAcc, 50.0);
        BankCommandInvoker invoker = new BankCommandInvoker();
        invoker.executeCommand(depositCommand);
        assertEquals(1050.0, childrenRonAcc.getAmount());
    }

    @Test
    void deposePositiveChildEur() throws AmountException, BlockedAccountException {
        // attempting to deposit a positive EUR amount
        BankCommand depositCommand = new DepositCommand(childrenEurAcc, 10.0);
        BankCommandInvoker invoker = new BankCommandInvoker();
        invoker.executeCommand(depositCommand);
        assertEquals(510.0, childrenEurAcc.getAmount());
    }

    @Test
    void deposePositiveSavings() throws AmountException, BlockedAccountException {
        // attempting to deposit a positive RON amount
        BankCommand depositCommand = new DepositCommand(savingsAcc, 500.0);
        BankCommandInvoker invoker = new BankCommandInvoker();
        invoker.executeCommand(depositCommand);
        assertEquals(1500.0, savingsAcc.getAmount());
    }

    @Test
    void transferPositiveChild() throws AmountException, BlockedAccountException {
        // creating a new RON account
        AccountRON newAccount = new AccountRON("RON125", 10.0);
        // transferring money from account to the new account
        BankCommand transferCommand = new TransferCommand(newAccount, childrenRonAcc, 100.0);
        BankCommandInvoker invoker = new BankCommandInvoker();
        invoker.executeCommand(transferCommand);

        // checking the new amount in the accounts
        assertEquals(900.0, childrenRonAcc.getAmount());
        assertEquals(110.0 , newAccount.getAmount());

        // transferring money from new account to the account
        BankCommand transferCommand2 = new TransferCommand(childrenRonAcc, newAccount, 10.0);
        invoker.executeCommand(transferCommand2);

        // checking the new amount in the accounts
        assertEquals(910.0, childrenRonAcc.getAmount());
        assertEquals(100.0 , newAccount.getAmount());
    }

    @Test
    void transferPositiveSavings() throws AmountException, BlockedAccountException {
        // creating a new RON account
        AccountRON newAccount = new AccountRON("RON125", 10.0);
        // transferring money from account to the new account
        BankCommand transferCommand = new TransferCommand(newAccount, savingsAcc, 100.0);
        BankCommandInvoker invoker = new BankCommandInvoker();
        invoker.executeCommand(transferCommand);

        // checking the new amount in the accounts
        assertEquals(900.0, savingsAcc.getAmount());
        assertEquals(110.0 , newAccount.getAmount());

        // transferring money from new account to the account
        BankCommand transferCommand2 = new TransferCommand(savingsAcc, newAccount, 10.0);
        invoker.executeCommand(transferCommand2);

        // checking the new amount in the accounts
        assertEquals(910.0, savingsAcc.getAmount());
        assertEquals(100.0 , newAccount.getAmount());
    }
}