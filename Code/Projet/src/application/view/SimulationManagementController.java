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

public class SimulationManagementController implements Initializable {

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
		this.lvEtapeEmprunt.setItems(this.olc);
		this.lvEtapeEmprunt.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		this.lvEtapeEmprunt.getFocusModel().focus(-1);
		this.lvEtapeEmprunt.getSelectionModel().selectedItemProperty().addListener(e -> this.validateComponentState());
		this.validateComponentState();
		
		this.olc = FXCollections.observableArrayList();
		this.lvEtapeAssurance.setItems(this.olc);
		this.lvEtapeAssurance.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		this.lvEtapeAssurance.getFocusModel().focus(-1);
		this.lvEtapeAssurance.getSelectionModel().selectedItemProperty().addListener(e -> this.validateComponentState());
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
	private TextField txtMontant;
	@FXML
	private TextField txtDuree;
	@FXML
	private TextField txtTaux;
	@FXML
	private ListView<Client> lvEtapeEmprunt;
	@FXML
	private ListView<Client> lvEtapeAssurance;
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
	 * Simule un emprunt
	 */
	@FXML
	private void doSimulerEmprunt() {
		//à faire
	}
	
	/**
	 * Simule une assurance d'emprunt
	 */
	@FXML
	private void doSimulerAssurance() {
		//à faire
	}

	/**
	 * Modifie le statut des boutons en fonction de la selection correspondante
	 */
	private void validateComponentState() {
		// Non implémenté => désactivé
		this.btnDesactClient.setDisable(true);
		int selectedIndice = this.lvEtapeEmprunt.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			this.btnModifClient.setDisable(false);
			this.btnComptesClient.setDisable(false);
		} else {
			this.btnModifClient.setDisable(true);
			this.btnComptesClient.setDisable(true);
		}
	}
}
