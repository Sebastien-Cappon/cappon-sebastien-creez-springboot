package com.safetynet.alerts.controller;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.service.IMedicalRecordService;

/**
 * A class that receives requests made from the usual CRUD endpoints for the medical records.
 * 
 * @author Sébastien Cappon
 * @version 1.0
 */
@RestController
public class MedicalRecordController {
	private static final Logger logger = LoggerFactory.getLogger(MedicalRecordController.class);

	@Autowired
	private IMedicalRecordService iMedicalRecordService;

	/**
	 * A <code>GetMapping</code> method on the <code>/medicalRecords</code> URI which
	 * calls the eponymous <code>IMedicalRecordService</code> method and returns the
	 * list of all medical records with status code 200. If the result is empty, it
	 * returns status code 204.
	 * 
	 * @return <code>MedicalRecord</code> list and a status code.
	 */
	@GetMapping("/medicalRecords")
	public ResponseEntity<List<MedicalRecord>> getMedicalRecordsList() {
		List<MedicalRecord> medicalRecordList = iMedicalRecordService.getMedicalRecordsList();

		if (medicalRecordList.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(medicalRecordList, HttpStatus.OK);
	}
	
	/**
	 * A <code>GetMapping</code> method on the <code>/medicalRecord</code> URI with
	 * a first name and a last name as <code>PathVariables</code>. It calls the
	 * eponymous <code>IMedicalRecordService</code> method and returns the
	 * <code>MedicalRecord</code> asked, with status code 200. If the result is
	 * empty, it returns status code 204.
	 * 
	 * @return A <code>MedicalRecord</code> and a status code.
	 */
	@GetMapping("/medicalRecord/{firstName}-{lastName}")
	public ResponseEntity<MedicalRecord> getMedicalRecordByFirstNameAndLastName(@PathVariable("firstName") String firstName, @PathVariable("lastName") String lastName) {
		MedicalRecord medicalRecord = iMedicalRecordService.getMedicalRecordByFirstNameAndLastName(firstName, lastName);

		if (medicalRecord == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(medicalRecord, HttpStatus.OK);
	}

	/**
	 * A <code>PostMapping</code> method on the <code>/medicalRecord</code> URI which
	 * calls the eponymous <code>IMedicalRecordService</code> method and returns the
	 * <code>MedicalRecord</code> added with status code 201. If the result is null,
	 * it returns status code 204.
	 * 
	 * @throws BAD_REQUEST if first name and last name are null or blank in the request
	 *                     body.
	 * 
	 * @return A <code>MedicalRecord</code> and a status code.
	 */
	@PostMapping("/medicalRecord")
	public ResponseEntity<MedicalRecord> addMedicalRecord(@RequestBody MedicalRecord medicalRecord) throws Exception {
		if (medicalRecord.getFirstName() == null || medicalRecord.getFirstName().isBlank() || medicalRecord.getLastName() == null || medicalRecord.getLastName().isBlank()) {
			logger.warn("Requested key-values pairs <firstName> and <lastName> are missing in the request body.");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		MedicalRecord medicalRecordAdded = iMedicalRecordService.addMedicalRecord(medicalRecord);
		if (medicalRecordAdded == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{firstName}-{lastName}").buildAndExpand(medicalRecordAdded.getFirstName(), medicalRecordAdded.getLastName()).toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(location);

		return new ResponseEntity<>(medicalRecordAdded, headers, HttpStatus.CREATED);
	}
	
	/**
	 * A <code>PutMapping</code> method on the <code>/medicalRecord</code> URI with
	 * a first name and a last name as <code>PathVariables</code>. It calls the
	 * eponymous <code>IMedicalRecordService</code> method and returns the modified
	 * <code>MedicalRecord</code>, with status code 200. If the result is null, it
	 * returns status code 204.
	 * 
	 * @return A <code>MedicalRecord</code> and a status code.
	 */
	@PutMapping("/medicalRecord/{firstName}-{lastName}")
	public ResponseEntity<MedicalRecord> updateMedicalRecord(@PathVariable("firstName") String firstName, @PathVariable("lastName") String lastName, @RequestBody MedicalRecord update) {
		MedicalRecord medicalRecordUpdated = iMedicalRecordService.updateMedicalRecord(firstName, lastName, update);

		if (medicalRecordUpdated == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(medicalRecordUpdated, HttpStatus.OK);
	}
	
	/**
	 * A <code>DeleteMapping</code> method on the <code>/medicalRecord</code> URI
	 * with a first name and a last name as <code>PathVariables</code>. It calls the
	 * eponymous <code>IMedicalRecordService</code> method and returns nothing
	 * except status code 204.
	 * 
	 * @return a status code 204.
	 */
	@DeleteMapping("medicalRecord/{firstName}-{lastName}")
	public ResponseEntity<Void> deleteMedicalRecord(@PathVariable("firstName") String firstName, @PathVariable("lastName") String lastName) {
		iMedicalRecordService.deleteMedicalRecord(firstName, lastName);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
}
