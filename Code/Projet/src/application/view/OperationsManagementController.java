package application.view;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import application.DailyBankApp;
import application.DailyBankState;
import application.control.ClientsManagement;
import application.control.ComptesManagement;
import application.control.OperationsManagement;
import application.tools.NoSelectionModel;
import application.tools.PairsOfValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.Client;
import model.data.CompteCourant;
import model.data.Operation;

public class OperationsManagementController implements Initializable {

	// Etat application
	private DailyBankState dbs;
	private OperationsManagement om;

	// Fenêtre physique
	private Stage primaryStage;

	// Données de la fenêtre
	private Client clientDuCompte;
	private CompteCourant compteConcerne;
	private ObservableList<Operation> olOperation;

	// Manipulation de la fenêtre
	/**
	 * Initialise la fenêtre de l'application
	 * 
	 * @param _primaryStage
	 * @param _om
	 * @param _dbstate
	 * @param client
	 * @param compte
	 */
	public void initContext(Stage _primaryStage, OperationsManagement _om, DailyBankState _dbstate, Client client, CompteCourant compte) {
		this.primaryStage = _primaryStage;
		this.dbs = _dbstate;
		this.om = _om;
		this.clientDuCompte = client;
		this.compteConcerne = compte;
		this.configure();
	}

	/**
	 * Configure la fermeture de la fenêtre
	 */
	private void configure() {
		this.primaryStage.setOnCloseRequest(e -> this.closeWindow(e));

		this.olOperation = FXCollections.observableArrayList();
		this.lvOperations.setItems(this.olOperation);
		this.lvOperations.setSelectionModel(new NoSelectionModel<Operation>());
		this.updateInfoCompteClient();
		this.validateComponentState();
	}

	/**
	 * Affiche la fenêtre
	 */
	public void displayDialog() {
		this.primaryStage.showAndWait();
	}

	// Gestion du stage
	/**
	 * Paramètre la fermeture de la fenêtre
	 * 
	 * @param e
	 * @return
	 */
	private Object closeWindow(WindowEvent e) {
		this.doCancel();
		e.consume();
		return null;
	}

	// Attributs de la scene + actions
	@FXML
	private Label lblInfosClient;
	@FXML
	private Label lblInfosCompte;
	@FXML
	private ListView<Operation> lvOperations;
	@FXML
	private Button btnDebit;
	@FXML
	private Button btnCredit;
	@FXML
	private Button btnVirement;

	/**
	 * Initialise le controleur
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	/**
	 * Quitte la fenêtre client
	 */
	@FXML
	private void doCancel() {
		this.primaryStage.close();
	}

	/**
	 * Enregistrement d'une opération de type Débit
	 */
	@FXML
	private void doDebit() {

		Operation op = this.om.enregistrerDebit();
		if (op != null) {
			this.updateInfoCompteClient();
			this.validateComponentState();
		}
	}

	/**
	 * Enregistrement d'une opération de type Crédit
	 */
	@FXML
	private void doCredit() {
	}

	/**
	 * Enregistrement d'une opération réalisée par l'utilisateur
	 */
	@FXML
	private void doAutre() {		
		ListView<CompteCourant> comptesListView = new ListView<CompteCourant>();
		comptesListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		comptesListView.getFocusModel().focus(-1);
		// compte
		ClientsManagement clientManagement;
		ArrayList<Client> clients;
		ObservableList<CompteCourant> comptes = FXCollections.observableArrayList();
		
		clientManagement =  new ClientsManagement(this.primaryStage, this.dbs);
		clients = clientManagement.getlisteComptes(-1, "", "");
		
		for (Client client : clients) {
			ComptesManagement comptesManagement = new ComptesManagement(this.primaryStage, this.dbs, client);
			for ( CompteCourant compte : comptesManagement.getComptesDunClient()) {
				comptes.add(compte);
			}
		}
		
		try {
			System.out.println(this.getClass().getResource("virementPane.fxml"));
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(this.getClass().getResource("virementPane.fxml"));
			BorderPane pane = loader.load();
			VirementController ctrl = loader.getController();
			ctrl.setDataListView(comptes);
			ctrl.setCompteConcerne(compteConcerne);
			
			Scene scene = new Scene(pane);
			Stage stage = new Stage();
			stage.setTitle("Virement");
			stage.initOwner(this.primaryStage);
			stage.setScene(scene);
			
			stage.showAndWait();
			
		} catch (IOException e) {
			System.out.println("Erreur : " + e);
		}
		
		
		
	}

	/**
	 * Modifie le statut des boutons en fonction d'une validation
	 */
	private void validateComponentState() {
		// Non implémenté => désactivé
		this.btnCredit.setDisable(true);
		this.btnDebit.setDisable(false);
		this.btnVirement.setDisable(false);
	}

	/**
	 * Met à jour les informations d'un compte client
	 */
	private void updateInfoCompteClient() {

		PairsOfValue<CompteCourant, ArrayList<Operation>> opesEtCompte;

		opesEtCompte = this.om.operationsEtSoldeDunCompte();

		ArrayList<Operation> listeOP;
		this.compteConcerne = opesEtCompte.getLeft();
		listeOP = opesEtCompte.getRight();

		String info;
		info = this.clientDuCompte.nom + "  " + this.clientDuCompte.prenom + "  (id : " + this.clientDuCompte.idNumCli
				+ ")";
		this.lblInfosClient.setText(info);

		info = "Cpt. : " + this.compteConcerne.idNumCompte + "  "
				+ String.format(Locale.ENGLISH, "%12.02f", this.compteConcerne.solde) + "  /  "
				+ String.format(Locale.ENGLISH, "%8d", this.compteConcerne.debitAutorise);
		this.lblInfosCompte.setText(info);

		this.olOperation.clear();
		for (Operation op : listeOP) {
			this.olOperation.add(op);
		}

		this.validateComponentState();
	}
}
