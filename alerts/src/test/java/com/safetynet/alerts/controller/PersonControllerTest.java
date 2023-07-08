package com.safetynet.alerts.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonService;
import com.safetynet.util.InstanceBuilder;

@WebMvcTest(controllers = PersonController.class)
public class PersonControllerTest {

	private Person PersonResponse = InstanceBuilder.createPerson("Micheal", "Scott", "126 Kellum Court", "Scranton", "PA 18510", "(212)-555-2102", "michael.scott@dundermifflin.com");

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PersonService personService;

	@Test
	public void getPersonsList_shouldReturnOk() throws Exception {
		List<Person> PersonResponseList = new ArrayList<>(Arrays.asList(
			InstanceBuilder.createPerson("Micheal", "Scott", "126 Kellum Court", "Scranton", "PA 18510", "(212)-555-2102", "michael.scott@dundermifflin.com"),
			InstanceBuilder.createPerson("Dwigth", "Schrute", "Schrute Farms Rural Rt. 6", "Honesdale", "PA 18431", "(717)-555-0177", "dwight.schrute@dundermifflin.com")));

		when(personService.getPersonsList())
			.thenReturn(PersonResponseList);

		mockMvc.perform(get("/persons").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.[*].firstName").isNotEmpty())
			.andExpect(jsonPath("$.[*].lastName").isNotEmpty())
			.andExpect(jsonPath("$.[*].address").isNotEmpty())
			.andExpect(jsonPath("$.[*].city").isNotEmpty())
			.andExpect(jsonPath("$.[*].zip").isNotEmpty())
			.andExpect(jsonPath("$.[*].phone").isNotEmpty())
			.andExpect(jsonPath("$.[*].email").isNotEmpty());
	}

	@Test
	public void getPersonsList_shouldReturnNoContent() throws Exception {
		when(personService.getPersonsList())
			.thenReturn(new ArrayList<>());
	
		mockMvc.perform(get("/persons").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	public void getPersonByFirstNameAndLastName_shouldReturnOk() throws Exception {
		when(personService.getPersonByFirstNameAndLastName(any(String.class), any(String.class)))
			.thenReturn(PersonResponse);
		
		mockMvc.perform(get("/person/{firstName}-{lastName}", "Michael", "Scott").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.firstName").value("Micheal"))
			.andExpect(jsonPath("$.lastName").value("Scott"))
			.andExpect(jsonPath("$.address").value("126 Kellum Court"))
			.andExpect(jsonPath("$.city").value("Scranton"))
			.andExpect(jsonPath("$.zip").value("PA 18510"))
			.andExpect(jsonPath("$.phone").value("(212)-555-2102"))
			.andExpect(jsonPath("$.email").value("michael.scott@dundermifflin.com"));
	}

	@Test
	public void getPersonByFirstNameAndLastName_shouldReturnNoContent() throws Exception {
		when(personService.getPersonByFirstNameAndLastName(any(String.class), any(String.class)))
			.thenReturn(null);
		
		mockMvc.perform(get("/person/{firstName}-{lastName}", "Sheldon", "Cooper").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	public void postPerson_shouldReturnOk() throws Exception {
		when(personService.addPerson(any(Person.class)))
			.thenReturn(PersonResponse);
		
		mockMvc.perform(post("/person")
				.content(objectMapper.writeValueAsString(PersonResponse))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.firstName").value("Micheal"))
			.andExpect(jsonPath("$.lastName").value("Scott"))
			.andExpect(jsonPath("$.address").value("126 Kellum Court"))
			.andExpect(jsonPath("$.city").value("Scranton"))
			.andExpect(jsonPath("$.zip").value("PA 18510"))
			.andExpect(jsonPath("$.phone").value("(212)-555-2102"))
			.andExpect(jsonPath("$.email").value("michael.scott@dundermifflin.com"));
	}

	@Test
	public void postPerson_shouldReturnNoContent() throws Exception {
		when(personService.addPerson(any(Person.class)))
			.thenReturn(null);
		
		mockMvc.perform(post("/person")
				.content(objectMapper.writeValueAsString(PersonResponse))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	public void postPerson_shouldReturnBadRequest() throws Exception {
		Person personBadRequest = new Person();

		when(personService.addPerson(any(Person.class)))
			.thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

		mockMvc.perform(post("/person")
				.content(objectMapper.writeValueAsString(personBadRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}

	@Test
	public void putPerson_shouldReturnOk() throws Exception {
		when(personService.updatePerson(any(String.class), any(String.class), any(Person.class)))
			.thenReturn(PersonResponse);
		
		mockMvc.perform(put("/person/{firstName}-{lastName}", "Michael", "Scott")
				.content(objectMapper.writeValueAsString(PersonResponse))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.firstName").value("Micheal"))
			.andExpect(jsonPath("$.lastName").value("Scott"))
			.andExpect(jsonPath("$.address").value("126 Kellum Court"))
			.andExpect(jsonPath("$.city").value("Scranton"))
			.andExpect(jsonPath("$.zip").value("PA 18510"))
			.andExpect(jsonPath("$.phone").value("(212)-555-2102"))
			.andExpect(jsonPath("$.email").value("michael.scott@dundermifflin.com"));
	}

	@Test
	public void putPerson_shouldReturnNoContent() throws Exception {
		when(personService.updatePerson(any(String.class), any(String.class), any(Person.class)))
			.thenReturn(null);
		
		mockMvc.perform(put("/person/{firstName}-{lastName}", "Michael", "Scott")
				.content(objectMapper.writeValueAsString(PersonResponse))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	public void deletePerson_shouldReturnNoContent() throws Exception {
		mockMvc.perform(delete("/person/{firstName}-{lastName}", "Michael", "Scott"))
			.andExpect(status().isNoContent());
	}
}
