package application;

import application.tools.ConstantesIHM;
import model.data.AgenceBancaire;
import model.data.Employe;

public class DailyBankState {
	private Employe empAct;
	private AgenceBancaire agAct;
	private boolean isChefDAgence;
	
	/**
	 * Donne un employ�
	 * 
	 * @return this.empAct
	 */
	public Employe getEmpAct() {
		return this.empAct;
	}

	/**
	 * D�fini un employ�
	 * 
	 * @param employeActif
	 */
	public void setEmpAct(Employe employeActif) {
		this.empAct = employeActif;
	}
	
	/**
	 * Donne l'agence bancaire
	 * 
	 * @return this.agAct
	 */
	public AgenceBancaire getAgAct() {
		return this.agAct;
	}

	/**
	 * D�fini l'agence bancaire
	 * 
	 * @param agenceActive
	 */
	public void setAgAct(AgenceBancaire agenceActive) {
		this.agAct = agenceActive;
	}

	/**
	 * Retourne si l'employ� est le chef de l'agence
	 * 
	 * @return this.isChefDAgence
	 */
	public boolean isChefDAgence() {
		return this.isChefDAgence;
	}

	/**
	 * D�fini l'employ� comme chef d'agence
	 * 
	 * @param isChefDAgence
	 */
	public void setChefDAgence(boolean isChefDAgence) {
		this.isChefDAgence = isChefDAgence;
	}

	/**
	 * Si l'employ� a les m�mes droits d'acc�s qu'un chef d'agence
	 * Alors il est d�fini comme chef d'agence
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
