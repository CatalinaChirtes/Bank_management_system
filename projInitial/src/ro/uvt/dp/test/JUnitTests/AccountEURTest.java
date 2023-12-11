package ro.uvt.dp.test.JUnitTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ro.uvt.dp.exceptions.AmountException;
import ro.uvt.dp.exceptions.BlockedAccountException;
import ro.uvt.dp.main.account.BankCommandInvoker;
import ro.uvt.dp.main.account.commanders.TransferCommand;
import ro.uvt.dp.main.account.interfaces.BankCommand;
import ro.uvt.dp.main.account.types.AccountEUR;

import static org.junit.jupiter.api.Assertions.*;

class AccountEURTest {
    private AccountEUR account;

    @BeforeEach
    void setUp() throws AmountException, BlockedAccountException {
        // creating a new EUR account
        account = new AccountEUR("EUR123", 100.0);
    }

    @Test
    void createValidEurAccount() throws AmountException, BlockedAccountException {
        AccountEUR accountEur = new AccountEUR("EUR124",150.0);
        assertEquals("EUR124", accountEur.getAccountNumber());
    }

    @Test
    void createNegativeEurAccount() throws BlockedAccountException {
        try {
            new AccountEUR("EUR124",-100);
        } catch (AmountException e) {
            assertEquals("Amount must be positive", e.getMessage());
        }
    }

    @Test
    void createZeroEurAccount() throws BlockedAccountException{
        try {
            new AccountEUR("EUR124",0);
        } catch (AmountException e) {
            assertEquals("Amount must be more than 0", e.getMessage());
        }
    }

    @Test
    void getInterest() {
        assertEquals(0.01, account.getInterest());
    }

    @Test
    void testToString() {
        assertEquals("Account EUR [Account: code=EUR123, amount=100.0]", account.toString());
    }

    @Test
    void transferPositive() throws AmountException, BlockedAccountException {
        // creating a new RON account
        AccountEUR newAccount = new AccountEUR("EUR124", 10.0);
        // transferring money from account to the new account
        BankCommand transferCommand = new TransferCommand(newAccount, account, 50.0);
        BankCommandInvoker invoker = new BankCommandInvoker();
        invoker.executeCommand(transferCommand);
        // checking the new amount in the accounts
        assertEquals(50.5, account.getTotalAmount());
        assertEquals(60.6 , newAccount.getTotalAmount());
    }

    @Test
    void transferNegative() throws AmountException, BlockedAccountException {
        // creating a new RON account
        AccountEUR newAccount = new AccountEUR("EUR124", 10.0);
        // transferring a negative amount of money from account to the new account
        BankCommand transferCommand = new TransferCommand(newAccount, account, -100.0);
        BankCommandInvoker invoker = new BankCommandInvoker();
        try {
            invoker.executeCommand(transferCommand);
        } catch (AmountException e) {
            assertEquals("Amount must be positive", e.getMessage());
        }
    }
}