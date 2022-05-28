package application.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import model.data.CompteCourant;

public class VirementController implements Initializable {
	
	private CompteCourant compteConcerne;
	
	private ObservableList<CompteCourant> listComptes;
	
	@FXML
	private TextField textFieldMontant;
	
	@FXML
	private Text soldeText;
	
	@FXML
	private Button buttonEnvoyer;
	
	@FXML
	private ListView<CompteCourant> listView;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		buttonEnvoyer.setOnAction(event -> envoyerVirement());
	}

	private void envoyerVirement() {
		int montant, selectedIndice;
		String montantText = this.textFieldMontant.getText();
		if (isParsable(montantText)) {
			montant = Integer.parseInt(montantText);
			if (montant <= this.compteConcerne.solde) {
				selectedIndice = this.listView.getSelectionModel().getSelectedIndex();
				if (selectedIndice >= 0) {
					
				}
			}
		}
	}
	
	public void setDataListView (ObservableList<CompteCourant> pfList) {
		this.listView.setItems(pfList);
		this.listComptes = pfList;
	}

	public void setCompteConcerne(CompteCourant pfCompteConcerne) {
		this.compteConcerne = pfCompteConcerne;
		this.soldeText.setText(Double.toString(pfCompteConcerne.solde));
	}
	
	public static boolean isParsable(String input) {
	    try {
	        Integer.parseInt(input);
	        return true;
	    } catch (final NumberFormatException e) {
	        return false;
	    }
	}

}
