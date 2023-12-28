package ro.uvt.dp.main.account;

import ro.uvt.dp.DatabaseConnection;
import ro.uvt.dp.exceptions.AmountException;
import ro.uvt.dp.exceptions.BlockedAccountException;
import ro.uvt.dp.main.account.interfaces.DecoratedAccount;
import ro.uvt.dp.main.account.interfaces.Operations;
import ro.uvt.dp.main.account.types.AccountEUR;
import ro.uvt.dp.main.account.types.AccountRON;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Account implements Operations, DecoratedAccount {

	protected String accountNumber = null;
	protected boolean blocked = false;
	protected double amount = 0;

	public enum TYPE {
		EUR, RON
	};

	protected Account(String accountNumber, double sum) throws AmountException, BlockedAccountException {
		this.accountNumber = accountNumber;
		depose(sum);
	}

	public double getTotalAmount() {
		return amount + amount * getInterest();
	}

	public double getAmount() {
		return amount;
	}

	public String getAccountType() {
		if (this instanceof AccountEUR) {
			return TYPE.EUR.toString();
		} else if (this instanceof AccountRON) {
			return TYPE.RON.toString();
		} else {
			return null;
		}
	}

	@Override
	public void depose(double sum) throws AmountException, BlockedAccountException {
		if (isBlocked()) {
			throw new BlockedAccountException("The account is blocked");
		}
		if (sum < 0) {
			throw new AmountException("Amount must be positive");
		}
		if (sum == 0) {
			throw new AmountException("Amount must be more than 0");
		}

		this.amount += sum;
	}

	@Override
	public void retrieve(double sum) throws AmountException, BlockedAccountException {
		if (isBlocked()) {
			throw new BlockedAccountException("The account is blocked");
		}
		if (sum < 0) {
			throw new AmountException("Amount must be positive");
		}
		if (sum > amount){
			throw new AmountException("Insufficient funds to retrieve" + sum);
		}
		if (sum == amount){
			throw new AmountException("Amount must be more than 0");
		}

		this.amount -= sum;
	}

	// added a new functionality to block an account
	public void blockAccount(){
		blocked = true;
	}

	public void unblockAccount(){
		blocked = false;
	}

	public boolean isBlocked(){
		return blocked;
	}

	public String toString() {
		return "Account: code=" + accountNumber + ", amount=" + amount;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	@Override
	public void decorateAccount() {
		System.out.print("Normal Account.");
	}

	public void saveToDatabaseAccounts(int clientId, int bankId) {
		try (Connection connection = DatabaseConnection.getConnection()) {
			int defaultAccountTypeId = getDefaultAccountTypeId(connection, "Normal");

			String query = "SELECT * FROM accounts WHERE account_number = ? AND client_id = ?";
			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				preparedStatement.setString(1, accountNumber);
				preparedStatement.setInt(2, clientId);

				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					if (resultSet.next()) {
						// If the account exists, update the details
						query = "UPDATE accounts SET sum = ? WHERE account_number = ? AND client_id = ?";
						try (PreparedStatement updateStatement = connection.prepareStatement(query)) {
							updateStatement.setDouble(1, amount);
							updateStatement.setString(2, accountNumber);
							updateStatement.setInt(3, clientId);
							updateStatement.executeUpdate();
						}
					} else {
						// If the account doesn't exist, insert new details
						query = "INSERT INTO accounts (account_number, client_id, account_type, sum, bank_id, account_type_id) VALUES (?, ?, ?, ?, ?, ?)";
						try (PreparedStatement insertStatement = connection.prepareStatement(query)) {
							insertStatement.setString(1, accountNumber);
							insertStatement.setInt(2, clientId);
							insertStatement.setString(3, this.getAccountType());
							insertStatement.setDouble(4, amount);
							insertStatement.setInt(5, bankId);
							insertStatement.setInt(6, defaultAccountTypeId);
							insertStatement.executeUpdate();
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error saving account to the database");
		}
	}

	private int getDefaultAccountTypeId(Connection connection, String typeName) throws SQLException {
		String query = "SELECT id FROM account_types WHERE type_name = ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setString(1, typeName);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getInt("id");
				}
			}
		}
		throw new RuntimeException("Account type '" + typeName + "' not found in the database");
	}

	public void updateAccountBalance() {
		try (Connection connection = DatabaseConnection.getConnection()) {
			String query = "UPDATE accounts SET sum = ? WHERE account_number = ?";

			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				preparedStatement.setDouble(1, this.getAmount());
				preparedStatement.setString(2, this.getAccountNumber());
				preparedStatement.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error updating account balance in the database");
		}
	}
}
