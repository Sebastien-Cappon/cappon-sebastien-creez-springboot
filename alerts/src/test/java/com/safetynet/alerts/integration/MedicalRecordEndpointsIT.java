package com.safetynet.alerts.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.util.InstanceBuilder;

@SpringBootTest
@AutoConfigureMockMvc
public class MedicalRecordEndpointsIT {

	DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void getMedicalRecordsList_shouldReturnOk() throws Exception {
		mockMvc.perform(get("/medicalRecords").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.[0].firstName").value("John"))
			.andExpect(jsonPath("$.[0].lastName").value("Boyd"))
			.andExpect(jsonPath("$.[0].birthdate").value(LocalDate.parse("03/06/1984", dateTimeFormatter).toString()))
			.andExpect(jsonPath("$.[0].medications").value(new ArrayList<>(List.of("aznol:350mg", "hydrapermazol:100mg"))))
			.andExpect(jsonPath("$.[0].allergies").value(new ArrayList<>(List.of("nillacilan"))));
	}

	@Test
	public void getMedicalRecordByFirstNameAndLastName_shouldReturnOk() throws Exception {
		mockMvc.perform(get("/medicalRecord/{firstName}-{lastName}", "Lily", "Cooper").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.firstName").value("Lily"))
			.andExpect(jsonPath("$.lastName").value("Cooper"))
			.andExpect(jsonPath("$.birthdate").value(LocalDate.parse("03/06/1994", dateTimeFormatter).toString()))
			.andExpect(jsonPath("$.medications").value(new ArrayList<>()))
			.andExpect(jsonPath("$.allergies").value(new ArrayList<>()));
	}

	@Test
	public void getMedicalRecordByFirstNameAndLastName_shouldReturnNoContent() throws Exception {
		mockMvc.perform(get("/medicalRecord/{firstName}-{lastName}", "John", "Doe").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	public void postMedicalRecord_shouldReturnOk() throws Exception {
		MedicalRecord requestBody = InstanceBuilder.createMedicalRecord("David", "Pliskin", LocalDate.parse("01/01/1972", dateTimeFormatter), new ArrayList<String>(List.of("diazepam:40mg")), new ArrayList<String>(List.of()));
		objectMapper.registerModule(new JavaTimeModule());

		mockMvc.perform(post("/medicalRecord")
				.content(objectMapper.writeValueAsString(requestBody))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.firstName").value("David"))
			.andExpect(jsonPath("$.lastName").value("Pliskin"))
			.andExpect(jsonPath("$.birthdate").value(LocalDate.parse("01/01/1972", dateTimeFormatter).toString()))
			.andExpect(jsonPath("$.medications").value(new ArrayList<>(List.of("diazepam:40mg"))))
			.andExpect(jsonPath("$.allergies").value(new ArrayList<>()));
	}

	@Test
	public void postMedicalRecord_shouldReturnBadRequest() throws Exception {
		MedicalRecord requestBody = InstanceBuilder.createMedicalRecord(null, null, null, new ArrayList<String>(List.of("diazepam:40mg")), new ArrayList<String>(List.of()));

		mockMvc.perform(post("/medicalRecord")
				.content(objectMapper.writeValueAsString(requestBody))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}

	@Test
	public void postMedicalRecord_shouldReturnNoContent() throws Exception {
		MedicalRecord requestBody = InstanceBuilder.createMedicalRecord("Lily", "Cooper", LocalDate.parse("03/06/1994", dateTimeFormatter), new ArrayList<String>(), new ArrayList<String>());
		objectMapper.registerModule(new JavaTimeModule());

		mockMvc.perform(post("/medicalRecord")
				.content(objectMapper.writeValueAsString(requestBody))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	public void putMedicalContent_shouldReturnOk() throws Exception {
		MedicalRecord requestBody = InstanceBuilder.createMedicalRecord("Tenley", "Boyd", LocalDate.parse("06/03/1949", dateTimeFormatter), new ArrayList<String>(List.of("aznol:350mg", "hydrapermazol:100mg")), new ArrayList<String>(List.of("nillacilan")));
		objectMapper.registerModule(new JavaTimeModule());

		mockMvc.perform(put("/medicalRecord/{firstName}-{lastName}", "Tenley", "Boyd")
				.content(objectMapper.writeValueAsString(requestBody))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.firstName").value("Tenley")).andExpect(jsonPath("$.lastName").value("Boyd"))
			.andExpect(jsonPath("$.birthdate").value(LocalDate.parse("06/03/1949", dateTimeFormatter).toString()))
			.andExpect(jsonPath("$.medications").value(new ArrayList<>(List.of("aznol:350mg", "hydrapermazol:100mg"))))
			.andExpect(jsonPath("$.allergies").value(new ArrayList<>(List.of("nillacilan"))));
	}

	@Test
	public void putMedicalContent_shouldReturnNoContent() throws Exception {
		MedicalRecord requestBody = InstanceBuilder.createMedicalRecord("John", "Boyd", null, null, null);

		mockMvc.perform(put("/medicalRecord/{firstName}-{lastName}", "John", "Boyd")
				.content(objectMapper.writeValueAsString(requestBody))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	public void deleteMedicalRecord_shouldReturnNoContent() throws Exception {
		mockMvc.perform(delete("/medicalRecord/{firstName}-{lastName}", "Clive", "Ferguson"))
			.andExpect(status().isNoContent());
	}
}
