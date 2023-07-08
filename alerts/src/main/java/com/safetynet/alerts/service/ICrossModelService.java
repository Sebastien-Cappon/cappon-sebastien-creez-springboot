package com.safetynet.alerts.service;

import java.util.List;
import java.util.Map;

import com.safetynet.alerts.dto.Child;
import com.safetynet.alerts.dto.FirestationScope;
import com.safetynet.alerts.dto.Household;
import com.safetynet.alerts.model.Person;

/**
 * <code>CrossModelService</code> interface that abstracts it from its
 * implementation in order to achieve better code modularity in compliance with
 * SOLID principles.
 *
 * @author Sébastien Cappon
 * @version 1.0
 */
public interface ICrossModelService {

	public FirestationScope getPersonsCoveredByFirestation(Long stationNumber);
	public List<Child> getChildrenFromAddress(String address);
	public List<String> getPhoneNumberFromPersonsCoveredByFirestation(Long firestation);
	public Household getPersonsAndFirestationFromAddress(String address);
	public List<String> getAddressesCoveredByFirestation(Long firestationNumber);
	public Map<Long, Map<String, Household>> getHouseholdsCoveredByFirestations(List<Long> stations);
	public List<Person> getPersonsInfoByFirstNameAndLastName(String firstName, String lastName);
	public List<String> getEmailsFromCityResidents(String city);
}
