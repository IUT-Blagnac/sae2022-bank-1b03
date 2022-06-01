package application.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import application.DailyBankState;
import application.control.ClientsManagement;
import application.control.EmployesManagement;
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
import model.data.Employe;

public class EmployesManagementController implements Initializable {

	// Etat application
	private DailyBankState dbs;
	private EmployesManagement em;

	// Fenêtre physique
	private Stage primaryStage;

	// Données de la fenêtre
	private ObservableList<Employe> olc;

	// Manipulation de la fenêtre
	/**
	 * Initialise la fenêtre de l'application
	 * 
	 * @param _primaryStage
	 * @param _cm
	 * @param _dbstate
	 */
	public void initContext(Stage _primaryStage, EmployesManagement _em, DailyBankState _dbstate) {
		this.em = _em;
		this.primaryStage = _primaryStage;
		this.dbs = _dbstate;
		this.configure();
		this.doRechercher();
	}

	/**
	 * Configure la fermeture de l'application
	 */
	private void configure() {
		this.primaryStage.setOnCloseRequest(e -> this.closeWindow(e));

		this.olc = FXCollections.observableArrayList();
		this.lvEmployes.setItems(this.olc);;
		this.lvEmployes.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		this.lvEmployes.getFocusModel().focus(-1);
		this.lvEmployes.getSelectionModel().selectedItemProperty().addListener(e -> this.validateComponentState());
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
	private ListView<Employe> lvEmployes;
	@FXML
	private Button btnDesactEmploye;
	@FXML
	private Button btnModifEmploye;

	/**
	 * Initialise le controleur
	 * @param location		un URL
	 * @param resources		ressources Bundle
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	/**
	 * Quitte le fenetre employe
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

		this.olc.clear();

		this.validateComponentState();
	}

	/**
	 * Valide la modification d'un compte client
	 */
	@FXML
	private void doModifierEmploye() {

		int selectedIndice = this.lvEmployes.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			Employe empMod = (Employe) this.olc;
			Employe result = this.em.modifierEmploye(empMod);
			if (result != null) {
				this.olc.set(selectedIndice, result);
			}
		}
	}

	/**
	 * Desactive un client actif 
	 */
	@FXML
	private void doDesactiverEmploye() {
	}

	/**
	 * Ajout d'un nouveau client
	 */
	@FXML
	private void doNouveauEmploye() {
		Employe employe;
		employe = this.em.nouveauEmploye();
		if (employe != null) {
			this.olc.add(employe);
		}
	}

	/**
	 * Modifie le statut des boutons en fonction de la selection correspondante
	 */
	private void validateComponentState() {
		// Non implémenté => désactivé
		this.btnDesactEmploye.setDisable(true);
		int selectedIndice = this.lvEmployes.getSelectionModel().getSelectedIndex();
		if (selectedIndice >= 0) {
			this.btnModifEmploye.setDisable(false);
		} else {
			this.btnModifEmploye.setDisable(true);
		}
	}
}
