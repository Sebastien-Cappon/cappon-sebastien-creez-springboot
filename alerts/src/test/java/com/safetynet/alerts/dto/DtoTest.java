package com.safetynet.alerts.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class DtoTest {

	@Test
	public void childToStringIsNotBlank() {
		String childToString = new Child().toString();
		assertThat(childToString).isNotBlank();
	}

	@Test
	public void firestationScopeToStringIsNotBlank() {
		String firestationScopeToString = new FirestationScope().toString();
		assertThat(firestationScopeToString).isNotBlank();
	}

	@Test
	public void householdToStringIsNotBlank() {
		String householdToString = new Household().toString();
		assertThat(householdToString).isNotBlank();
	}
}
