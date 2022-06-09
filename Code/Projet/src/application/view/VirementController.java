package application.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

import application.DailyBankState;
import application.control.ExceptionDialog;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.data.CompteCourant;
import model.orm.AccessOperation;
import model.orm.exception.ApplicationException;
import model.orm.exception.DatabaseConnexionException;

public class VirementController implements Initializable {
	
	private CompteCourant compteConcerne;
	
	private Stage primaryStage;

	private DailyBankState dbs;

	private ObservableList<CompteCourant> listComptes;

	private Stage stage;
	
	private OperationsManagementController operationManagement;
	
	@FXML
	private TextField textFieldMontant;
	
	@FXML
	private Text soldeText;
	
	@FXML
	private Button buttonEnvoyer;
	
	@FXML
	private ListView<CompteCourant> listView;
	
	@FXML
	private Button buttonAnnuler;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		buttonEnvoyer.setOnAction(event -> envoyerVirement());
		buttonAnnuler.setOnAction(event -> annuler());
	}

	/**
	 * Permet d'envoyer un virement
	 */
	private void envoyerVirement() {
		int montant, selectedIndice;
		String montantText = this.textFieldMontant.getText();
		String erreur;
		
		erreur = "Entrer un entier inférieur ou égal au solde du compte";
		if (isParsable(montantText)) {
			montant = Integer.parseInt(montantText);
			if (montant <= this.compteConcerne.solde && montant > 0) {
				selectedIndice = this.listView.getSelectionModel().getSelectedIndex();
				erreur = "Selectionner un compte";
				if (selectedIndice >= 0) {
					erreur = null;
					try {
						AccessOperation ao = new AccessOperation();
						ao.insertDebit(this.compteConcerne.idNumCompte, montant, "Virement Compte à Compte");
						ao.insertCredit(this.listComptes.get(selectedIndice).idNumCompte, montant, "Virement Compte à Compte");
						this.stage.close();
						this.operationManagement.updateInfoCompteClient();
					} catch (DatabaseConnexionException e) {
						ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dbs, e);
						ed.doExceptionDialog();
						this.primaryStage.close();
					} catch (ApplicationException ae) {
						ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dbs, ae);
						ed.doExceptionDialog();
					}
				}
			}
		}
		
		if (!Objects.isNull(erreur)) {
			this.alertBox(erreur);
		}
		
	}
	
	/**
	 * Setter listView
	 * @param pfList
	 */
	public void setDataListView (ObservableList<CompteCourant> pfList) {
		this.listView.setItems(pfList);
		this.listComptes = pfList;
	}

	/**
	 * Setter compteConcerne
	 * @param pfCompteConcerne
	 */
	public void setCompteConcerne(CompteCourant pfCompteConcerne) {
		this.compteConcerne = pfCompteConcerne;
		this.soldeText.setText(Double.toString(pfCompteConcerne.solde));
	}
	
	/**
	 * Permet de savoir si une chaine de caractère peut être transformé en entier
	 * @param input
	 * @return vrais si c'est possible
	 */
	private static boolean isParsable(String input) {
	    try {
	        Integer.parseInt(input);
	        return true;
	    } catch (final NumberFormatException e) {
	        return false;
	    }
	}
	
	/**
	 * quitte le fenêtre
	 */
	private void annuler() {
		this.stage.close();
	}

	/**
	 * Setter primaryStage
	 * @param pfPrimaryStage
	 */
	public void setPrimaryStage(Stage pfPrimaryStage) {
		this.primaryStage = pfPrimaryStage;
	}
	
	/**
	 * setter dbs
	 * @param pfDbs
	 */
	public void setDataBase(DailyBankState pfDbs) {
		this.dbs = pfDbs;
	}
	
	/**
	 * Permet d'afficher une alert
	 * @param message
	 */
	private void alertBox (String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText("Erreur");
		alert.setContentText(message);
		alert.showAndWait();
	}

	/**
	 * Setter de stage
	 * @param pfStage
	 */
	public void setStage(Stage pfStage) {
		this.stage = pfStage;
	}

	/**
	 * Setter operationManagement
	 * @param pfOperationsManagementController
	 */
	public void setOperationManagement(OperationsManagementController pfOperationsManagementController) {
		this.operationManagement = pfOperationsManagementController;
	}

}
