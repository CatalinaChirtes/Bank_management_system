package ro.uvt.dp.main.bank;

import ro.uvt.dp.exceptions.ClientNotFoundException;
import ro.uvt.dp.main.BankingSystemMediator;
import ro.uvt.dp.main.client.Client;

import java.util.ArrayList;
import java.util.List;

public class Bank {
	private String bankCode;
	private BankingSystemMediator mediator;

	public Bank(String bankCode, BankingSystemMediator mediator) {
		this.bankCode = bankCode;
		this.mediator = mediator;
	}

	public void addClient(Client c) {
		mediator.addClient(c);
	}


	public Client getClient(String name) throws ClientNotFoundException {
		return mediator.getClient(name);
	}


	@Override
	public String toString() {
		return "Bank [code=" + bankCode + ", clients=" + mediator.getClients() + "]";
	}


	public List<Client> getClients() {
		return mediator.getClients();
	}


	// added a new functionality to remove a client
	public void removeClient(String name) throws ClientNotFoundException {
		Client clientToRemove = mediator.getClient(name);
		if (clientToRemove != null) {
			mediator.removeClient(clientToRemove);
		} else {
			throw new ClientNotFoundException("Client not found");
		}
	}
}
