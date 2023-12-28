package ro.uvt.dp.main.bank;

import ro.uvt.dp.DatabaseConnection;
import ro.uvt.dp.exceptions.ClientNotFoundException;
import ro.uvt.dp.main.BankingSystemMediator;
import ro.uvt.dp.main.client.Client;

import java.sql.*;
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

	public void saveToDatabase() {
		try (Connection connection = DatabaseConnection.getConnection()) {
			// Check if the bank already exists in the database
			if (bankExistsInDatabase(bankCode, connection)) {
				System.out.println("Bank already exists in the database.");
				return;
			}

			// If the bank doesn't exist, insert into the database
			String query = "INSERT INTO banks (bank_code) VALUES (?)";
			try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
				preparedStatement.setString(1, this.bankCode);
				preparedStatement.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error saving bank to the database");
		}
	}

	private boolean bankExistsInDatabase(String bankCode, Connection connection) throws SQLException {
		String query = "SELECT * FROM banks WHERE bank_code = ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, bankCode);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				return resultSet.next();
			}
		}
	}
}
