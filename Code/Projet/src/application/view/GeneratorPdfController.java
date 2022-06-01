package application.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import model.data.Client;
import model.data.CompteCourant;
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

	private Client clientDuCompte;

	private CompteCourant compteConcerne;

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
		this.chemin = null;
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
		 if (!Objects.isNull(this.chemin)) {
			 Document document = new Document();
			 try {
				PdfWriter.getInstance(document, new FileOutputStream(this.chemin + "/relevé.pdf"));
				document.open();
		        Paragraph preface = new Paragraph();
		        preface.add(new Paragraph("Client: " + this.clientDuCompte.nom));
		        preface.add(new Paragraph("Numéro client: " + this.clientDuCompte.idNumCli));
		        preface.add(new Paragraph("ID du compte: " + this.compteConcerne.idNumCompte));
		        preface.add(new Paragraph("Operation: "));
		        for(int i = 0; i < operationList.size(); i++) {
		        	preface.add(new Paragraph(operationList.get(i).toString()));
		        }
		        document.add(preface);
				document.close();
				this.stage.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		 }
	}

	/**
	 * Fermer la fenêtre
	 */
	private void annuler() {
		this.stage.close();
	}

	public void setCompteConcerne(CompteCourant pfCompteConcerne) {
		this.compteConcerne = pfCompteConcerne;
	}

	public void setClient(Client pfClientDuCompte) {
		this.clientDuCompte = pfClientDuCompte;
	}

}
