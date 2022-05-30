package application.view;

import java.io.File;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import model.data.Operation;

public class GeneratorPdfController implements Initializable {
	
	private Stage stage;
	
	private ObservableList<Operation> operationList;
	
	private String chemin;
	
	@FXML
	private Button annuler;
	
	@FXML
	private Button generer;
	
	@FXML
	private Button selectDossier;
	
	@FXML
	private Label cheminText;

	/**
	 * Setter stage
	 * @param pfStage
	 */
	public void setStage(Stage pfStage) {
		this.stage = pfStage;
	}

	/**
	 * Setter operationList
	 * @param pfOperationList
	 */
	public void setOperation(ObservableList<Operation> pfOperationList) {
		this.operationList = pfOperationList;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		annuler.setOnAction(event -> annuler());
		generer.setOnAction(event -> generer());
		selectDossier.setOnAction(event -> selectDossier());
		this.generer.setDisable(true);
	}

	/**
	 * Permet de selectionner un dossier
	 */
	private void selectDossier() {
		DirectoryChooser directoryChooser;
		File selectedDirectory;
		
		directoryChooser = new DirectoryChooser();
		selectedDirectory = directoryChooser.showDialog(this.stage);
		
		if (Objects.isNull(selectedDirectory)) {
			cheminText.setText("Aucun dossier selectionné");
			this.chemin = null;
			this.generer.setDisable(true);
		} else {
			cheminText.setText(selectedDirectory.getAbsolutePath());
			this.chemin = selectedDirectory.getAbsolutePath();
			this.generer.setDisable(false);
		}
		
	}

	/**
	 * Générer le PDF
	 */
	private void generer() {
		
	}

	/**
	 * Fermer la fenêtre
	 */
	private void annuler() {
		this.stage.close();
	}

}
