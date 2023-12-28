package ro.uvt.dp.main.client;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import ro.uvt.dp.DatabaseConnection;
import ro.uvt.dp.exceptions.AccountNotFoundException;
import ro.uvt.dp.exceptions.AmountException;
import ro.uvt.dp.exceptions.BlockedAccountException;
import ro.uvt.dp.main.BankingSystemMediator;
import ro.uvt.dp.main.Logger;
import ro.uvt.dp.main.account.Account;
import ro.uvt.dp.main.account.Account.TYPE;
import ro.uvt.dp.main.account.interfaces.AccountFactory;

public class Client {

	private String name;
	private String address;
	private String gender;
	private List<Account> accounts;
	private Logger logger;
	private BankingSystemMediator mediator;


	private Client(String name, String address, TYPE type, String accountNumber, double sum, String gender, BankingSystemMediator mediator) throws AmountException, BlockedAccountException, AccountNotFoundException {
		this.name = name;
		this.address = address;
		this.gender = gender;
		accounts = new LinkedList<>();
		logger = Logger.getInstance("logger.txt");
		addAccount(type, accountNumber, sum);
		logger.log("Client " + name + " was created");
		this.mediator = mediator;
	}

	public static class ClientBuilder{
		private String name;
		private String address;
		private TYPE type;
		private String accountNumber;
		private double sum;
		private String gender;

		public ClientBuilder(String name, String address, TYPE type, String accountNumber, double sum) {
			this.name = name;
			this.address = address;
			this.type = type;
			this.accountNumber = accountNumber;
			this.sum = sum;
		}

		public ClientBuilder setGender(String gender){
			this.gender = gender;
			return this;
		}

		public Client createClient(BankingSystemMediator mediator) throws IllegalArgumentException, AmountException, BlockedAccountException, AccountNotFoundException {
			if (name == null) {
				throw new IllegalArgumentException("Name is a mandatory field.");
			} else if (address == null) {
				throw new IllegalArgumentException("Address is a mandatory field.");
			} else if (type == null) {
				throw new IllegalArgumentException("Account type is a mandatory field.");
			} else if (accountNumber == null) {
				throw new IllegalArgumentException("Account number is a mandatory field.");
			} else if (sum == 0) {
				throw new IllegalArgumentException("Sum is a mandatory field.");
			}
			return new Client(name, address, type, accountNumber, sum, gender, mediator);
		}
	}

	private AccountFactory getAccountFactory(TYPE type) throws AccountNotFoundException {
		if (type == Account.TYPE.EUR) {
			return new AccountFactory.AccountEURFactory();
		} else if (type == Account.TYPE.RON) {
			return new AccountFactory.AccountRONFactory();
		} else {
			throw new AccountNotFoundException("Account type " + type + " not found");
		}
	}

	public void addAccount(TYPE type, String accountNumber, double sum) throws AmountException, BlockedAccountException, AccountNotFoundException {
		AccountFactory accountFactory = getAccountFactory(type);
		if (accountFactory != null) {
			Account account = accountFactory.createAccount(accountNumber, sum);
			accounts.add(account);
			logger.log("Account number: " + accountNumber);
		} else {
			System.out.println("Unsupported account type");
		}
	}

	public Account getAccount(String accountNumber) {
		for (Account account : accounts) {
			if (account.getAccountNumber().equals(accountNumber)) {
				return account;
			}
		}
		return null;
	}

	// added a new functionality to close an account
	public void closeAccount(String accountNumber) throws AccountNotFoundException {
		Account accountToRemove = null;
		for (Account account : accounts) {
			if (account.getAccountNumber().equals(accountNumber)) {
				accountToRemove = account;
				break;
			}
		}
		if (accountToRemove != null) {
			accounts.remove(accountToRemove);
		}
		else
			throw new AccountNotFoundException("Account not found");
	}


	public List<Account> getAccounts(){
		return accounts;
	}


	@Override
	public String toString() {
		return "\n\tClient [name=" + name + ", address=" + address + ", gender=" + gender + ", accounts=" + accounts + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void saveToDatabase(String bankName) {
		try (Connection connection = DatabaseConnection.getConnection()) {
			int clientId = -1;
			int bankId = -1;

			// Check if the client already exists in the database
			boolean clientExists = clientExistsInDatabase(name, connection);
			if (clientExists) {
				System.out.println("Client already exists in the database.");

				// Retrieve client ID from the database
				clientId = getClientIdFromDatabase(name, connection);
			} else {
				// If the client doesn't exist, insert into the database
				String query = "INSERT INTO clients (name, address, gender) VALUES (?, ?, ?)";

				try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
					preparedStatement.setString(1, name);
					preparedStatement.setString(2, address);
					preparedStatement.setString(3, gender);
					preparedStatement.executeUpdate();

					try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
						if (generatedKeys.next()) {
							clientId = generatedKeys.getInt(1);
						}
					}
				}
			}

			// Get bank ID by name
			bankId = getBankIdFromDatabase(bankName, connection);

			// Save accounts to the database if necessary
			for (Account account : accounts) {
				account.saveToDatabaseAccounts(clientId, bankId);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error saving client to the database");
		}
	}

	private boolean clientExistsInDatabase(String name, Connection connection) throws SQLException {
		String query = "SELECT * FROM clients WHERE name = ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, name);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				return resultSet.next();
			}
		}
	}

	private int getClientIdFromDatabase(String name, Connection connection) throws SQLException {
		String query = "SELECT id FROM clients WHERE name = ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, name);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getInt("id");
				}
			}
		}
		throw new RuntimeException("Client ID not found in the database for name: " + name);
	}

	private int getBankIdFromDatabase(String name, Connection connection) throws SQLException {
		String query = "SELECT id FROM banks WHERE bank_code = ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, name);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getInt("id");
				}
			}
		}
		throw new RuntimeException("Bank ID not found in the database for name: " + name);
	}
}
