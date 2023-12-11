package ro.uvt.dp.test.JUnitTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ro.uvt.dp.exceptions.AccountNotFoundException;
import ro.uvt.dp.exceptions.AmountException;
import ro.uvt.dp.exceptions.BlockedAccountException;
import ro.uvt.dp.exceptions.ClientNotFoundException;
import ro.uvt.dp.main.BankingSystem;
import ro.uvt.dp.main.BankingSystemMediator;
import ro.uvt.dp.main.account.Account;
import ro.uvt.dp.main.bank.Bank;
import ro.uvt.dp.main.client.Client;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

class BankTest {
    private Bank bank;
    private Client client1;
    private Client client2;
    BankingSystemMediator mediator = new BankingSystem();

    @BeforeEach
    void setUp() throws AmountException, BlockedAccountException, AccountNotFoundException {
        // creating a new bank
        bank = new Bank("ING Bank", mediator);

        // creating clients and adding them to the bank
        // creating a new EUR account
        client1 = new Client.ClientBuilder("George Georgescu", "Brasov", Account.TYPE.EUR, "EUR123", 700.0)
                .setGender("male")
                .createClient(mediator);
        // creating a new RON account
        client2 = new Client.ClientBuilder("Maria Popescu", "Oradea", Account.TYPE.RON, "RON123", 150.0)
                .setGender("female")
                .createClient(mediator);
        bank.addClient(client1);
        bank.addClient(client2);
    }

    @Test
    void addClient() throws AmountException, BlockedAccountException, AccountNotFoundException {
        Client client3 = new Client.ClientBuilder("Ion Ionescu", "Iasi", Account.TYPE.EUR, "EUR124", 200.0)
                .setGender("male")
                .createClient(mediator);
        mediator.addClient(client3);
        // checking the number of clients the bank has
        assertEquals(3, mediator.getClients().size());
    }

    @Test
    void getExistingClient() throws ClientNotFoundException {
        // trying to get an existing client
        assertNotNull(mediator.getClient("Maria Popescu"));
    }

    @Test
    void getNonExistingClient() throws ClientNotFoundException {
        // trying to get a non-existing client
        assertNull(mediator.getClient("Julia Marinescu"));
    }

    @Test
    void testToString() {
        assertEquals("Bank [code=ING Bank, clients=[" +
                "\n\tClient [name=George Georgescu, address=Brasov, gender=male, accounts=[Account EUR [Account: code=EUR123, amount=700.0]]], \n" +
                "\tClient [name=Maria Popescu, address=Oradea, gender=female, accounts=[Account RON [Account: code=RON123, amount=150.0]]]]]", bank.toString());
    }

    @Test
    void testRemoveAccount() throws ClientNotFoundException {
        // checking the number of clients from a bank
        assertEquals(2, mediator.getClients().size());
        // removing the client
        mediator.removeClient(mediator.getClient("Maria Popescu"));
        // checking the number of clients from a bank to see it got reduced
        assertEquals(1, bank.getClients().size());
        // ensuring that the client has been removed
        assertNull(mediator.getClient("Maria Popescu"));
        try {
            mediator.removeClient(mediator.getClient("Maria Popescu"));
        }
        catch (ClientNotFoundException e) {
            assertEquals("Client not found", e.getMessage());
        }
    }
}