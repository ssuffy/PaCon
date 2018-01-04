package com.sofi.pacon.domain.model

import java.util.Date;

public class Event {
	private Date dateDebut;
	private Date dateFin;
	private int nbDay;
	private String action;
	private String cause;
	private String effect;
	private int daysOff;
	private String environment;

	public Date getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	public Date getDateFin() {
		return dateFin;
	}

	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}

	public int getNbDay() {
		return nbDay;
	}

	public void setNbDay(int nbDay) {
		this.nbDay = nbDay;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public String getEffect() {
		return effect;
	}

	public void setEffect(String effect) {
		this.effect = effect;
	}

	public int getDaysOff() {
		return daysOff;
	}

	public void setDaysOff(int daysOff) {
		this.daysOff = daysOff;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}


}
