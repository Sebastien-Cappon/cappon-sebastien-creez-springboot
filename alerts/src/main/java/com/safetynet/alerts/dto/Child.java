package com.safetynet.alerts.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;
import com.safetynet.alerts.model.Person;

/**
 * Model class which creates the DTO (Data Transfer Object) <code>Child</code>.
 * It contains getters and setters, as well as an override
 * <code>toSring()</code> method for display in the console.
 * 
 * @singularity <code>Child</code> inherits <code>Person</code>.
 *              <code>Child</code> contains a list of <code>Person</code>, who
 *              lives at the same address, as an attribute.
 *
 * @author Sébastien Cappon
 * @version 1.0
 */
@JsonView(Views.ChildView.class)
public class Child extends Person {
	private List<Person> householdMembers;

	public List<Person> getHouseholdMembers() {
		return householdMembers;
	}

	public void setHouseholdMembers(List<Person> householdMembers) {
		this.householdMembers = householdMembers;
	}

	/**
	 * An override method for user-friendly display of <code>Child</code> attributes in the
	 * console. Not necessary, except for test purposes.
	 * 
	 * @return <code>String</code> containing all the attributes of <code>Child</code>.
	 */
	@Override
	public String toString() {
		return ("\nchild{" + "firstName=" + firstName + ", " + "lastName=" + lastName + ", " + "age=" + age + ",\n"
				+ "householdMembers=" + householdMembers + "}");
	}
}
