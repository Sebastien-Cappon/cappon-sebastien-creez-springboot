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
import com.safetynet.alerts.model.Firestation;
import com.safetynet.util.InstanceBuilder;

@SpringBootTest
@AutoConfigureMockMvc
public class FirestationEndpointsIT {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void getFirestationsList_shouldReturnOk() throws Exception {
		mockMvc.perform(get("/firestations").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.[0].station").value(3))
			.andExpect(jsonPath("$.[0].address").value("1509 Culver St"));
	}

	@Test
	public void getFirestationsByAddress_shouldReturnOk() throws Exception {
		mockMvc.perform(get("/firestation/{address}", "112 Steppes Pl").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].station").value(3))
			.andExpect(jsonPath("$[0].address").value("112 Steppes Pl"))
			.andExpect(jsonPath("$[1].station").value(4))
			.andExpect(jsonPath("$[1].address").value("112 Steppes Pl"));
	}

	@Test
	public void getFirestationsByAddress_shouldReturnNoContent() throws Exception {
		mockMvc.perform(get("/firestation/{address}", "155 Country Lane").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	public void postFirestation_shouldReturnOk() throws Exception {
		Firestation requestBody = InstanceBuilder.createFirestation(Long.valueOf(42), "155 Country Lane");

		mockMvc.perform(post("/firestation")
				.content(objectMapper.writeValueAsString(requestBody))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.station").value(42))
			.andExpect(jsonPath("$.address").value("155 Country Lane"));
	}

	@Test
	public void postFirestation_shouldReturnNoContent() throws Exception {
		Firestation requestBody = InstanceBuilder.createFirestation(Long.valueOf(3), "1509 Culver St");

		mockMvc.perform(post("/firestation")
				.content(objectMapper.writeValueAsString(requestBody))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	public void postFirestation_shouldReturnBadReques() throws Exception {
		Firestation requestBody = InstanceBuilder.createFirestation(null, "1509 Culver St");

		mockMvc.perform(post("/firestation")
				.content(objectMapper.writeValueAsString(requestBody))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}

	@Test
	public void putFirestation_shouldReturnOk() throws Exception {
		mockMvc.perform(put("/firestation/{station}-{address}", Long.valueOf(3), "112 Steppes Pl")
				.content(objectMapper.writeValueAsString(1))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].station").value(1))
			.andExpect(jsonPath("$[0].address").value("112 Steppes Pl"));
	}

	@Test
	public void putFirestation_shouldReturnNoContent() throws Exception {
		mockMvc.perform(put("/firestation/{station}-{address}", Long.valueOf(4), "112 Steppes Pl")
				.content(objectMapper.writeValueAsString(4))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	public void putNonExistingFirestation_shouldReturnNoContent() throws Exception {
		mockMvc.perform(put("/firestation/{station}-{address}", Long.valueOf(7), "777 Lucky Nb")
				.content(objectMapper.writeValueAsString(77))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	public void deleteFirestation_shouldReturnNoContent() throws Exception {
		mockMvc.perform(delete("/firestation/{station}-{address}", Long.valueOf(2), "951 LoneTree Rd"))
			.andExpect(status().isNoContent());
	}
}
