package com.safetynet.alerts.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.Person;
import com.safetynet.util.InstanceBuilder;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonEndpointsIT {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testGetPersons_shouldReturnOk() throws Exception {
		mockMvc.perform(get("/persons").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].firstName").value("John"))
			.andExpect(jsonPath("$[0].lastName").value("Boyd"))
			.andExpect(jsonPath("$[0].address").value("1509 Culver St"))
			.andExpect(jsonPath("$[0].city").value("Culver"))
			.andExpect(jsonPath("$[0].zip").value("97451"))
			.andExpect(jsonPath("$[0].phone").value("841-874-6512"))
			.andExpect(jsonPath("$[0].email").value("jaboyd@email.com"));
	}

	@Test
	public void getPersonByFirstNameAndLastName_shouldReturnOk() throws Exception {
		mockMvc.perform(get("/person/{firstName}-{lastName}", "Jacob", "Boyd").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.firstName").value("Jacob"))
			.andExpect(jsonPath("$.lastName").value("Boyd"))
			.andExpect(jsonPath("$.address").value("1509 Culver St"))
			.andExpect(jsonPath("$.city").value("Culver"))
			.andExpect(jsonPath("$.zip").value("97451"))
			.andExpect(jsonPath("$.phone").value("841-874-6513"))
			.andExpect(jsonPath("$.email").value("drk@email.com"));
	}

	@Test
	public void getPersonByFirstNameAndLastName_shouldReturnNoContent() throws Exception {
		mockMvc.perform(get("/person/{firstName}-{lastName}", "Jane", "Doe").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	public void postPerson_shouldReturnOk() throws Exception {
		Person requestBody = InstanceBuilder.createPerson("Arthur", "Dent", "155 Country Lane", "Cottington", "01100", "155-042-0101", "solong@h2g2.com");

		mockMvc.perform(post("/person")
				.content(objectMapper.writeValueAsString(requestBody))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.firstName").value("Arthur"))
			.andExpect(jsonPath("$.lastName").value("Dent"))
			.andExpect(jsonPath("$.address").value("155 Country Lane"))
			.andExpect(jsonPath("$.city").value("Cottington"))
			.andExpect(jsonPath("$.zip").value("01100"))
			.andExpect(jsonPath("$.phone").value("155-042-0101"))
			.andExpect(jsonPath("$.email").value("solong@h2g2.com"));
	}

	@Test
	public void postEmptyPerson_shouldReturnOk() throws Exception {
		Person requestBody = InstanceBuilder.createPerson("Tricia", "McMillan", null, null, null, null, null);

		mockMvc.perform(post("/person")
				.content(objectMapper.writeValueAsString(requestBody))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.firstName").value("Tricia"))
			.andExpect(jsonPath("$.lastName").value("McMillan"))
			.andExpect(jsonPath("$.address").value(""))
			.andExpect(jsonPath("$.city").value(""))
			.andExpect(jsonPath("$.zip").value(""))
			.andExpect(jsonPath("$.phone").value(""))
			.andExpect(jsonPath("$.email").value(""));
	}

	@Test
	public void postEmptyPerson_shouldReturnBadRequest() throws Exception {
		Person requestBody = InstanceBuilder.createPerson(null, null, "155 Country Lane", "Cottington", "01100", "101-024-0551", "trillian@h2g2.com");

		mockMvc.perform(post("/person")
				.content(objectMapper.writeValueAsString(requestBody))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}

	@Test
	public void postPerson_shouldReturnNoContent() throws Exception {
		Person requestBody = InstanceBuilder.createPerson("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");

		mockMvc.perform(post("/person")
				.content(objectMapper.writeValueAsString(requestBody))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	public void putPerson_shouldReturnOk() throws Exception {
		Person requestBody = InstanceBuilder.createPerson("John", "Boyd", "9051 Revluc Ts", "Revluc", "15479", "215-647-8148", "joboyd@email.com");

		mockMvc.perform(put("/person/{firstName}-{lastName}", "John", "Boyd")
				.content(objectMapper.writeValueAsString(requestBody))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.firstName").value("John"))
			.andExpect(jsonPath("$.lastName").value("Boyd"))
			.andExpect(jsonPath("$.address").value("9051 Revluc Ts"))
			.andExpect(jsonPath("$.city").value("Revluc"))
			.andExpect(jsonPath("$.zip").value("15479"))
			.andExpect(jsonPath("$.phone").value("215-647-8148"))
			.andExpect(jsonPath("$.email").value("joboyd@email.com"));
	}

	@Test
	public void putPerson_shouldReturnNoContent() throws Exception {
		Person requestBody = InstanceBuilder.createPerson("John", "Boyd", null, null, null, null, null);

		mockMvc.perform(put("/person/{firstName}-{lastName}", "John", "Boyd")
				.content(objectMapper.writeValueAsString(requestBody))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	public void deletePerson_shouldReturnNoContent() throws Exception {
		mockMvc.perform(delete("/person/{firstName}-{lastName}", "Eric", "Cadigan"))
			.andExpect(status().isNoContent());
	}
}
