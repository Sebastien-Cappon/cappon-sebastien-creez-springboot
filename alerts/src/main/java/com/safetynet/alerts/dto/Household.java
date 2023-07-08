package com.safetynet.alerts.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;
import com.safetynet.alerts.model.Person;

/**
 * Model class which creates the DTO (Data Transfer Object)
 * <code>Household</code>. It contains getters and setters, as well as an
 * override <code>toSring()</code> method for display in the console.
 * 
 * @singularity <code>Household</code> contains a list of <code>Person</code>,
 *              living at a given address. The other attribute is the list of
 *              firestations which cover this address.
 *
 * @author Sébastien Cappon
 * @version 1.0
 */
public class Household {
	@JsonView(Views.HouseholdFireView.class)
	private List<Long> firestationsNumber;
	@JsonView(Views.HouseholdView.class)
	private List<Person> householdMembers;

	public List<Long> getFirestationsNumber() {
		return firestationsNumber;
	}

	public void setFirestationsNumber(List<Long> firestationsONumber) {
		this.firestationsNumber = firestationsONumber;
	}

	public List<Person> getHouseholdMembers() {
		return householdMembers;
	}

	public void setHouseholdMembers(List<Person> householdMembers) {
		this.householdMembers = householdMembers;
	}

	/**
	 * An override method for user-friendly display of <code>Household</code> attributes in the
	 * console. Not necessary, except for test purposes.
	 * 
	 * @return <code>String</code> containing all the attributes of <code>Household</code>.
	 */
	@Override
	public String toString() {
		return ("\nhouseholdDetails={firestationsNumber=" + firestationsNumber + ", " + "\nhouseholdMembers="
				+ householdMembers + "}");
	}
}
