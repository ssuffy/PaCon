package com.sofi.pacon.domain.model;

import java.util.Date;
import java.util.List;

public class Day {
	private Date date;
	private int score;
	private String intensity;
	private String painDiffusion;
	private int interruption;
	private List<String> feltPain;
	private List<String> PainLocation;
	private List<String> activity;
	private String environment;
	private List<String> contributeFactor;
	private List<String> relieveEffect;
	private List<String> ineffectiveFactor;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getIntensity() {
		return intensity;
	}

	public void setIntensity(String intensity) {
		this.intensity = intensity;
	}

	public String getPainDiffusion() {
		return painDiffusion;
	}

	public void setPainDiffusion(String painDiffusion) {
		this.painDiffusion = painDiffusion;
	}

	public int getInterruption() {
		return interruption;
	}

	public void setInterruption(int interruption) {
		this.interruption = interruption;
	}

	public List<String> getFeltPain() {
		return feltPain;
	}

	public void setFeltPain(List<String> feltPain) {
		this.feltPain = feltPain;
	}

	public List<String> getPainLocation() {
		return PainLocation;
	}

	public void setPainLocation(List<String> painLocation) {
		PainLocation = painLocation;
	}

	public List<String> getActivity() {
		return activity;
	}

	public void setActivity(List<String> activity) {
		this.activity = activity;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public List<String> getContributeFactor() {
		return contributeFactor;
	}

	public void setContributeFactor(List<String> contributeFactor) {
		this.contributeFactor = contributeFactor;
	}

	public List<String> getRelieveEffect() {
		return relieveEffect;
	}

	public void setRelieveEffect(List<String> relieveEffect) {
		this.relieveEffect = relieveEffect;
	}

	public List<String> getIneffectiveFactor() {
		return ineffectiveFactor;
	}

	public void setIneffectiveFactor(List<String> ineffectiveFactor) {
		this.ineffectiveFactor = ineffectiveFactor;
	}
}
