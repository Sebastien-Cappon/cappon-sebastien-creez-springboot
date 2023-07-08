package com.safetynet.alerts.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.safetynet.alerts.dto.Child;
import com.safetynet.alerts.dto.FirestationScope;
import com.safetynet.alerts.dto.Household;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.CrossModelService;
import com.safetynet.util.DtoInstanceBuilder;
import com.safetynet.util.InstanceBuilder;

@WebMvcTest(controllers = CrossModelController.class)
public class CrossModelControllerTest {

	private List<Person> personsList = new ArrayList<>(Arrays.asList(
		InstanceBuilder.createPerson("Micheal", "Scott", "126 Kellum Court", "Scranton", "PA 18510", "(212)-555-2102", "michael.scott@dundermifflin.com"),
		InstanceBuilder.createPerson("Dwigth", "Schrute", "Schrute Farms Rural Rt. 6", "Honesdale", "PA 18431", "(717)-555-0177", "dwight.schrute@dundermifflin.com"),
		InstanceBuilder.createPerson("Jim", "Halpert", "Linden Ave.", "Scranton", "PA 18510", "(212)-555-2098", "jim.halpert@dundermifflin.com")
	));
	private Household householdResponse = DtoInstanceBuilder .createHousehold(new ArrayList<Long>(List.of(Long.valueOf(2), Long.valueOf(3))), personsList);

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CrossModelService crossModelService;

	@Test
	public void getPersonsCoveredByFirestation_shouldReturnOk() throws Exception {
		FirestationScope firestationScopeResponse = DtoInstanceBuilder.createFirestationScope(2, 1, personsList);

		when(crossModelService.getPersonsCoveredByFirestation(any(Long.class)))
			.thenReturn(firestationScopeResponse);

		mockMvc.perform(get("/firestation").param("stationNumber", "1").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.adultQuantity").value(2))
			.andExpect(jsonPath("$.childQuantity").value(1))
			.andExpect(jsonPath("$.personsCoveredByFirestation.[*].firstName").isNotEmpty())
			.andExpect(jsonPath("$.personsCoveredByFirestation.[*].lastName").isNotEmpty())
			.andExpect(jsonPath("$.personsCoveredByFirestation.[*].address").isNotEmpty())
			.andExpect(jsonPath("$.personsCoveredByFirestation.[*].phone").isNotEmpty());
	}

	@Test
	public void getPersonsCoveredByFirestation_shouldReturnNoContent() throws Exception {
		when(crossModelService.getPersonsCoveredByFirestation(any(Long.class)))
			.thenReturn(DtoInstanceBuilder.createFirestationScope(0, 0, new ArrayList<>()));
		
		mockMvc.perform(get("/firestation").param("stationNumber", "2").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	public void getChildrenFromAddress_shouldReturnOk() throws Exception {
		List<Child> childResponseList = new ArrayList<>(Arrays.asList(
			DtoInstanceBuilder.createChildViewed("Cecelia", "Halpert", 3, personsList),
			DtoInstanceBuilder.createChildViewed("Phillip", "Halpert", 2, personsList)
		));

		when(crossModelService.getChildrenFromAddress(any(String.class)))
			.thenReturn(childResponseList);

		mockMvc.perform(get("/childAlert").param("address", "Linden Ave.").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk()).andExpect(jsonPath("$.[*].firstName").isNotEmpty())
			.andExpect(jsonPath("$.[*].lastName").isNotEmpty()).andExpect(jsonPath("$.[*].age").isNotEmpty())
			.andExpect(jsonPath("$.[*].householdMembers.[*].firstName").isNotEmpty())
			.andExpect(jsonPath("$.[*].householdMembers.[*].lastName").isNotEmpty())
			.andExpect(jsonPath("$.[*].householdMembers.[*].age").isNotEmpty());
	}

	@Test
	public void getChildrenFromAddress_shouldReturnNoContent() throws Exception {
		when(crossModelService.getChildrenFromAddress(any(String.class)))
			.thenReturn(new ArrayList<>());
		
		mockMvc.perform(get("/childAlert").param("address", "1725 Slough Ave.").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	public void getPhoneNumberFromPersonsCoveredByFirestation_shouldReturn() throws Exception {
		List<String> phoneNumbersResponseList = new ArrayList<String>(List.of("(212)-555-2102", "(212)-555-2098"));

		when(crossModelService.getPhoneNumberFromPersonsCoveredByFirestation(any(Long.class)))
			.thenReturn(phoneNumbersResponseList);

		mockMvc.perform(get("/phoneAlert").param("firestation", "1").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.[0]").value("(212)-555-2102"))
			.andExpect(jsonPath("$.[1]").value("(212)-555-2098"));
	}

	@Test
	public void getPhoneNumberFromPersonsCoveredByFirestation_shouldNoContent() throws Exception {
		when(crossModelService.getPhoneNumberFromPersonsCoveredByFirestation(any(Long.class)))
			.thenReturn(new ArrayList<>());
		
		mockMvc.perform(get("/phoneAlert").param("firestation", "2").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	public void getPersonsAndFirestationFromAddress_shouldReturnOk() throws Exception {
		when(crossModelService.getPersonsAndFirestationFromAddress(any(String.class)))
			.thenReturn(householdResponse);
		
		mockMvc.perform(get("/fire").param("address", "Linden Ave.").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.firestationsNumber.[*]").isNotEmpty())
			.andExpect(jsonPath("$.householdMembers.[*].firstName").isNotEmpty())
			.andExpect(jsonPath("$.householdMembers.[*].lastName").isNotEmpty())
			.andExpect(jsonPath("$.householdMembers.[*].phone").isNotEmpty())
			.andExpect(jsonPath("$.householdMembers.[*].age").isNotEmpty())
			.andExpect(jsonPath("$.householdMembers.[*].medications").isNotEmpty())
			.andExpect(jsonPath("$.householdMembers.[*].allergies").isNotEmpty());
	}

	@Test
	public void getPersonsAndFirestationFromAddress_shouldReturnNoContent() throws Exception {
		when(crossModelService.getPersonsAndFirestationFromAddress(any(String.class)))
			.thenReturn(DtoInstanceBuilder.createHousehold(new ArrayList<>(), new ArrayList<>()));
		
		mockMvc.perform(get("/fire").param("address", "1725 Slough Ave.").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	public void getHouseholdsCoveredByFirestations_shouldReturnOk() throws Exception {
		Map<String, Household> householdByAddressTestMap = new TreeMap<>();
		householdByAddressTestMap.put("Linden Ave.", householdResponse);
		Map<Long, Map<String, Household>> householdAndFirestationTestMap = new TreeMap<>();
		householdAndFirestationTestMap.put(Long.valueOf(1), householdByAddressTestMap);

		when(crossModelService.getHouseholdsCoveredByFirestations(anyList()))
			.thenReturn(householdAndFirestationTestMap);

		mockMvc.perform(get("/flood/stations").param("stations", "1").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.[*]").isNotEmpty())
			.andExpect(jsonPath("$.[*].[*]").isNotEmpty())
			.andExpect(jsonPath("$.[*].[*].householdMembers.[*].firstName").isNotEmpty())
			.andExpect(jsonPath("$.[*].[*].householdMembers.[*].lastName").isNotEmpty())
			.andExpect(jsonPath("$.[*].[*].householdMembers.[*].phone").isNotEmpty())
			.andExpect(jsonPath("$.[*].[*].householdMembers.[*].age").isNotEmpty())
			.andExpect(jsonPath("$.[*].[*].householdMembers.[*].medications").isNotEmpty())
			.andExpect(jsonPath("$.[*].[*].householdMembers.[*].allergies").isNotEmpty());
	}

	@Test
	public void getHouseholdsCoveredByFirestations_shouldReturnNoContent() throws Exception {
		when(crossModelService.getHouseholdsCoveredByFirestations(anyList()))
			.thenReturn(new TreeMap<>());
		
		mockMvc.perform(get("/flood/stations").param("stations", "2,3").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	public void getPersonsInfoByFirstNameAndLastName_shouldReturnOk() throws Exception {
		MultiValueMap<String, String> firstNameAndLastNameParams = new LinkedMultiValueMap<>();
		firstNameAndLastNameParams.add("firstName", "Michael");
		firstNameAndLastNameParams.add("lastName", "Scott");
		List<Person> personsViewedList = new ArrayList<>(Arrays.asList(
			InstanceBuilder.createPersonViewed("Michael", "Scott", "126 Kellum Court","michael.scott@dundermifflin.com", 48, new ArrayList<String>(), new ArrayList<String>())
		));

		when(crossModelService.getPersonsInfoByFirstNameAndLastName(any(String.class), any(String.class)))
			.thenReturn(personsViewedList);

		mockMvc.perform(get("/personInfo").params(firstNameAndLastNameParams).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.[0].firstName").value("Michael"))
			.andExpect(jsonPath("$.[0].lastName").value("Scott"))
			.andExpect(jsonPath("$.[0].address").value("126 Kellum Court"))
			.andExpect(jsonPath("$.[0].email").value("michael.scott@dundermifflin.com"))
			.andExpect(jsonPath("$.[0].age").value(48))
			.andExpect(jsonPath("$.[0].medications").isEmpty())
			.andExpect(jsonPath("$.[0].allergies").isEmpty());
	}

	@Test
	public void getPersonsInfoByFirstNameAndLastName_shouldReturnNoContent() throws Exception {
		MultiValueMap<String, String> firstNameAndLastNameParams = new LinkedMultiValueMap<>();
		firstNameAndLastNameParams.add("firstName", "Johnny");
		firstNameAndLastNameParams.add("lastName", "English");

		when(crossModelService.getPersonsInfoByFirstNameAndLastName(any(String.class), any(String.class)))
			.thenReturn(new ArrayList<>());

		mockMvc.perform(get("/personInfo").params(firstNameAndLastNameParams).accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	public void getEmailsFromCityResidents_shouldReturnOk() throws Exception {
		List<String> emailsResponseList = new ArrayList<String>(List.of("michael.scott@dundermifflin.com", "dwight.schrute@dundermifflin.com"));

		when(crossModelService.getEmailsFromCityResidents(any(String.class)))
			.thenReturn(emailsResponseList);

		mockMvc.perform(get("/communityEmail").param("city", "Scranton").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.[0]").value("michael.scott@dundermifflin.com"))
			.andExpect(jsonPath("$.[1]").value("dwight.schrute@dundermifflin.com"));
	}

	@Test
	public void getEmailsFromCityResidents_shouldReturnNoContent() throws Exception {
		when(crossModelService.getEmailsFromCityResidents(any(String.class)))
			.thenReturn(new ArrayList<>());
		
		mockMvc.perform(get("/communityEmail").param("city", "Scranton").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}
}
