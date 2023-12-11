package ro.uvt.dp.test.JUnitTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ro.uvt.dp.exceptions.AmountException;
import ro.uvt.dp.exceptions.BlockedAccountException;
import ro.uvt.dp.main.account.Account;
import ro.uvt.dp.main.account.BankCommandInvoker;
import ro.uvt.dp.main.account.commanders.DepositCommand;
import ro.uvt.dp.main.account.commanders.RetrieveCommand;
import ro.uvt.dp.main.account.interfaces.AccountFactory;
import ro.uvt.dp.main.account.interfaces.BankCommand;
import ro.uvt.dp.main.account.types.AccountEUR;
import ro.uvt.dp.main.account.types.AccountRON;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

class AccountTest {
    private Account account;
    private Account accountRon;
    private AccountFactory accountFactory;
    private AccountFactory accountFactoryRon;

    @BeforeEach
    void setUp() throws AmountException, BlockedAccountException  {
        // creating a new EUR and RON account
        accountFactory = new AccountFactory.AccountEURFactory();
        accountFactoryRon = new AccountFactory.AccountRONFactory();
        account = accountFactory.createAccount("EUR123", 1000.0);
        accountRon = accountFactoryRon.createAccount("RON123", 300.0);
    }

    @Test
    void getTotalEURAmount() {
        // getting the total amount for the EUR account
        assertEquals(1010.0, account.getTotalAmount(), account.getInterest());
    }

    @Test
    void getTotalRONLessAmount() {
        // getting the total amount for the RON account, with the interest rate for small amounts (less than 500 RON)
        assertEquals(309.0, accountRon.getTotalAmount(), account.getInterest());
    }

    @Test
    void getTotalRONMoreAmount() throws AmountException, BlockedAccountException {
        BankCommand depositCommand = new DepositCommand(accountRon, 300.0);
        BankCommandInvoker invoker = new BankCommandInvoker();
        invoker.executeCommand(depositCommand);
        // getting the total amount for the RON account, with the interest rate for greater amounts (more than 500 RON)
        assertEquals(648.0, accountRon.getTotalAmount(), account.getInterest());
    }

    @Test
    void deposePositive() throws AmountException, BlockedAccountException {
        // attempting to deposit a positive amount
        BankCommand depositCommand = new DepositCommand(account, 500.0);
        BankCommandInvoker invoker = new BankCommandInvoker();
        invoker.executeCommand(depositCommand);
        // check the total amount after depositing
        assertEquals(1515.0, account.getTotalAmount(), account.getInterest());
    }

    @Test
    void deposeNegative() throws BlockedAccountException {
        // attempting to deposit a negative amount
        BankCommand depositCommand = new DepositCommand(account, -500.0);
        BankCommandInvoker invoker = new BankCommandInvoker();
        try {
            invoker.executeCommand(depositCommand);
        } catch (AmountException e) {
            assertEquals("Amount must be positive", e.getMessage());
        }
    }

    @Test
    void deposeZero() throws BlockedAccountException {
        // attempting to deposit 0 as an amount
        BankCommand depositCommand = new DepositCommand(account, 0);
        BankCommandInvoker invoker = new BankCommandInvoker();
        try {
            invoker.executeCommand(depositCommand);
        } catch (AmountException e) {
            assertEquals("Amount must be more than 0", e.getMessage());
        }
    }

    @Test
    void retrievePositive() throws AmountException, BlockedAccountException {
        // attempting to retrieve a positive amount
        BankCommand retrieveCommand = new RetrieveCommand(account, 300.0);
        BankCommandInvoker invoker = new BankCommandInvoker();
        invoker.executeCommand(retrieveCommand);
        assertEquals(707.0, account.getTotalAmount(), account.getInterest());
    }

    @Test
    void retrieveMore() throws BlockedAccountException {
        // attempting to retrieve more funds than available
        double sum = 1700.0;
        BankCommand retrieveCommand = new RetrieveCommand(account, sum);
        BankCommandInvoker invoker = new BankCommandInvoker();
        try {
            invoker.executeCommand(retrieveCommand);
        } catch (AmountException e) {
            assertEquals("Insufficient funds to retrieve" + sum, e.getMessage());
        }
    }

    @Test
    void retrieveNegative() throws BlockedAccountException {
        // attempting to retrieve a negative amount
        BankCommand retrieveCommand = new RetrieveCommand(account, -300.0);
        BankCommandInvoker invoker = new BankCommandInvoker();
        try {
            invoker.executeCommand(retrieveCommand);
        } catch (AmountException e) {
            assertEquals("Amount must be positive", e.getMessage());
        }
    }

    @Test
    void retrieveZero() throws BlockedAccountException {
        // attempting to retrieve 0 as an amount
        BankCommand retrieveCommand = new RetrieveCommand(account, 0);
        BankCommandInvoker invoker = new BankCommandInvoker();
        try {
            invoker.executeCommand(retrieveCommand);
        } catch (AmountException e) {
            assertEquals("Amount must be more than 0", e.getMessage());
        }
    }

    @Test
    void testToString() {
        assertEquals("Account EUR [Account: code=EUR123, amount=1000.0]", account.toString());
    }

    @Test
    void getAccountNumber() {
        assertEquals("EUR123", account.getAccountNumber());
    }

    @Test
    public void testAccountBlocked() throws AmountException {
        // blocking the account
        account.blockAccount();
        assertTrue("The account is blocked", account.isBlocked());
        // no operations can be done from the account when blocked
        BankCommand depositCommand = new DepositCommand(account, 100.0);
        BankCommand retrieveCommand = new RetrieveCommand(account, 10.0);
        BankCommandInvoker invoker = new BankCommandInvoker();
        try {
            invoker.executeCommand(depositCommand);
            invoker.executeCommand(retrieveCommand);
        } catch (BlockedAccountException e) {
            assertEquals("The account is blocked", e.getMessage());
        }
    }

    @Test
    public void testAccountUnblocked() throws AmountException, BlockedAccountException {
        // blocking the account
        account.blockAccount();
        assertTrue("The account is blocked", account.isBlocked());
        // unblocking the account
        account.unblockAccount();
        assertTrue("The account is unblocked", !account.isBlocked());
        // checking if all operations can be done from the account when unblocked
        BankCommand depositCommand = new DepositCommand(account, 100.0);
        BankCommand retrieveCommand = new RetrieveCommand(account, 10.0);
        BankCommandInvoker invoker = new BankCommandInvoker();
        invoker.executeCommand(depositCommand);
        invoker.executeCommand(retrieveCommand);
    }
}