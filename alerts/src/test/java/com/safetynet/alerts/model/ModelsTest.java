package com.safetynet.alerts.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class ModelsTest {

	@Test
	public void firestationToStringIsNotBlank() {
		String firestationToString = new Firestation().toString();
		assertThat(firestationToString).isNotBlank();
	}

	@Test
	public void medicalRecordToStringIsNotBlank() {
		String medicalRecordToString = new MedicalRecord().toString();
		assertThat(medicalRecordToString).isNotBlank();
	}

	@Test
	public void personToStringIsNotBlank() {
		String personToString = new Person().toString();
		assertThat(personToString).isNotBlank();
	}
}
