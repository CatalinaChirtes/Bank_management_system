package ro.uvt.dp.test.JUnitTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import ro.uvt.dp.exceptions.AccountNotFoundException;
import ro.uvt.dp.exceptions.AmountException;
import ro.uvt.dp.exceptions.BlockedAccountException;
import ro.uvt.dp.main.BankingSystem;
import ro.uvt.dp.main.BankingSystemMediator;
import ro.uvt.dp.main.account.Account;

import ro.uvt.dp.main.client.Client;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

class ClientTest {
    private Client client1;
    private Client client2;
    BankingSystemMediator mediator = new BankingSystem();

    @BeforeEach
    void setUp() throws AmountException, BlockedAccountException, AccountNotFoundException {
        // creating a new RON account
        client1 = new Client.ClientBuilder("Ioana Popescu", "Timisoara", Account.TYPE.RON, "RON123", 200.0)
                .setGender("female")
                .createClient(mediator);
        // creating a new EUR account
        client2 = new Client.ClientBuilder("Rares Ionescu", "Sibiu", Account.TYPE.EUR, "EUR123", 150.0)
                .setGender("male")
                .createClient(mediator);
    }

    @Test
    void addNegativeValueAccountEUR() throws BlockedAccountException, AccountNotFoundException {
        // adding a new account to client1 who has a negative amount in the account, so an illegal argument exception is thrown
        try {
            client1.addAccount(Account.TYPE.EUR, "EUR124", -500.0);
        } catch (AmountException e) {
            assertEquals(1, client1.getAccounts().size());
            assertEquals("Amount must be positive", e.getMessage());
        }
    }

    @Test
    void addNegativeValueAccountRON() throws BlockedAccountException, AccountNotFoundException {
        // adding a new account to client1 who has a negative amount in the account, so an illegal argument exception is thrown
        try {
            client2.addAccount(Account.TYPE.RON, "RON124", -300.0);
        } catch (AmountException e) {
            assertEquals(1, client2.getAccounts().size());
            assertEquals("Amount must be positive", e.getMessage());
        }
    }

    @Test
    void getExistingAccount() {
        // trying to get an existing account
        assertNotNull(client1.getAccount("RON123"));
    }

    @Test
    void getNonExistingAccount() {
        // trying to get a non-existing account
        assertNull(client1.getAccount("RON456"));
    }

    @Test
    void testToString() {
        assertEquals("\n\tClient [name=Ioana Popescu, address=Timisoara, gender=female, accounts=[Account RON " +
                "[Account: code=RON123, amount=200.0]]]", client1.toString());
    }

    @Test
    void getName() {
        assertEquals("Ioana Popescu", client1.getName());
    }

    @Test
    void setName() {
        client1.setName("Ioana Papa");
        assertEquals("Ioana Papa", client1.getName());
    }

    @Test
    public void testCloseAccount() throws AccountNotFoundException {
        // checking the number of accounts of a client
        assertEquals(1, client2.getAccounts().size());
        // closing the account
        client2.closeAccount("EUR123");
        // checking the number of accounts of a client has reduced
        assertEquals(0, client2.getAccounts().size());
        // ensuring that the account has been closed
        assertNull(client2.getAccount("EUR123"));
        try {
            client2.closeAccount("EUR123");
        } catch (AccountNotFoundException e) {
            assertEquals("Account not found", e.getMessage());
        }
    }
}