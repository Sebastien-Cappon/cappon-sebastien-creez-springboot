package com.safetynet.util;

import java.time.LocalDate;
import java.util.List;

import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;

public class InstanceBuilder {

	public static Firestation createFirestation(Long station, String address) {
		Firestation firestation = new Firestation();
		firestation.setStation(station);
		firestation.setAddress(address);

		return firestation;
	}

	public static MedicalRecord createMedicalRecord(String firstName, String lastName, LocalDate birthdate,
			List<String> medications, List<String> allergies) {
		MedicalRecord medicalRecord = new MedicalRecord();
		medicalRecord.setFirstName(firstName);
		medicalRecord.setLastName(lastName);
		medicalRecord.setBirthdate(birthdate);
		medicalRecord.setMedications(medications);
		medicalRecord.setAllergies(allergies);

		return medicalRecord;
	}

	public static Person createPerson(String firstName, String lastName, String address, String city, String zip,
			String phone, String email) {
		Person person = new Person();
		person.setFirstName(firstName);
		person.setLastName(lastName);
		person.setAddress(address);
		person.setCity(city);
		person.setZip(zip);
		person.setPhone(phone);
		person.setEmail(email);

		return person;
	}

	public static Person createPersonViewed(String firstName, String lastName, String address, String email, int age,
			List<String> medications, List<String> allergies) {
		Person personViewed = new Person();
		personViewed.setFirstName(firstName);
		personViewed.setLastName(lastName);
		personViewed.setAddress(address);
		personViewed.setEmail(email);
		personViewed.setAge(age);
		personViewed.setMedications(medications);
		personViewed.setAllergies(allergies);

		return personViewed;
	}
}
