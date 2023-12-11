package ro.uvt.dp.main;

import ro.uvt.dp.exceptions.ClientNotFoundException;
import ro.uvt.dp.main.client.Client;

import java.util.List;

public interface BankingSystemMediator {
    public void addClient(Client client);
    public void removeClient(Client client);
    public Client getClient(String name) throws ClientNotFoundException;
    public List<Client> getClients();
}
