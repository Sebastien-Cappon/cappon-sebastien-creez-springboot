package com.safetynet.alerts.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@SpringBootTest
@AutoConfigureMockMvc
public class CrossModelUrlsIT {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void getPersonsCoveredByFirestation_shouldReturnOk() throws Exception {
		mockMvc.perform(get("/firestation")
				.param("stationNumber", "2")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.adultQuantity").value(4))
			.andExpect(jsonPath("$.childQuantity").value(1))
			.andExpect(jsonPath("$.personsCoveredByFirestation.[0].firstName").value("Jonanathan"))
			.andExpect(jsonPath("$.personsCoveredByFirestation.[0].lastName").value("Marrack"))
			.andExpect(jsonPath("$.personsCoveredByFirestation.[0].address").value("29 15th St"))
			.andExpect(jsonPath("$.personsCoveredByFirestation.[0].phone").value("841-874-6513"));
	}

	@Test
	public void getPersonsCoveredByFirestation_shouldReturnNoContent() throws Exception {
		mockMvc.perform(get("/firestation")
				.param("stationNumber", "42")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	public void getChildrenFromAddress_shouldReturnOk() throws Exception {
		mockMvc.perform(get("/childAlert")
				.param("address", "1509 Culver St")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.[0].firstName").value("Tenley"))
			.andExpect(jsonPath("$.[0].lastName").value("Boyd"))
			.andExpect(jsonPath("$.[0].age").value(11))
			.andExpect(jsonPath("$.[0].householdMembers.[0].firstName").value("John"))
			.andExpect(jsonPath("$.[0].householdMembers.[0].lastName").value("Boyd"))
			.andExpect(jsonPath("$.[0].householdMembers.[0].age").value(39));
	}

	@Test
	public void getChildrenFromAddress_shouldReturnNoContent() throws Exception {
		mockMvc.perform(get("/childAlert")
				.param("address", "155 Country Lane")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	public void getPhoneNumberFromPersonsCoveredByFirestation_shouldReturn() throws Exception {
		mockMvc.perform(get("/phoneAlert")
				.param("firestation", "2")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.[0]").value("841-874-6513"))
			.andExpect(jsonPath("$.[1]").value("841-874-7878"))
			.andExpect(jsonPath("$.[2]").value("841-874-7512"))
			.andExpect(jsonPath("$.[3]").value("841-874-7458"));
	}

	@Test
	public void getPhoneNumberFromPersonsCoveredByFirestation_shouldNoContent() throws Exception {
		mockMvc.perform(get("/phoneAlert")
				.param("firestation", "42")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	public void getPersonsAndFirestationFromAddress_shouldReturnOk() throws Exception {
		mockMvc.perform(get("/fire")
				.param("address", "1509 Culver St")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.firestationsNumber.[0]").value(3))
			.andExpect(jsonPath("$.householdMembers.[0].firstName").value("John"))
			.andExpect(jsonPath("$.householdMembers.[0].lastName").value("Boyd"))
			.andExpect(jsonPath("$.householdMembers.[0].phone").value("841-874-6512"))
			.andExpect(jsonPath("$.householdMembers.[0].age").value(39))
			.andExpect(jsonPath("$.householdMembers.[0].medications").value(new ArrayList<>(List.of("aznol:350mg", "hydrapermazol:100mg"))))
			.andExpect(jsonPath("$.householdMembers.[0].allergies").value(new ArrayList<>(List.of("nillacilan"))));
	}

	@Test
	public void getPersonsAndFirestationFromAddress_shouldReturnNoContent() throws Exception {
		mockMvc.perform(get("/fire")
				.param("address", "155 Country Lane")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	public void getHouseholdsCoveredByFirestations_shouldReturnOk() throws Exception {
		mockMvc.perform(get("/flood/stations")
				.param("stations", "4,1,3")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk()).andExpect(jsonPath("$.['1']").exists())
			.andExpect(jsonPath("$.['1'].['644 Gershwin Cir']").exists())
			.andExpect(jsonPath("$.['1'].['644 Gershwin Cir'].householdMembers.[0].firstName").value("Peter"))
			.andExpect(jsonPath("$.['1'].['644 Gershwin Cir'].householdMembers.[0].lastName").value("Duncan"))
			.andExpect(jsonPath("$.['1'].['644 Gershwin Cir'].householdMembers.[0].phone").value("841-874-6512"))
			.andExpect(jsonPath("$.['1'].['644 Gershwin Cir'].householdMembers.[0].age").value(22))
			.andExpect(jsonPath("$.['1'].['644 Gershwin Cir'].householdMembers.[0].medications").value(new ArrayList<>()))
			.andExpect(jsonPath("$.['1'].['644 Gershwin Cir'].householdMembers.[0].allergies").value(new ArrayList<>(List.of("shellfish"))));
	}

	@Test
	public void getHouseholdsCoveredByFirestations_shouldReturnNoContent() throws Exception {
		mockMvc.perform(get("/flood/stations")
				.param("stations", "42")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	public void getPersonsInfoByFirstNameAndLastName_shouldReturnOk() throws Exception {
		MultiValueMap<String, String> firstNameAndLastNameParams = new LinkedMultiValueMap<>();
		firstNameAndLastNameParams.add("firstName", "Sophia");
		firstNameAndLastNameParams.add("lastName", "Zemicks");

		mockMvc.perform(get("/personInfo")
				.params(firstNameAndLastNameParams)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.[0].firstName").value("Sophia"))
			.andExpect(jsonPath("$.[0].lastName").value("Zemicks"))
			.andExpect(jsonPath("$.[0].address").value("892 Downing Ct"))
			.andExpect(jsonPath("$.[0].email").value("soph@email.com"))
			.andExpect(jsonPath("$.[0].age").value(35))
			.andExpect(jsonPath("$.[0].medications").value(new ArrayList<>(List.of("aznol:60mg", "hydrapermazol:900mg", "pharmacol:5000mg", "terazine:500mg"))))
			.andExpect(jsonPath("$.[0].allergies").value(new ArrayList<>(List.of("peanut", "shellfish", "aznol"))));
	}

	@Test
	public void getPersonsInfoByFirstNameAndLastName_shouldReturnNoContent() throws Exception {
		MultiValueMap<String, String> firstNameAndLastNameParams = new LinkedMultiValueMap<>();
		firstNameAndLastNameParams.add("firstName", "Zaphod");
		firstNameAndLastNameParams.add("lastName", "Beeblebrox");

		mockMvc.perform(get("/personInfo")
				.params(firstNameAndLastNameParams)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	public void getEmailsFromCityResidents_shouldReturnOk() throws Exception {
		mockMvc.perform(get("/communityEmail")
				.param("city", "Culver")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.[0]").value("jaboyd@email.com"))
			.andExpect(jsonPath("$.[1]").value("drk@email.com"))
			.andExpect(jsonPath("$.[2]").value("tenz@email.com"));
	}

	@Test
	public void getEmailsFromCityResidents_shouldReturnNoContent() throws Exception {
		mockMvc.perform(get("/communityEmail")
				.param("city", "Cottington")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}
}