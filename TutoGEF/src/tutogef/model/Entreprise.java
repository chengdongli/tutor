package tutogef.model;

public class Entreprise extends Node {
	public static final String PROPERTY_CAPITAL="EntrpriseCapital";

	private String address;
	private int capital;
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public void setCapital(int capital) {
		int oldCapital = this.capital;
		this.capital = capital;
		getListeners().firePropertyChange(PROPERTY_CAPITAL, oldCapital, capital);
	}
	
	public String getAddress() {
		return this.address;
	}
	
	public int getCapital() {
		return this.capital;
	}
}
