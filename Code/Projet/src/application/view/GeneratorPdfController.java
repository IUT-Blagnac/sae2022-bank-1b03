package application.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
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
				PdfWriter.getInstance(document, new FileOutputStream(this.chemin + "/Relevé " + this.clientDuCompte.nom + "_" + this.compteConcerne.idNumCompte  + ".pdf"));
				document.open();
				Paragraph body = new Paragraph();
				Paragraph titre = new Paragraph();
				Paragraph content = new Paragraph();
				Font bold = new Font(FontFamily.HELVETICA, 25, Font.BOLD);
				int nbColone = 3;
				PdfPTable tableau = new PdfPTable(nbColone);
				
				
				titre.setAlignment(Element.ALIGN_CENTER);
				titre.setFont(bold);
				titre.add("Relevé mensuel");
				
				content.add(new Paragraph("Client: " + this.clientDuCompte.prenom + " " + this.clientDuCompte.nom));
				content.add(new Paragraph("Numéro client: " + this.clientDuCompte.idNumCli));
				content.add(new Paragraph("Relevé du compte: " + this.compteConcerne.idNumCompte));
				
				
				tableau.addCell(new PdfPCell(new Paragraph("Date")));
				tableau.addCell(new PdfPCell(new Paragraph("Type opération")));
				tableau.addCell(new PdfPCell(new Paragraph("Montant")));
				
				for (int i = 0; i < this.operationList.size(); i++) {
					tableau.addCell(new PdfPCell(new Paragraph(this.operationList.get(i).dateOp.toString())));
					tableau.addCell(new PdfPCell(new Paragraph(String.format("%25s", this.operationList.get(i).idTypeOp))));
					tableau.addCell(new PdfPCell(new Paragraph(String.format("%10.02f", this.operationList.get(i).montant))));
				}
				
				body.add(titre);
				body.add(Chunk.NEWLINE);
				body.add(content);
				body.add(Chunk.NEWLINE);
				body.add(tableau);
				
				document.add(body);
				document.close();
				
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText("PDF");
				alert.setContentText("PDF généré: " + this.chemin);
				alert.showAndWait();
				
				this.stage.close();
			} catch (FileNotFoundException e) {
				erreur(e.toString());
			} catch (DocumentException e) {
				erreur(e.toString());
			}
		 }
	}
	
	private void erreur(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText("Erreur");
		alert.setContentText(message);
		alert.showAndWait();
	}

	/**
	 * Fermer la fenêtre
	 */
	private void annuler() {
		this.stage.close();
	}

	/**
	 * Permet de set le compte sur le controller
	 * @param pfCompteConcerne
	 */
	public void setCompteConcerne(CompteCourant pfCompteConcerne) {
		this.compteConcerne = pfCompteConcerne;
	}

	/**
	 * P=ermet de set le client sur le controller
	 * @param pfClientDuCompte
	 */
	public void setClient(Client pfClientDuCompte) {
		this.clientDuCompte = pfClientDuCompte;
	}

}
