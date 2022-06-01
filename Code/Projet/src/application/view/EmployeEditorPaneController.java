package application.view;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import application.DailyBankState;
import application.control.ExceptionDialog;
import application.tools.AlertUtilities;
import application.tools.ConstantesIHM;
import application.tools.EditionMode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.Client;
import model.data.Employe;
import model.orm.exception.ApplicationException;
import model.orm.exception.Order;
import model.orm.exception.Table;

public class EmployeEditorPaneController implements Initializable {

	// Etat application
	private DailyBankState dbs;

	// Fenêtre physique
	private Stage primaryStage;

	// Données de la fenêtre
	private Employe employeEdite;
	private EditionMode em;
	private Employe employeResult;

	// Manipulation de la fenêtre
	/**
	 * Initialise la fenêtre de l'application
	 * 
	 * @param _primaryStage
	 * @param _dbstate
	 */
	public void initContext(Stage _primaryStage, DailyBankState _dbstate) {
		this.primaryStage = _primaryStage;
		this.dbs = _dbstate;
		this.configure();
	}

	/**
	 * Configure la fermeture de la fenêtre
	 */
	private void configure() {
		this.primaryStage.setOnCloseRequest(e -> this.closeWindow(e));
	}

	/**
	 * Affiche la fenêtre et initialise les différents fonctionnalités/modes de l'application
	 * 
	 * @param client
	 * @param mode
	 * @return
	 */
	public Employe displayDialog(Employe employe, EditionMode mode) {

		this.em = mode;
		if (employe == null) {
			this.employeEdite = new Employe();
		} else {
			this.employeEdite = new Employe(employe);
		}
		this.employeResult = null;
		switch (mode) {
		case CREATION:
			this.txtIdemp.setDisable(true);
			this.txtNom.setDisable(false);
			this.txtPrenom.setDisable(false);
			
			this.lblMessage.setText("Informations sur le nouvel employe");
			this.butOk.setText("Ajouter");
			this.butCancel.setText("Annuler");
			break;
		case MODIFICATION:
			this.txtIdemp.setDisable(true);
			this.txtNom.setDisable(false);
			this.txtPrenom.setDisable(false);
			
			this.lblMessage.setText("Informations employe");
			this.butOk.setText("Modifier");
			this.butCancel.setText("Annuler");
			break;
		case SUPPRESSION:
			// ce mode n'est pas utilisé pour les Clients :
			// la suppression d'un client n'existe pas il faut que le chef d'agence
			// bascule son état "Actif" à "Inactif"
			ApplicationException ae = new ApplicationException(Table.NONE, Order.OTHER, "SUPPRESSION CLIENT NON PREVUE",
					null);
			ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dbs, ae);
			ed.doExceptionDialog();

			break;
		}
		// Paramétrages spécifiques pour les chefs d'agences
		if (ConstantesIHM.isAdmin(this.dbs.getEmpAct())) {
			// rien pour l'instant
		}
		// initialisation du contenu des champs
		this.txtIdemp.setText("" + this.employeEdite.idEmploye);
		this.txtNom.setText(this.employeEdite.nom);
		this.txtPrenom.setText(this.employeEdite.prenom);
		this.txtDrAc.setText(this.employeEdite.droitsAccess);

		this.employeResult = null;

		this.primaryStage.showAndWait();
		return this.employeResult;
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
	private Label lblMessage;
	@FXML
	private TextField txtIdemp;
	@FXML
	private TextField txtNom;
	@FXML
	private TextField txtPrenom;
	@FXML
	private TextField txtDrAc;
	@FXML
	private Button butOk;
	@FXML
	private Button butCancel;

	/**
	 * Initalise le controleur
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	/**
	 * Quitte la fenêtre client
	 */
	@FXML
	private void doCancel() {
		this.employeResult = null;
		this.primaryStage.close();
	}

	/**
	 * Ajoute, modifie ou supprime un client (ou non) selon le résultat de la saisie (true/false)
	 */
	@FXML
	private void doAjouter() {
		switch (this.em) {
		case CREATION:
			if (this.isSaisieValide()) {
				this.employeResult = this.employeEdite;
				this.primaryStage.close();
			}
			break;
		case MODIFICATION:
			if (this.isSaisieValide()) {
				this.employeResult = this.employeEdite;
				this.primaryStage.close();
			}
			break;
		case SUPPRESSION:
			this.employeResult = this.employeEdite;
			this.primaryStage.close();
			break;
		}

	}

	/**
	 * Vérifie si la saisie est valide
	 * 
	 * @return la validité de la saisie (true/false)
	 */
	private boolean isSaisieValide() {
		this.employeEdite.nom = this.txtNom.getText().trim();
		this.employeEdite.prenom = this.txtPrenom.getText().trim();
		this.employeEdite.droitsAccess = this.txtDrAc.getText().trim();

		if (this.employeEdite.nom.isEmpty()) {
			AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie", null, "Le nom ne doit pas être vide",
					AlertType.WARNING);
			this.txtNom.requestFocus();
			return false;
		}
		if (this.employeEdite.prenom.isEmpty()) {
			AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie", null, "Le prénom ne doit pas être vide",
					AlertType.WARNING);
			this.txtPrenom.requestFocus();
			return false;
		}

		return true;
	}
}
