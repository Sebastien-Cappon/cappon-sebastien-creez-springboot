package com.safetynet.util;

import java.util.List;

import com.safetynet.alerts.dto.Child;
import com.safetynet.alerts.dto.FirestationScope;
import com.safetynet.alerts.dto.Household;
import com.safetynet.alerts.model.Person;

public class DtoInstanceBuilder {

	/* FULL PARAM CHILD BUILDER : KEEP IT JUST IN CASE.
	 * public static Child createChild(String firstName, String lastName, String address, String city, String zip, String phone, String email, List<Person> householdMembers) {
	 * 	Child child = new Child();
	 * 	child.setFirstName(firstName);
	 * 	child.setLastName(lastName);
	 * 	child.setAddress(address);
	 * 	child.setCity(city);
	 * 	child.setZip(zip);
	 * 	child.setPhone(phone);
	 * 	child.setEmail(email);
	 * 
	 * 	child.setHouseholdMembers(householdMembers);
	 * 
	 * 	return child;
	 * }
	 */

	public static Child createChildViewed(String firstName, String lastName, int age, List<Person> householdMembers) {
		Child childViewed = new Child();
		childViewed.setFirstName(firstName);
		childViewed.setLastName(lastName);
		childViewed.setAge(age);

		childViewed.setHouseholdMembers(householdMembers);

		return childViewed;
	}

	public static FirestationScope createFirestationScope(int adultQuantity, int childQuantity, List<Person> personCoverdByFirestation) {
		FirestationScope firestationScope = new FirestationScope();
		firestationScope.setAdultQuantity(adultQuantity);
		firestationScope.setChildQuantity(childQuantity);
		firestationScope.setPersonsCoveredByFirestation(personCoverdByFirestation);

		return firestationScope;
	}

	public static Household createHousehold(List<Long> firestationNumber, List<Person> householdMembers) {
		Household household = new Household();
		household.setFirestationsNumber(firestationNumber);
		household.setHouseholdMembers(householdMembers);

		return household;
	}
}
