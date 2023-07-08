package com.safetynet.alerts.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.alerts.dto.Child;
import com.safetynet.alerts.dto.FirestationScope;
import com.safetynet.alerts.dto.Household;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataRepository;

/**
 * A service class which performs the business processes relating to the URL
 * endpoints and calls the repository for necessary basics CRUD operations.
 *
 * @singularity Contains manually implemented intermediate logs
 *              (<code>DEBUG</code> and <code>WARN</code>) to make it easier to
 *              monitor operations carried out on the API.
 *
 * @author Sébastien Cappon
 * @version 1.0
 */
@Service
public class CrossModelService implements ICrossModelService {
	private static final Logger logger = LoggerFactory.getLogger(CrossModelService.class);

	@Autowired
	private DataRepository repository;

	/**
	 * A <code>GET</code> method that returns a list of <code>Person</code>, along
	 * with the number of children and adults in them, covered by a fire station
	 * whose <code>stationNumber</code> is passed as a parameter.
	 * 
	 * @return <code>firestationScope</code> (DTO).
	 */
	@Override
	public FirestationScope getPersonsCoveredByFirestation(Long stationNumber) {
		FirestationScope firestationScope = new FirestationScope();

		List<Person> personsCoveredByFirestation = new ArrayList<>();
		int adultQuantity = 0;
		int childQuantity = 0;
		logger.debug("[CREATED] Person list, adults and children counters.");

		for (Firestation firestation : repository.getFirestationsList()) {
			if (firestation.getStation().equals(stationNumber)) {
				logger.debug("[CHECKED] Existence of Firestation (" + stationNumber + ") in the Firestation list.");
				for (Person person : repository.getPersonsList()) {
					if (firestation.getAddress().equals(person.getAddress())) {
						logger.debug("[CHECKED] Correspondence of Person (" + person.getFirstName() + " " + person.getLastName() + ") address (" + person.getAddress() + ") with one of those served by Firestation (" + firestation.getStation() + ").");
						personsCoveredByFirestation.add(person);

						if (person.getAge() > 18) {
							adultQuantity++;
						} else {
							childQuantity++;
						}
					}
				}
				logger.debug("[UPDATED] List of Persons covered by Firestation (" + stationNumber + "), adults and children counter.");
			}
		}

		firestationScope.setPersonsCoveredByFirestation(personsCoveredByFirestation);
		firestationScope.setAdultQuantity(adultQuantity);
		firestationScope.setChildQuantity(childQuantity);
		logger.debug("[UPDATED] Person list and initialized counters.");

		return firestationScope;
	}

	/**
	 * A <code>GET</code> method that returns a list of <code>Child</code>, as well
	 * as a list of other household members, at a given <code>address</code> passed
	 * as a parameter.
	 * 
	 * @return <code>Child</code> (DTO) list.
	 */
	@Override
	public List<Child> getChildrenFromAddress(String address) {
		List<Child> children = new ArrayList<>();

		for (Person person : repository.getPersonsList()) {
			if (person.getAddress().equals(address)) {
				logger.debug("[CHECKED] Correspondence of Person (" + person.getFirstName() + " " + person.getLastName() + ") address (" + person.getAddress() + ") with concerned address : " + address);
				if (person.getAge() <= 18) {
					logger.debug("[CHECKED] Person (" + person.getFirstName() + " " + person.getLastName() + ") is underage (" + person.getAge() + ")");
					List<Person> householdMembers = new ArrayList<>();
					Child child = new Child();

					child.setFirstName(person.getFirstName());
					child.setLastName(person.getLastName());
					child.setAge(person.getAge());

					for (Person potentialHouseholdMember : repository.getPersonsList()) {
						if (potentialHouseholdMember.getAddress().equals(address) && !potentialHouseholdMember.getFirstName().equals(child.getFirstName())) {
							logger.debug("[CHECKED] Correspondence of Person (" + potentialHouseholdMember.getFirstName() + " " + potentialHouseholdMember.getLastName() + ") address (" + potentialHouseholdMember.getAddress() + ") with concerned address : " + address);
							logger.debug("[CHECKED] Person (" + potentialHouseholdMember.getFirstName() + " " + potentialHouseholdMember.getLastName() + ") is not the actual child (" + child.getFirstName() + " " + child.getLastName() + ").");
							householdMembers.add(potentialHouseholdMember);
						}
					}
					logger.debug("[UPDATED] Household members list.");

					child.setHouseholdMembers(householdMembers);
					children.add(child);
				}
			}
		}
		logger.debug("[UPDATED] DTO Child list.");

		return children;
	}

	/**
	 * A <code>GET</code> method that returns a list of the phone numbers of
	 * residents covered by a <code>firestation</code> which its number is passed as
	 * a parameter.
	 * 
	 * @return A list of <code>String</code> that are phone numbers.
	 */
	@Override
	public List<String> getPhoneNumberFromPersonsCoveredByFirestation(Long firestation) {
		logger.debug("[CALL TO] DTO FirestationScope of the Firestation : " + firestation);
		FirestationScope firestationScope = getPersonsCoveredByFirestation(firestation);
		logger.debug("[GOT    ] DTO FirestationScope of the Firestation : " + firestation);
		List<String> phoneNumbers = new ArrayList<>();

		for (Person person : firestationScope.getPersonsCoveredByFirestation()) {
			if (!phoneNumbers.contains(person.getPhone())) {
				logger.debug("[CHECKED] Person (" + person.getFirstName() + " " + person.getLastName() + ") phone number (" + person.getPhone() + ") is not already in the list.");
				phoneNumbers.add(person.getPhone());
			}
		}
		logger.debug("[UPDATED] List of phone numbers.");

		return phoneNumbers;
	}

	/**
	 * A <code>GET</code> method which returns a list of all the residents living at
	 * the <code>address</code> passed in parameter, as well as the firestation(s)
	 * which should cover it.
	 * 
	 * @return <code>Household</code> (DTO).
	 */
	@Override
	public Household getPersonsAndFirestationFromAddress(String address) {
		List<Firestation> firestationsConcerned = repository.getFirestationsByAddress(address);
		logger.debug("[GOT    ] Firestation list of the firestations covering the " + address);
		List<Long> stationsConcerned = new ArrayList<>();
		List<Person> householdMembers = new ArrayList<>();
		Household household = new Household();

		for (Firestation firestation : firestationsConcerned) {
			stationsConcerned.add(firestation.getStation());
		}
		logger.debug("[UPDATED] List of firestations concerned.");

		for (Person person : repository.getPersonsList()) {
			if (person.getAddress().equals(address)) {
				logger.debug("[CHECKED] Correspondence of Person (" + person.getFirstName() + " " + person.getLastName() + ") address (" + person.getAddress() + ") with concerned address : " + address);
				householdMembers.add(person);
			}
		}
		logger.debug("[UPDATED] Person list of household members.");

		household.setFirestationsNumber(stationsConcerned);
		household.setHouseholdMembers(householdMembers);
		logger.debug("[UPDATED] DTO Household.");

		return household;
	}

	/**
	 * A <code>GET</code> method which returns a list of all the addresses covered
	 * by a firestation which <code>firestationNumber</code> is passed in parameter.
	 * 
	 * @return A list of <code>String</code> that are addresses.
	 */
	@Override
	public List<String> getAddressesCoveredByFirestation(Long firestationNumber) {
		List<String> addresses = new ArrayList<>();

		for (Firestation firestation : repository.getFirestationsList()) {
			if (firestation.getStation().equals(firestationNumber)) {
				logger.debug("[CHECKED] Firestation (" + firestationNumber + ") is in the Firestation list.");
				addresses.add(firestation.getAddress());
			}
		}
		logger.debug("[UPDATED] List of addresses with addresses covered by firestation (" + firestationNumber + ").");

		return addresses;
	}

	/**
	 * A <code>GET</code> method that returns a list of all the
	 * <code>Household</code> sorted by addresses covered by <code>stations</code>.
	 * The list of which is passed as parameter.
	 * 
	 * @return <code>Household</code> (DTO) list.
	 */
	@Override
	public Map<Long, Map<String, Household>> getHouseholdsCoveredByFirestations(List<Long> stations) {
		List<String> householdAddresses = new ArrayList<>();
		Map<Long, Map<String, Household>> householdsByAddressesByFirestation = new TreeMap<>();

		for (Long stationNumber : stations) {
			if (!this.getAddressesCoveredByFirestation(stationNumber).isEmpty()) {
				logger.debug("[CHECKED] DTO FirestationScope of the Firestation (" + stationNumber + ") is not empty.");
				Map<String, Household> householdsByAddress = new TreeMap<>();
				for (String address : this.getAddressesCoveredByFirestation(stationNumber)) {
					if (!householdAddresses.contains(address)) {
						logger.debug("[CHECKED] Address (" + address + ") is not already in the list of addresses.");
						householdAddresses.add(address);
						householdsByAddress.put(address, this.getPersonsAndFirestationFromAddress(address));
					}
				}
				householdsByAddressesByFirestation.put(stationNumber, householdsByAddress);
			}
		}
		logger.debug("[UPDATED] TreeMap of firestations concerned and TreeMap of DTOs Household by addresses.");

		return householdsByAddressesByFirestation;
	}

	/**
	 * A <code>GET</code> method that returns a list of
	 * <code>Person<code> whose <code>firstName</code> and <code>lastName</code> are
	 * passed as parameters.
	 * 
	 * @singularity Homonyms are taken into account.
	 * 
	 * @return <code>Household</code> (DTO) list.
	 */
	@Override
	public List<Person> getPersonsInfoByFirstNameAndLastName(String firstName, String lastName) {
		List<Person> personsConcerned = new ArrayList<>();

		for (Person person : repository.getPersonsList()) {
			if (person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)) {
				logger.debug("[CHECKED] Person (" + person.getFirstName() + " " + person.getLastName() + ") exists in the Person list.");
				personsConcerned.add(person);
			}
		}
		logger.debug("[UPDATED] Person list of persons named : " + firstName + " " + lastName);

		return personsConcerned;
	}

	/**
	 * A <code>GET</code> method that returns the list of email addresses of all the
	 * city residents.
	 * 
	 * @return A list of <code>String</code> that are email addresses.
	 */
	@Override
	public List<String> getEmailsFromCityResidents(String city) {
		List<String> emailsFromCityResidents = new ArrayList<>();

		for (Person person : repository.getPersonsList()) {
			if (person.getCity().equals(city) && !emailsFromCityResidents.contains(person.getEmail())) {
				logger.debug("[CHECKED] Person email (" + person.getEmail() + ") is not already in the mailing list.");
				emailsFromCityResidents.add(person.getEmail());
			}
		}
		logger.debug("[UPDATED] List of emails for the city of " + city);

		return emailsFromCityResidents;
	}
}
