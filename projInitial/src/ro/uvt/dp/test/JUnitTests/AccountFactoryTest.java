package ro.uvt.dp.test.JUnitTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ro.uvt.dp.exceptions.AmountException;
import ro.uvt.dp.exceptions.BlockedAccountException;
import ro.uvt.dp.main.account.Account;
import ro.uvt.dp.main.account.interfaces.AccountFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AccountFactoryTest {
    private AccountFactory accountFactory;

    @BeforeEach
    void setUp() {
        accountFactory = new AccountFactory.AccountEURFactory();
    }

    @Test
    void createEURAccount() throws AmountException, BlockedAccountException {
        Account account = accountFactory.createAccount("EUR123", 500.0);
        assertNotNull(account);
        assertEquals("EUR", account.getAccountType());
        assertEquals(500.0, account.getAmount());
        assertEquals("EUR123", account.getAccountNumber());
    }

    @Test
    void createRONAccount() throws AmountException, BlockedAccountException {
        accountFactory = new AccountFactory.AccountRONFactory();
        Account account = accountFactory.createAccount("RON456", 1000.0);
        assertNotNull(account);
        assertEquals("RON", account.getAccountType());
        assertEquals(1000.0, account.getAmount());
        assertEquals("RON456", account.getAccountNumber());
    }

    @Test
    void createInvalidAccount() {
        accountFactory = new AccountFactory() {
            @Override
            public Account createAccount(String accountNumber, double sum) throws AmountException {
                // simulating an invalid account creation
                throw new AmountException("Invalid account creation");
            }
        };

        try {
            accountFactory.createAccount("INVALID123", 0.0);
        } catch (AmountException | BlockedAccountException e) {
            assertEquals("Invalid account creation", e.getMessage());
        }
    }
}