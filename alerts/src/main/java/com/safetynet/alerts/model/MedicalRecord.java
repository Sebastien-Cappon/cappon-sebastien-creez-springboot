package com.safetynet.alerts.model;

import java.time.LocalDate;
import java.util.List;

/**
 * Model class which creates the POJO (Plain Old Java Object)
 * <code>MedicalRecord</code>. It contains getters and setters, as well as an
 * override <code>toSring()</code> method for display in the console.
 *
 * @author Sébastien Cappon
 * @version 1.0
 */
public class MedicalRecord {
	private String firstName;
	private String lastName;
	private LocalDate birthdate;
	private List<String> medications;
	private List<String> allergies;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalDate getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(LocalDate birthdate) {
		this.birthdate = birthdate;
	}

	public List<String> getMedications() {
		return medications;
	}

	public void setMedications(List<String> medications) {
		this.medications = medications;
	}

	public List<String> getAllergies() {
		return allergies;
	}

	public void setAllergies(List<String> allergies) {
		this.allergies = allergies;
	}

	/**
	 * An override method for user-friendly display of <code>MedicalRecord</code>
	 * attributes in the console. Not necessary, except for test purposes.
	 * 
	 * @return <code>String</code> containing all the attributes of
	 *         <code>MedicalRecord</code>.
	 */
	@Override
	public String toString() {
		return ("\nmedicalrecord{" + "firstName=" + firstName + "," + "lastName=" + lastName + "," + "birthdate="
				+ birthdate + "," + "medications=" + medications + "," + "allergies=" + allergies + "}");
	}
}
