package application.control;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.StageManagement;
import application.view.LoginDialogController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.data.Employe;
import model.orm.AccessEmploye;
import model.orm.exception.ApplicationException;
import model.orm.exception.DatabaseConnexionException;

public class LoginDialog {

	private Stage primaryStage;
	private DailyBankState dbs;
	private LoginDialogController ldc;

	/** Constructeur de la classe qui permet de paramétrer la fenêtre
	 * @param _parentStage la scène parente
	 * @param _dbstate	la session de l'utilisateur
	 */
	public LoginDialog(Stage _parentStage, DailyBankState _dbstate) {
		this.dbs = _dbstate;
		try {
			FXMLLoader loader = new FXMLLoader(LoginDialogController.class.getResource("logindialog.fxml"));
			BorderPane root = loader.load();

			Scene scene = new Scene(root, root.getPrefWidth()+20, root.getPrefHeight()+10);
			scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

			this.primaryStage = new Stage();
			this.primaryStage.initModality(Modality.WINDOW_MODAL);
			this.primaryStage.initOwner(_parentStage);
			StageManagement.manageCenteringStage(_parentStage, this.primaryStage);
			this.primaryStage.setScene(scene);
			this.primaryStage.setTitle("Identification");
			this.primaryStage.setResizable(false);

			this.ldc = loader.getController();
			this.ldc.initContext(this.primaryStage, this, _dbstate);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Affiche la scène grâce à l'appel de la fonction du controleur de connexion
	 */
	public void doLoginDialog() {
		this.ldc.displayDialog();
	}

	/** Permet de chercher un employé a l'aide du nom d'utilisateur et du mot de passe inqiqué par l'utilisateur
	 * @param login le login de l'employé
	 * @param password le mot de passe de l'employé
	 * @return un employe si il en trouve un, sinon null
	 */
	public Employe chercherParLogin(String login, String password) {
		Employe employe = null;
		try {
			AccessEmploye ae = new AccessEmploye();

			employe = ae.getEmploye(login, password);

			if (employe != null) {
				this.dbs.setEmpAct(employe);
			}
		} catch (DatabaseConnexionException e) {
			ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dbs, e);
			ed.doExceptionDialog();
			this.dbs.setEmpAct(null);
			this.primaryStage.close();
			employe = null;
		} catch (ApplicationException ae) {
			ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dbs, ae);
			ed.doExceptionDialog();
			this.dbs.setEmpAct(null);
			this.primaryStage.close();
			employe = null;
		}
		return employe;
	}
}
