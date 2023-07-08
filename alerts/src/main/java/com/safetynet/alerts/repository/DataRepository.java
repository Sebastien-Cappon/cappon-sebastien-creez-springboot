package com.safetynet.alerts.repository;

import java.util.List;

import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;

/**
 * Repository interface which abstracts it from its implementation in order to
 * obtain better code modularity in compliance with SOLID principles. This will
 * be particularly useful if you need to change or add data sources.
 *
 * @author Sébastien Cappon
 * @version 1.0
 */
public interface DataRepository {
	List<Person> getPersonsList();
	Person getPersonByFirstNameAndLastName(String firstName, String lastName);
	Person addPerson(Person person);
	Person updatePerson(String firstName, String lastName, Person update);
	void deletePerson(String firstName, String lastName);

	List<Firestation> getFirestationsList();
	List<Firestation> getFirestationsByAddress(String stationAddress);
	List<Firestation> getFirestationsByNumberAndAddress(Long stationNumber, String stationAddress);
	Firestation addFirestation(Firestation firestation);
	List<Firestation> updateFirestationNumber(Long stationNumber, String stationAddress, Long newStationNumber);
	void deleteFirestation(Long stationNumber, String stationAddress);

	List<MedicalRecord> getMedicalRecordsList();
	MedicalRecord getMedicalRecordByFirstNameAndLastName(String firstName, String lastName);
	MedicalRecord addMedicalRecord(MedicalRecord medicalRecord);
	MedicalRecord updateMedicalRecord(String firstName, String lastName, MedicalRecord update);
	void deleteMedicalRecord(String firstName, String lastName);
}
