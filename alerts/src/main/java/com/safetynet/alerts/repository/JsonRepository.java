package com.safetynet.alerts.repository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Value;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;

/**
 * A class that communicates with a data source (repository): a Json file. It
 * reads the content and injects it into POJOs. It contains all the API's
 * CRUD-specific methods.
 * 
 * @singularity Contains manually implemented intermediate logs
 *              (<code>DEBUG</code> and <code>WARN</code>) to make it easier to
 *              monitor operations carried out on the API.
 *
 * @author Sébastien Cappon
 * @version 1.0
 */
@Repository
public class JsonRepository implements DataRepository {
	private static final Logger logger = LoggerFactory.getLogger(JsonRepository.class);

	List<Person> persons = new ArrayList<>();
	List<Firestation> firestations = new ArrayList<>();
	List<MedicalRecord> medicalRecords = new ArrayList<>();

	public JsonRepository() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		Value format = JsonFormat.Value.forPattern("MM/dd/yyyy");
		objectMapper.configOverride(LocalDate.class).setFormat(format);

		File jsonFile = new File("src/main/resources/json/data.json");
		JsonNode rootNode = objectMapper.readTree(jsonFile);
		logger.debug("Json file read : " + jsonFile);

		JsonNode personsNode = rootNode.path("persons");
		JsonNode firestationsNode = rootNode.path("firestations");
		JsonNode medicalRecordsNode = rootNode.path("medicalrecords");

		ObjectReader personReader = objectMapper.readerFor(new TypeReference<List<Person>>() {});
		persons = personReader.readValue(personsNode);
		logger.debug("[CREATED] Person objects list.");
		ObjectReader firestationReader = objectMapper.readerFor(new TypeReference<List<Firestation>>() {});
		firestations = firestationReader.readValue(firestationsNode);
		logger.debug("[CREATED] Firestation objects list.");
		ObjectReader medicalRecordReader = objectMapper.readerFor(new TypeReference<List<MedicalRecord>>() {});
		medicalRecords = medicalRecordReader.readValue(medicalRecordsNode);
		logger.debug("[CREATED] MedicalRecord objects list.");
	}

	/**
	 * A <code>GET</code> method for extracting the list of <code>Person</code> and
	 * assigning them their <code>MedicalRecord</code> if one exists.
	 * 
	 * @return a list of <code>Person</code>
	 */
	@Override
	public List<Person> getPersonsList() {
		logger.debug("[STARTED] Merging each Person with their respective MedicalRecord.");
		for (Person person : persons) {
			for (MedicalRecord medicalRecord : medicalRecords) {
				if (person.getFirstName().equals(medicalRecord.getFirstName()) && person.getLastName().equals(medicalRecord.getLastName())) {
					logger.debug("[CHECKED] Person (" + person.getFirstName() + " " + person.getLastName() + ") has a MedicalRecord to attach.");

					int personAge = Period.between(medicalRecord.getBirthdate(), LocalDate.now()).getYears();
					logger.debug("[CREATED] Person age (" + personAge + ") is calculate from birthdate.");

					person.setBirthdate(medicalRecord.getBirthdate());
					person.setAge(personAge);
					person.setMedications(medicalRecord.getMedications());
					person.setAllergies(medicalRecord.getAllergies());
					logger.debug("[UPDATED] Person (" + person.getFirstName() + " " + person.getLastName() + ") and MedicalRecord attached.");
				}
			}
		}

		return persons;
	}

	/**
	 * A <code>GET</code> method for retrieving a <code>Person</code> designated by
	 * its <code>firstName</code> and <code>lastName</code>, passed as parameters.
	 * 
	 * @return <code>Person</code> if it exists. <code>Null</code> otherwise.
	 */
	@Override
	public Person getPersonByFirstNameAndLastName(String firstName, String lastName) {
		for (Person person : persons) {
			if (person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)) {
				logger.debug("[CHECKED] Person (" + person.getFirstName() + " " + person.getLastName() + ") exists in the Person list.");
				return person;
			}
		}

		logger.warn("This Person doesn't exist in the Person list.");
		return null;
	}

	/**
	 * A <code>POST</code> method for adding a <code>Person</code> passed as request
	 * body.
	 * 
	 * @return <code>Person</code> added. <code>Null</code> if the
	 *         <code>Person</code> already exists in the <code>Person</code> list.
	 */
	@Override
	public Person addPerson(Person person) {
		for (Person existingPerson : persons) {
			if (person.getFirstName().equals(existingPerson.getFirstName()) && person.getLastName().equals(existingPerson.getLastName())) {
				logger.warn("This Person already exists in the Person list.");
				return null;
			}
		}
		logger.debug("[CHECKED] Person to create is not already existing in the Person list.");

		if (person.getAddress() == null) { person.setAddress(""); }
		if (person.getCity() == null) { person.setCity(""); }
		if (person.getZip() == null) { person.setZip(""); }
		if (person.getPhone() == null) { person.setPhone(""); }
		if (person.getEmail() == null) { person.setEmail(""); }
		logger.debug("[UPDATED] Empty values for null values of requested keys.");

		persons.add(person);
		return person;
	}

	/**
	 * A <code>PUT</code> method for updating a <code>Person</code> passed as
	 * request body and designated by its <code>firstName</code> and
	 * <code>lastName</code>, passed as parameters.
	 * 
	 * @return <code>Person</code> updated. <code>Null</code> if the
	 *         <code>Person</code> is already updated.
	 */
	@Override
	public Person updatePerson(String firstName, String lastName, Person update) {
		Person personToUpdate = getPersonByFirstNameAndLastName(firstName, lastName);
		logger.debug("[GOT    ] Person to update : " + firstName + " " + lastName);
		String newAddress = update.getAddress();
		String newCity = update.getCity();
		String newZip = update.getZip();
		String newPhone = update.getPhone();
		String newEmail = update.getEmail();

		if (newAddress == null) { newAddress = personToUpdate.getAddress(); }
		if (newCity == null) { newCity = personToUpdate.getCity(); }
		if (newZip == null) { newZip = personToUpdate.getZip(); }
		if (newPhone == null) { newPhone = personToUpdate.getPhone(); }
		if (newEmail == null) { newEmail = personToUpdate.getEmail(); }
		logger.debug("[UPDATED] Empty values for null values of requested keys of the update");

		if (!newAddress.equals(personToUpdate.getAddress())
				|| !newCity.equals(personToUpdate.getCity())
				|| !newZip.equals(personToUpdate.getZip())
				|| !newPhone.equals(personToUpdate.getPhone())
				|| !newEmail.equals(personToUpdate.getEmail())) {
			logger.debug("[CHECKED] Person to update is not already updated (same values)");

			personToUpdate.setAddress(newAddress);
			personToUpdate.setCity(newCity);
			personToUpdate.setZip(newZip);
			personToUpdate.setPhone(newPhone);
			personToUpdate.setEmail(newEmail);

			return personToUpdate;
		}

		logger.warn("This Person seems to be already updated.");
		return null;
	}

	/**
	 * A <code>DELETE</code> method for deleting, from the
	 * <code>Person<code> list, a <code>Person</code> designated by its
	 * <code>firstName</code> and <code>lastName</code>, passed as parameters.
	 * 
	 * @return <code>void</code>
	 */
	@Override
	public void deletePerson(String firstName, String lastName) {
		Person personToDelete = getPersonByFirstNameAndLastName(firstName, lastName);
		logger.debug("[GOT    ] Person to delete : " + firstName + " " + lastName);

		persons.remove(personToDelete);
	}

	/**
	 * A <code>GET</code> method for extracting the list of <code>Firestation</code>
	 * mapping.
	 * 
	 * @return a list of <code>Firestation</code>
	 */
	@Override
	public List<Firestation> getFirestationsList() {
		return firestations;
	}

	/**
	 * A <code>GET</code> method for retrieving a list of <code>Firestation</code>
	 * mapping designated by their <code>firestationAddress</code>, passed as
	 * parameter.
	 * 
	 * @return <code>Firestation</code> if it exists.
	 */
	@Override
	public List<Firestation> getFirestationsByAddress(String firestationAddress) {
		List<Firestation> firestationsByAddress = new ArrayList<>();
		List<Long> stationsInTheListAbove = new ArrayList<>();

		for (Firestation firestation : firestations) {
			if (firestation.getAddress().equals(firestationAddress)) {
				if (!stationsInTheListAbove.contains(firestation.getStation())) {
					firestationsByAddress.add(firestation);
					stationsInTheListAbove.add(firestation.getStation());
				}
			}
		}
		logger.debug("[CHECKED] Existence of clones or multiple stations for : " + firestationAddress);

		return firestationsByAddress;
	}

	/**
	 * A <code>GET</code> method for retrieving a list of <code>Firestation</code>
	 * mapping designated by their <code>firestationNumber</code> and
	 * <code>firestationAddress</code>, passed as parameters.
	 * 
	 * @return <code>Firestation</code> mapping if it exists.
	 */
	@Override
	public List<Firestation> getFirestationsByNumberAndAddress(Long firestationNumber, String firestationAddress) {
		List<Firestation> targetedFirestations = new ArrayList<>();

		for (Firestation firestation : firestations) {
			if (firestation.getStation().equals(firestationNumber) && firestation.getAddress().equals(firestationAddress)) {
				targetedFirestations.add(firestation);
			}
		}
		logger.debug("[CHECKED] Existence of double mapping or multiple stations for the mapping : " + firestationNumber + " - " + firestationAddress);

		return targetedFirestations;
	}

	/**
	 * A <code>POST</code> method for adding a <code>Firestation</code> passed as
	 * request body.
	 * 
	 * @return <code>Firestation</code> added. <code>Null</code> if the
	 *         <code>Firestation</code> already exists in the
	 *         <code>Firestation</code> mapping list.
	 */
	@Override
	public Firestation addFirestation(Firestation firestation) {
		for (Firestation existingFirestation : firestations) {
			if (firestation.getStation().equals(existingFirestation.getStation()) && firestation.getAddress().equals(existingFirestation.getAddress())) {
				logger.warn("This mapping already exists in the Firestation mapping list.");
				return null;
			}
		}
		logger.debug("[CHECKED] Firestation map to create is not already existing in the Firestation mapping list.");

		firestations.add(firestation);
		return firestation;
	}

	/**
	 * A <code>PUT</code> method for updating a <code>Firestation</code> with its
	 * <code>newStationNumber</code> passed as request body and designated by its
	 * <code>stationNumber</code> and <code>stationsAddress</code>, passed as
	 * parameters.
	 * 
	 * @return <code>Firestation</code> updated. <code>Null</code> if the
	 *         <code>Firestation</code> is already updated or doesn't exist.
	 */
	@Override
	public List<Firestation> updateFirestationNumber(Long stationNumber, String stationAddress, Long newStationNumber) {
		if (!newStationNumber.equals(stationNumber)) {
			logger.debug("[CHECKED] Firestation map to update is not already updated.");

			List<Firestation> firestationsToUpdate = getFirestationsByNumberAndAddress(stationNumber, stationAddress);
			logger.debug("[GOT    ] List of Firestation maps to update : " + stationNumber + " - " + stationAddress);
			if (!firestationsToUpdate.isEmpty()) {
				for (Firestation firestation : firestationsToUpdate) {
					firestation.setStation(newStationNumber);
				}

				return firestationsToUpdate;
			} else {
				logger.warn("This mapping do not exist yet.");
				return null;
			}
		}

		logger.warn("This mapping seems to be already updated.");
		return null;
	}

	/**
	 * A <code>DELETE</code> method for deleting, from the
	 * <code>Firestation<code> list, a list <code>Firestation</code> designated by
	 * their <code>stationNumber</code> and <code>stationAddress</code>, passed as
	 * parameter.
	 * 
	 * @return <code>void</code>
	 */
	@Override
	public void deleteFirestation(Long stationNumber, String stationAddress) {
		List<Firestation> firestationToDelete = getFirestationsByNumberAndAddress(stationNumber, stationAddress);
		logger.debug("[GOT    ] List of Firestation maps to delete : " + stationNumber + " - " + stationAddress);

		for (Firestation firestation : firestationToDelete) {
			firestations.remove(firestation);
		}
	}

	/**
	 * A <code>GET</code> method for extracting the list of
	 * <code>MedicalRecord</code>
	 * 
	 * @return a list of <code>MedicalRecord</code>
	 */
	@Override
	public List<MedicalRecord> getMedicalRecordsList() {
		return medicalRecords;
	}

	/**
	 * A <code>GET</code> method for retrieving a <code>MedicalRecord</code>
	 * designated by its <code>firstName</code> and <code>lastName</code>, passed as
	 * parameter.
	 * 
	 * @return <code>MedicalRecord</code> if it exists. <code>Null</code> otherwise.
	 */
	@Override
	public MedicalRecord getMedicalRecordByFirstNameAndLastName(String firstName, String lastName) {
		for (MedicalRecord medicalRecord : medicalRecords) {
			if (medicalRecord.getFirstName().equals(firstName) && medicalRecord.getLastName().equals(lastName)) {
				logger.debug("[CHECKED] MedicalRecord (" + medicalRecord.getFirstName() + " " + medicalRecord.getLastName() + ") exists in the MedicalRecord list.");
				return medicalRecord;
			}
		}

		logger.warn("This medical record doesn't exist in the MedicalRecord list.");
		return null;
	}

	/**
	 * A <code>POST</code> method for adding a <code>MedicalRecord</code> passed as
	 * request body.
	 * 
	 * @return <code>MedicalRecord</code> added. <code>Null</code> if the
	 *         <code>MedicalRecord</code> already exists in the
	 *         <code>MedicalRecord</code> list.
	 */
	@Override
	public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord) {
		for (MedicalRecord existingMedicalRecord : medicalRecords) {
			if (medicalRecord.getFirstName().equals(existingMedicalRecord.getFirstName()) && medicalRecord.getLastName().equals(existingMedicalRecord.getLastName())) {
				logger.warn("This MedicalRecord already exists in the MedicalRecord list.");
				return null;
			}
		}
		logger.debug("[CHECKED] MedicalRecord to create is not already existing in the MedicalRecord list.");

		if (medicalRecord.getBirthdate() == null) { medicalRecord.setBirthdate(LocalDate.now()); }
		if (medicalRecord.getMedications() == null) { medicalRecord.setMedications(new ArrayList<String>()); }
		if (medicalRecord.getAllergies() == null) { medicalRecord.setAllergies(new ArrayList<String>()); }
		logger.debug("[UPDATED] Empty/Default values for null values of requested keys.");

		medicalRecords.add(medicalRecord);
		return medicalRecord;
	}

	/**
	 * A <code>PUT</code> method for updating a <code>MedicalRecord</code> with a
	 * <code>MedicalRecord</code> passed as request body and designated by its
	 * <code>firstName</code> and <code>lastName</code>, passed as parameters.
	 * 
	 * @return <code>MedicalRecord</code> updated. <code>Null</code> if the
	 *         <code>MedicalRecord</code> is already updated.
	 */
	@Override
	public MedicalRecord updateMedicalRecord(String firstName, String lastName, MedicalRecord update) {
		MedicalRecord medicalRecordToUpdate = getMedicalRecordByFirstNameAndLastName(firstName, lastName);
		logger.debug("[GOT    ] MedicalRecord to update : " + firstName + " " + lastName);
		LocalDate newBirthdate = update.getBirthdate();
		List<String> newMedications = update.getMedications();
		List<String> newAllergies = update.getAllergies();

		if (newBirthdate == null) { newBirthdate = medicalRecordToUpdate.getBirthdate(); }
		if (newMedications == null) { newMedications = medicalRecordToUpdate.getMedications(); }
		if (newAllergies == null) { newAllergies = medicalRecordToUpdate.getAllergies(); }
		logger.debug("[UPDATED] Empty values for null values of requested keys of the update");

		if (!newBirthdate.equals(medicalRecordToUpdate.getBirthdate())
				|| !newMedications.equals(medicalRecordToUpdate.getMedications())
				|| !newAllergies.equals(medicalRecordToUpdate.getAllergies())) {
			logger.debug("[CHECKED] MedicalRecord to update is not already updated (same values)");

			medicalRecordToUpdate.setBirthdate(newBirthdate);
			medicalRecordToUpdate.setMedications(newMedications);
			medicalRecordToUpdate.setAllergies(newAllergies);

			return medicalRecordToUpdate;
		}

		logger.warn("This MedicalRecord seems to be already updated.");
		return null;
	}
	
	/**
	 * A <code>DELETE</code> method for deleting, from the
	 * <code>MedicalRecord<code> list, a <code>MedicalRecord</code> designated by
	 * its <code>firstName</code> and <code>lastName</code>, passed as
	 * parameter.
	 * 
	 * @return <code>void</code>
	 */
	@Override
	public void deleteMedicalRecord(String firstName, String lastName) {
		MedicalRecord medicalRecordToDelete = getMedicalRecordByFirstNameAndLastName(firstName, lastName);
		logger.debug("[GOT    ] MedicalRecord to delete : " + firstName + " " + lastName);

		medicalRecords.remove(medicalRecordToDelete);
	}
}
