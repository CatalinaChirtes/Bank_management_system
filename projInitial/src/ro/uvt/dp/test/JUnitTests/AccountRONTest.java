package ro.uvt.dp.test.JUnitTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ro.uvt.dp.exceptions.AmountException;
import ro.uvt.dp.exceptions.BlockedAccountException;
import ro.uvt.dp.main.account.BankCommandInvoker;
import ro.uvt.dp.main.account.commanders.DepositCommand;
import ro.uvt.dp.main.account.commanders.RetrieveCommand;
import ro.uvt.dp.main.account.commanders.TransferCommand;
import ro.uvt.dp.main.account.interfaces.BankCommand;
import ro.uvt.dp.main.account.types.AccountRON;

import static org.junit.jupiter.api.Assertions.*;

class AccountRONTest {
    private AccountRON account;

    @BeforeEach
    void setUp() throws AmountException, BlockedAccountException {
        // creating a new RON account
        account = new AccountRON("RON123", 400.0);
    }

    @Test
    void createValidRonAccount() throws AmountException, BlockedAccountException {
        AccountRON accountRon = new AccountRON("RON124",150.8);
        assertEquals("RON124", accountRon.getAccountNumber());
    }

    @Test
    void createNegativeRonAccount() throws BlockedAccountException {
        try {
            new AccountRON("RON124",-500);
        } catch (AmountException e) {
            assertEquals("Amount must be positive", e.getMessage());
        }
    }

    @Test
    void createZeroRonAccount() throws BlockedAccountException {
        try {
            new AccountRON("RON124",0);
        } catch (AmountException e) {
            assertEquals("Amount must be more than 0", e.getMessage());
        }
    }

    @Test
    void getInterest() throws AmountException, BlockedAccountException {
        // ensuring that the interest rate is correct for a small balances (less than 500 RON)
        assertEquals(0.03, account.getInterest());
        // increasing the balance over 500 RON and checking the interest rate for larger balances
        BankCommand depositCommand = new DepositCommand(account, 500.0);
        BankCommandInvoker invoker = new BankCommandInvoker();
        invoker.executeCommand(depositCommand);
        assertEquals(0.08, account.getInterest());
    }

    @Test
    void testToString() {
        assertEquals("Account RON [Account: code=RON123, amount=400.0]", account.toString());
    }

    @Test
    void transferPositive() throws AmountException, BlockedAccountException {
        // creating a new RON account
        AccountRON newAccount = new AccountRON("RON124", 10.0);
        // transferring money from account to the new account
        BankCommand transferCommand = new TransferCommand(newAccount, account, 120.0);
        BankCommandInvoker invoker = new BankCommandInvoker();
        invoker.executeCommand(transferCommand);
        // checking the new amount in the accounts
        assertEquals(288.4, account.getTotalAmount());
        assertEquals(133.9 , newAccount.getTotalAmount());
    }

    @Test
    void transferNegative() throws AmountException, BlockedAccountException {
        // creating a new RON account
        AccountRON newAccount = new AccountRON("RON124", 10.0);
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