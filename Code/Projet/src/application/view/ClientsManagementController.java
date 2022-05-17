package application.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import application.DailyBankState;
import application.control.ClientsManagement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.Client;

public class ClientsManagementController implements Initializable {

	// Etat application
	private DailyBankState dbs;
	private ClientsManagement cm;

	// Fenêtre physique
	private Stage primaryStage;

	// Données de la fenêtre
	private ObservableList<Client> olc;

	// Manipulation de la fenêtre
	/**
	 * Initialise la fenêtre de l'application
	 * 
	 * @param _primaryStage
	 * @param _cm
	 * @param _dbstate
	 */
	public void initContext(Stage _primaryStage, ClientsManagement _cm, DailyBankState _dbstate) {
		this.cm = _cm;
		this.primaryStage = _primaryStage;
		this.dbs = _dbstate;
		this.configure();
	}

	/**
	 * Configure la fermeture de l'application
	 */
	private void configure() {
		this.primaryStage.setOnCloseRequest(e -> this.closeWindow(e));

		this.olc = FXCollections.observableArrayList();
		this.lvClients.setItems(this.olc);
		this.lvClients.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		this.lvClients.getFocusModel().focus(-1);
		this.lvClients.getSelectionModel().selectedItemProperty().addListener(e -> this.validateComponentState());
		this.validateComponentState();
	}

	/**
	 * Affiche la fenêtre de l'application
	 */
	public void displayDialog() {
		this.primaryStage.showAndWait();
	}

	// Gestion du stage
	/**
	 * Parametre la fermeture de la fenetre
	 * 
	 * @param e	fenetre
	 * @return	null
	 */
	private Object closeWindow(WindowEvent e) {
		this.doCancel();
		e.consume();
		return null;
	}

	// Attributs de la scene + actions
	@FXML
	private TextField txtNum;
	@FXML
	private TextField txtNom;
	@FXML
	private TextField txtPrenom;
	@FXML
	private ListView<Client> lvClients;
	@FXML
	private Button btnDesactClient;
	@FXML
	private Button btnModifClient;
	@FXML
	private Button btnComptesClient;

	/**
	 * Initialise le controleur
	 * @param location		un URL
	 * @param resources		ressources Bundle
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	/**
	 * Quitte le fenetre client
	 */
	@FXML
	private void doCancel() {
		this.primaryStage.close();
	}

	/**
	 * Recherche un client dans la base de données
	 */
	@FXML
	private void doRechercher() {
		int numCompte;
		try {
			String nc = this.txtNum.getText();
			if (nc.equals("")) {
				numCompte = -1;
			} else {
				numCompte = Integer.parseInt(nc);
				if (numCompte < 0) {
					this.txtNum.setText("");
					numCompte = -1;
				}
			}
		} catch (NumberFormatException nfe) {
			this.txtNum.setText("");
			numCompte = -1;
		}

		String debutNom = this.txtNom.getText();
		String debutPrenom = this.txtPrenom.getText();

		if (numCompte != -1) {
			this.txtNom.setText("");
			this.txtPrenom.setText("");
		} else {
			if (debutNom.equals("") && !debutPrenom.equals("")) {
				this.txtPrenom.setText("");
			}
		}

		// Recherche des clients en BD. cf. AccessClient > getClients(.)
		// numCompte != -1 => recherche sur numCompte
		// numCompte != -1 et debutNom non vide => recherche nom/prenom
		// numCompte != -1 et debutNom vide => recherche tous les clients
		ArrayList<Client> listeCli;
		listeCli = this.cm.getlisteComptes(numCompte, debutNom, debutPrenom);

		this.olc.clear();
		for (Client cli : listeCli) {
			this.olc.add(cli);
		}

		this.validateComponentState();
	}

	/**
	 * Entre dans le menu de modification d'un compte client
	 */
	@FXML
	private void doComptesClient() {
		int selectedIndice = this.lvClients.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			Client client = this.olc.get(selectedIndice);
			this.cm.gererComptesClient(client);
		}
	}

	/**
	 * Valide la modification d'un compte client
	 */
	@FXML
	private void doModifierClient() {

		int selectedIndice = this.lvClients.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			Client cliMod = this.olc.get(selectedIndice);
			Client result = this.cm.modifierClient(cliMod);
			if (result != null) {
				this.olc.set(selectedIndice, result);
			}
		}
	}

	/**
	 * Desactive un client actif 
	 */
	@FXML
	private void doDesactiverClient() {
	}

	/**
	 * Ajout d'un nouveau client
	 */
	@FXML
	private void doNouveauClient() {
		Client client;
		client = this.cm.nouveauClient();
		if (client != null) {
			this.olc.add(client);
		}
	}

	/**
	 * Modifie le statut des boutons en fonction de la selection correspondante
	 */
	private void validateComponentState() {
		// Non implémenté => désactivé
		this.btnDesactClient.setDisable(true);
		int selectedIndice = this.lvClients.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			this.btnModifClient.setDisable(false);
			this.btnComptesClient.setDisable(false);
		} else {
			this.btnModifClient.setDisable(true);
			this.btnComptesClient.setDisable(true);
		}
	}
}
