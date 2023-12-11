package ro.uvt.dp.test;

import ro.uvt.dp.exceptions.AccountNotFoundException;
import ro.uvt.dp.exceptions.AmountException;
import ro.uvt.dp.exceptions.BlockedAccountException;
import ro.uvt.dp.exceptions.ClientNotFoundException;
import ro.uvt.dp.main.BankingSystem;
import ro.uvt.dp.main.BankingSystemMediator;
import ro.uvt.dp.main.account.Account;
import ro.uvt.dp.main.account.BankCommandInvoker;
import ro.uvt.dp.main.account.commanders.DepositCommand;
import ro.uvt.dp.main.account.commanders.RetrieveCommand;
import ro.uvt.dp.main.account.commanders.TransferCommand;
import ro.uvt.dp.main.account.interfaces.BankCommand;
import ro.uvt.dp.main.account.types.AccountRON;
import ro.uvt.dp.main.bank.Bank;
import ro.uvt.dp.main.client.Client;

public class Test {

	public static void main(String[] args) throws AmountException, BlockedAccountException, AccountNotFoundException, ClientNotFoundException {
		/**
		 * Create BCR bank with 2 clients
		 */
		BankingSystemMediator mediator = new BankingSystem();
		Bank bcr = new Bank("BCR Bank", mediator);
		// Create client Ionescu with 2 accounts, one in EUR and one in RON
		Client cl1 = new Client.ClientBuilder("Ion Ionescu", "Iasi", Account.TYPE.EUR, "EUR124", 200.9).setGender("male").createClient(mediator);
		mediator.addClient(cl1);
		cl1.addAccount(Account.TYPE.RON, "RON1234", 400);
		// Create client Popescu with an account in RON
		Client cl2 = new Client.ClientBuilder("Maria Popescu", "Oradea", Account.TYPE.RON, "RON126", 100).setGender("female").createClient(mediator);
		mediator.addClient(cl2);
		System.out.println(bcr);

		/**
		 * Create bank CEC with one client
		 */
		Bank cec = new Bank("CEC Bank", mediator);
		Client clientCEC = new Client.ClientBuilder("George Georgescu", "Brasov", Account.TYPE.EUR, "EUR128", 700).setGender("male").createClient(mediator);
		cec.addClient(clientCEC);
		System.out.println(cec);

		BankCommandInvoker invoker = new BankCommandInvoker();

		// depose in account RON126 of client Vasile
		Client cl = mediator.getClient("Maria Popescu");
		if (cl != null) {
			BankCommand depositCommand = new DepositCommand(cl.getAccount("RON126"), 400);
			invoker.executeCommand(depositCommand);
			System.out.println(cl);
		}

		// retrieve from account RON126 of client Vasile
		if (cl != null) {
			BankCommand retrieveCommand = new RetrieveCommand(cl.getAccount("RON126"), 67);
			invoker.executeCommand(retrieveCommand);
			System.out.println(cl);
		}

		// transfer between accounts RON126 si RON1234
		AccountRON c1 = (AccountRON) cl.getAccount("RON126");
		AccountRON c2 = (AccountRON) mediator.getClient("Ion Ionescu").getAccount("RON1234");
		BankCommand transferCommand = new TransferCommand(c1, c2, 40);
		invoker.executeCommand(transferCommand);
		System.out.println(bcr);
	}
}
