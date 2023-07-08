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
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.service.FirestationService;
import com.safetynet.util.InstanceBuilder;

@WebMvcTest(controllers = FirestationController.class)
public class FirestationControllerTest {

	private List<Firestation> firestationsResponseList = new ArrayList<>(Arrays.asList(
		InstanceBuilder.createFirestation(Long.valueOf(23), "93 Diagon Alley"),
		InstanceBuilder.createFirestation(Long.valueOf(24), "42 Grimmauld Pl")
	));
	private Firestation firestationResponse = InstanceBuilder.createFirestation(Long.valueOf(23), "4 Privet Dr");

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FirestationService firestationService;

	@Test
	public void getFirestationsList_shouldReturnOk() throws Exception {
		when(firestationService.getFirestationsList())
			.thenReturn(firestationsResponseList);
		
		mockMvc.perform(get("/firestations").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.[*].station").isNotEmpty())
			.andExpect(jsonPath("$.[*].address").isNotEmpty());
	}

	@Test
	public void getFirestationsList_shouldReturnNoContent() throws Exception {
		when(firestationService.getFirestationsList())
			.thenReturn(new ArrayList<>());
		
		mockMvc.perform(get("/firestations").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	public void getFirestationsByAddress_shouldReturnOk() throws Exception {
		when(firestationService.getFirestationsByAddress(any(String.class)))
			.thenReturn(firestationsResponseList);

		mockMvc.perform(get("/firestation/{address}", "4 Privet Dr").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].station").value(23))
			.andExpect(jsonPath("$[0].address").value("93 Diagon Alley"))
			.andExpect(jsonPath("$[1].station").value(24))
			.andExpect(jsonPath("$[1].address").value("42 Grimmauld Pl"));
	}

	@Test
	public void getFirestationsByAddress_shouldReturnNoContent() throws Exception {
		when(firestationService.getFirestationsByAddress(any(String.class)))
			.thenReturn(new ArrayList<>());
		
		mockMvc.perform(get("/firestation/{address}", "155 Country Lane").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	public void postFirestation_shouldReturnOk() throws Exception {
		when(firestationService.addFirestation(any(Firestation.class)))
			.thenReturn(firestationResponse);
		
		mockMvc.perform(post("/firestation")
				.content(objectMapper.writeValueAsString(firestationResponse))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.station").value(23))
			.andExpect(jsonPath("$.address").value("4 Privet Dr"));
	}

	@Test
	public void postFirestation_shouldReturnNoContent() throws Exception {
		when(firestationService.addFirestation(any(Firestation.class)))
			.thenReturn(null);
		
		mockMvc.perform(post("/firestation")
				.content(objectMapper.writeValueAsString(firestationResponse))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	public void postFirestation_shouldReturnBadRequest() throws Exception {
		Firestation firestationBadRequest = InstanceBuilder.createFirestation(Long.valueOf(0), "");

		when(firestationService.addFirestation(any(Firestation.class)))
				.thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

		mockMvc.perform(post("/firestation")
				.content(objectMapper.writeValueAsString(firestationBadRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}

	@Test
	public void putFirestation_shouldReturnOk() throws Exception {
		List<Firestation> firestationResponseListOfOneElement = new ArrayList<>(Arrays.asList(firestationResponse));

		when(firestationService.updateFirestationNumber(any(Long.class), any(String.class), any(Long.class)))
				.thenReturn(firestationResponseListOfOneElement);

		mockMvc.perform(put("/firestation/{station}-{address}", Long.valueOf(22), "4 Privet Dr")
				.content(objectMapper.writeValueAsString(23))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
			.andExpect(jsonPath("$[0].station").value(23))
			.andExpect(jsonPath("$[0].address").value("4 Privet Dr"));
	}

	@Test
	public void putFirestation_shouldReturnNoContent() throws Exception {
		when(firestationService.updateFirestationNumber(any(Long.class), any(String.class), any(Long.class)))
			.thenReturn(null);
		
		mockMvc.perform(put("/firestation/{station}-{address}", Long.valueOf(23), "4 Privet Dr")
				.content(objectMapper.writeValueAsString(1))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	public void deleteFirestation_shouldReturnNoContent() throws Exception {
		mockMvc.perform(delete("/firestation/{station}-{address}", Long.valueOf(1), "4 Privet Dr"))
			.andExpect(status().isNoContent());
	}
}
