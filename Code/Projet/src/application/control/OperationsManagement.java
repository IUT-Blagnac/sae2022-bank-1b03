package application.control;

import java.util.ArrayList;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.CategorieOperation;
import application.tools.PairsOfValue;
import application.tools.StageManagement;
import application.view.OperationsManagementController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.data.Client;
import model.data.CompteCourant;
import model.data.Operation;
import model.orm.AccessCompteCourant;
import model.orm.AccessOperation;
import model.orm.exception.ApplicationException;
import model.orm.exception.DatabaseConnexionException;

public class OperationsManagement {

	private Stage primaryStage; 
	private DailyBankState dbs;
	private OperationsManagementController omc;
	private Client clientDuCompte;
	private CompteCourant compteConcerne;

	/** Constructeur de la classe qui permet de paramétrer la fenêtre
	 * @param _parentStage	la scène parente
	 * @param _dbstate	la session de l'utilisateur
	 * @param client le client sélectionné sur lequel on gère les opérations
	 * @param compte le compte courant du client
	 */
	public OperationsManagement(Stage _parentStage, DailyBankState _dbstate, Client client, CompteCourant compte) {

		this.clientDuCompte = client;
		this.compteConcerne = compte;
		this.dbs = _dbstate;
		try {
			FXMLLoader loader = new FXMLLoader(
					OperationsManagementController.class.getResource("operationsmanagement.fxml"));
			BorderPane root = loader.load();

			Scene scene = new Scene(root, 900 + 20, 350 + 10);
			scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

			this.primaryStage = new Stage();
			this.primaryStage.initModality(Modality.WINDOW_MODAL);
			this.primaryStage.initOwner(_parentStage);
			StageManagement.manageCenteringStage(_parentStage, this.primaryStage);
			this.primaryStage.setScene(scene);
			this.primaryStage.setTitle("Gestion des opérations");
			this.primaryStage.setResizable(false);

			this.omc = loader.getController();
			this.omc.initContext(this.primaryStage, this, _dbstate, client, this.compteConcerne);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Affiche la scène grâce à l'appel de la fonction du controleur de gestion des opérations
	 */
	public void doOperationsManagementDialog() {
		this.omc.displayDialog();
	}

	/** Permet d'enregistrer un nouveau débit sur le compte d'un client
	 * @return l'opération effectuée (un débit)
	 */
	public Operation enregistrerDebit() {

		OperationEditorPane oep = new OperationEditorPane(this.primaryStage, this.dbs);
		Operation op = oep.doOperationEditorDialog(this.compteConcerne, CategorieOperation.DEBIT);
		if (op != null) {
			try {
				AccessOperation ao = new AccessOperation();

				ao.insertDebit(this.compteConcerne.idNumCompte, op.montant, op.idTypeOp);

			} catch (DatabaseConnexionException e) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dbs, e);
				ed.doExceptionDialog();
				this.primaryStage.close();
				op = null;
			} catch (ApplicationException ae) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dbs, ae);
				ed.doExceptionDialog();
				op = null;
			}
		}
		return op;
	}
	
	/**Permet d'enregistrer un nouveau crédit sur le compte d'un client
	 * @return l'opération effectuée (un créedit)
	 */
	public Operation enregistrerCredit() {
		
		OperationEditorPane oep = new OperationEditorPane(this.primaryStage, this.dbs);
		Operation op = oep.doOperationEditorDialog(this.compteConcerne, CategorieOperation.CREDIT);
		if(op != null) {
			try {
			AccessOperation ao = new AccessOperation();
			
			ao.insertCredit(this.compteConcerne.idNumCompte, op.montant, op.idTypeOp);
			
			}catch (DatabaseConnexionException e) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dbs, e);
				ed.doExceptionDialog();
				this.primaryStage.close();
				op = null;
			}catch (ApplicationException ae) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dbs, ae);
				ed.doExceptionDialog();
				op = null;
			}
		}
		return op;
	}

	/** Permet d'obtenir la liste des opérations par compte courant
	 * @return un duo de valeur avec le compte et les différentes opérations
	 */
	public PairsOfValue<CompteCourant, ArrayList<Operation>>  operationsEtSoldeDunCompte() {
		ArrayList<Operation> listeOP = new ArrayList<>();

		try {
			// Relecture BD du solde du compte
			AccessCompteCourant acc = new AccessCompteCourant();
			this.compteConcerne = acc.getCompteCourant(this.compteConcerne.idNumCompte);

			// lecture BD de la liste des operations du compte de l'utilisateur
			AccessOperation ao = new AccessOperation();
			listeOP = ao.getOperations(this.compteConcerne.idNumCompte);

		} catch (DatabaseConnexionException e) {
			ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dbs, e);
			ed.doExceptionDialog();
			this.primaryStage.close();
			listeOP = new ArrayList<>();
		} catch (ApplicationException ae) {
			ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dbs, ae);
			ed.doExceptionDialog();
			listeOP = new ArrayList<>();
		}
		System.out.println(this.compteConcerne.solde);
		return new PairsOfValue<>(this.compteConcerne, listeOP);
	}
}
