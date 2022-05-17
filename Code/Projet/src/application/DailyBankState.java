package application;

import application.tools.ConstantesIHM;
import model.data.AgenceBancaire;
import model.data.Employe;

public class DailyBankState {
	private Employe empAct;
	private AgenceBancaire agAct;
	private boolean isChefDAgence;

	/**
	 * Renvoie l'employé empAct
	 * 
	 * @return l'employé actuel
	 */
	public Employe getEmpAct() {
		return this.empAct;
	}

	/**
	 * Initialise un employé dit actif
	 * 
	 * @param employeActif
	 */
	public void setEmpAct(Employe employeActif) {
		this.empAct = employeActif;
	}

	/**
	 * Retourne l'agence actuelle
	 * 
	 * @return l'agence courante
	 */
	public AgenceBancaire getAgAct() {
		return this.agAct;
	}
	
	/**
	 * Initialise l'agence bancaire
	 * 
	 * @param agenceActive
	 */
	public void setAgAct(AgenceBancaire agenceActive) {
		this.agAct = agenceActive;
	}

	/**
	 * Retourne le chef d'agence
	 * 
	 * @return le chef d'agence
	 */
	public boolean isChefDAgence() {
		return this.isChefDAgence;
	}

	/**
	 * Initialise le chef d'agence
	 * 
	 * @param isChefDAgence
	 */
	public void setChefDAgence(boolean isChefDAgence) {
		this.isChefDAgence = isChefDAgence;
	}

	/**
	 * Initialise les droits d'accès du chef d'agence
	 * 
	 * @param droitsAccess
	 */
	public void setChefDAgence(String droitsAccess) {
		this.isChefDAgence = false;

		if (droitsAccess.equals(ConstantesIHM.AGENCE_CHEF)) {
			this.isChefDAgence = true;
		}
	}
}
