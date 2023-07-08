package com.safetynet.alerts.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.service.MedicalRecordService;
import com.safetynet.util.InstanceBuilder;

@WebMvcTest(controllers = MedicalRecordController.class)
public class MedicalRecordControllerTest {

	private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	private LocalDate birthdateResponse = LocalDate.parse("06/11/1959", dateTimeFormatter);
	private List<String> medicationsResponse = new ArrayList<>(List.of("paracetamol:4000mg", "hydrocodone:4mg"));
	private List<String> allergiesResponse = new ArrayList<>(List.of());
	private MedicalRecord medicalRecordResponse = InstanceBuilder.createMedicalRecord("Gregory", "House", birthdateResponse, medicationsResponse, allergiesResponse);

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MedicalRecordService medicalRecordService;

	@Test
	public void getMedicalRecordsList_shouldReturnOk() throws Exception {
		List<MedicalRecord> medicalRecordResponseList = new ArrayList<>(Arrays.asList(
			InstanceBuilder.createMedicalRecord("Gregory", "House", LocalDate.parse("06/11/1959", dateTimeFormatter), List.of("paracetamol:4000mg", "hydrocodone:4mg"), List.of()),
			InstanceBuilder.createMedicalRecord("Robert", "Chase", LocalDate.parse("01/01/1975", dateTimeFormatter), List.of(), List.of("strawberry"))
		));

		when(medicalRecordService.getMedicalRecordsList())
			.thenReturn(medicalRecordResponseList);

		mockMvc.perform(get("/medicalRecords").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
			.andExpect(jsonPath("$.[*].firstName").isNotEmpty())
			.andExpect(jsonPath("$.[*].lastName").isNotEmpty())
			.andExpect(jsonPath("$.[*].birthdate").isNotEmpty())
			.andExpect(jsonPath("$.[*].medications").isNotEmpty())
			.andExpect(jsonPath("$.[*].allergies").isNotEmpty());
	}

	@Test
	public void getMedicalRecordList_shouldReturnNoContent() throws Exception {
		when(medicalRecordService.getMedicalRecordsList())
			.thenReturn(new ArrayList<>());
	
		mockMvc.perform(get("/medicalRecords").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	public void getMedicalRecordByFirstNameAndLastName_shouldReturnOk() throws Exception {
		when(medicalRecordService.getMedicalRecordByFirstNameAndLastName(any(String.class), any(String.class)))
			.thenReturn(medicalRecordResponse);
		
		mockMvc.perform(get("/medicalRecord/{firstName}/{lastName}", "Gregory", "House").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.firstName").value("Gregory"))
			.andExpect(jsonPath("$.lastName").value("House"))
			.andExpect(jsonPath("$.birthdate").value(birthdateResponse.toString()))
			.andExpect(jsonPath("$.medications").value(medicationsResponse))
			.andExpect(jsonPath("$.allergies").value(allergiesResponse));
	}

	@Test
	public void getMedicalRecordByFirstNameAndLastName_shouldReturnNoContent() throws Exception {
		when(medicalRecordService.getMedicalRecordByFirstNameAndLastName(any(String.class), any(String.class)))
			.thenReturn(null);
		
		mockMvc.perform(get("/medicalRecord/{firstName}/{lastName}", "Michael", "Scott").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	public void postMedicalRecord_shouldReturnOk() throws Exception {
		objectMapper.registerModule(new JavaTimeModule());

		when(medicalRecordService.addMedicalRecord(any(MedicalRecord.class)))
			.thenReturn(medicalRecordResponse);

		mockMvc.perform(post("/medicalRecord")
				.content(objectMapper.writeValueAsString(medicalRecordResponse))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.firstName").value("Gregory"))
			.andExpect(jsonPath("$.lastName").value("House"))
			.andExpect(jsonPath("$.birthdate").value(birthdateResponse.toString()))
			.andExpect(jsonPath("$.medications").value(medicationsResponse))
			.andExpect(jsonPath("$.allergies").value(allergiesResponse));
	}

	@Test
	public void postMedicalRecord_shouldReturnNoContent() throws Exception {
		objectMapper.registerModule(new JavaTimeModule());

		when(medicalRecordService.addMedicalRecord(any(MedicalRecord.class)))
			.thenReturn(null);

		mockMvc.perform(post("/medicalRecord")
				.content(objectMapper.writeValueAsString(medicalRecordResponse))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	public void postMedicalRecord_shouldReturnBadRequest() throws Exception {
		MedicalRecord mecidalRecordBadRequest = new MedicalRecord();

		when(medicalRecordService.addMedicalRecord(any(MedicalRecord.class)))
			.thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

		mockMvc.perform(post("/medicalRecord")
				.content(objectMapper.writeValueAsString(mecidalRecordBadRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}

	@Test
	public void putMedicalContent_shouldReturnOk() throws Exception {
		objectMapper.registerModule(new JavaTimeModule());

		when(medicalRecordService.updateMedicalRecord(any(String.class), any(String.class), any(MedicalRecord.class)))
			.thenReturn(medicalRecordResponse);

		mockMvc.perform(put("/medicalRecord/{firstName}/{lastName}", "Gregory", "House")
				.content(objectMapper.writeValueAsString(medicalRecordResponse))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
			.andExpect(jsonPath("$.firstName").value("Gregory"))
			.andExpect(jsonPath("$.lastName").value("House"))
			.andExpect(jsonPath("$.birthdate").value(birthdateResponse.toString()))
			.andExpect(jsonPath("$.medications").value(medicationsResponse))
			.andExpect(jsonPath("$.allergies").value(allergiesResponse));
	}

	@Test
	public void putMedicalContent_shouldReturnNoContent() throws Exception {
		objectMapper.registerModule(new JavaTimeModule());

		when(medicalRecordService.updateMedicalRecord(any(String.class), any(String.class), any(MedicalRecord.class)))
			.thenReturn(null);

		mockMvc.perform(put("/medicalRecord/{firstName}/{lastName}", "Gregory", "House")
				.content(objectMapper.writeValueAsString(medicalRecordResponse))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	public void deleteMedicalRecord_shouldReturnNoContent() throws Exception {
		mockMvc.perform(delete("/medicalRecord/{firstName}/{lastName}", "Gregory", "House"))
			.andExpect(status().isNoContent());
	}
}
