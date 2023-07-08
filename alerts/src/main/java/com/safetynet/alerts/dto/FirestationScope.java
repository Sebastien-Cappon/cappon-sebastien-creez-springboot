package com.safetynet.alerts.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;
import com.safetynet.alerts.model.Person;

/**
 * Model class which creates the DTO (Data Transfer Object)
 * <code>FirestationScope</code>. It contains getters and setters, as well as an
 * override <code>toSring()</code> method for display in the console.
 * 
 * @singularity <code>FirestationScope</code> contains a list of
 *              <code>Person</code>, who are covered by a given firestation, as
 *              an attribute. The two others attributes are the numbers of adult
 *              and child covered by this firestation.
 *
 * @author Sébastien Cappon
 * @version 1.0
 */
@JsonView(Views.FirestationScopeView.class)
public class FirestationScope {
	private int adultQuantity;
	private int childQuantity;
	private List<Person> personsCoveredByFirestation;

	public int getAdultQuantity() {
		return adultQuantity;
	}

	public void setAdultQuantity(int adultQuantity) {
		this.adultQuantity = adultQuantity;
	}

	public int getChildQuantity() {
		return childQuantity;
	}

	public void setChildQuantity(int childQuantity) {
		this.childQuantity = childQuantity;
	}

	public List<Person> getPersonsCoveredByFirestation() {
		return personsCoveredByFirestation;
	}

	public void setPersonsCoveredByFirestation(List<Person> personsCoveredByFirestation) {
		this.personsCoveredByFirestation = personsCoveredByFirestation;
	}

	/**
	 * An override method for user-friendly display of <code>FirestationScope</code>
	 * attributes in the console. Not necessary, except for test purposes.
	 * 
	 * @return <code>String</code> containing all the attributes of
	 *         <code>FirestationScope</code>.
	 */
	@Override
	public String toString() {
		return ("\nfirestationScope{" + "adultQuantity=" + adultQuantity + ", " + "childQuantity=" + childQuantity
				+ ",\n" + "personsCoveredByFirestation=" + personsCoveredByFirestation + "}");
	}
}