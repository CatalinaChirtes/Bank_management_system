package ro.uvt.dp.main;

import ro.uvt.dp.main.client.Client;

import java.util.ArrayList;
import java.util.List;

public class BankingSystem implements BankingSystemMediator {
    private List<Client> clients;

    public BankingSystem() {
        this.clients = new ArrayList<>();
    }

    @Override
    public void addClient(Client client) {
        clients.add(client);
    }

    @Override
    public void removeClient(Client client) {
        clients.remove(client);
    }

    @Override
    public Client getClient(String name) {
        for (Client client : clients) {
            if (client.getName().equals(name)) {
                return client;
            }
        }
        return null;
    }

    @Override
    public List<Client> getClients() {
        return clients;
    }
}
