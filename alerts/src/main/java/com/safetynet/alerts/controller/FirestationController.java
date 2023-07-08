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

import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.service.IFirestationService;

/**
 * A class that receives requests made from the usual CRUD endpoints for the firestations.
 * 
 * @author Sébastien Cappon
 * @version 1.0
 */
@RestController
public class FirestationController {
	private static final Logger logger = LoggerFactory.getLogger(FirestationController.class);

	@Autowired
	IFirestationService iFirestationService;

	/**
	 * A <code>GetMapping</code> method on the <code>/firestations</code> URI which
	 * calls the eponymous <code>IFirestationService</code> method and returns the
	 * list of all firestations mapping with status code 200. If the result is
	 * empty, it returns status code 204.
	 * 
	 * @return <code>Firestation</code> list and a status code.
	 */
	@GetMapping("/firestations")
	public ResponseEntity<List<Firestation>> getFirestationsList() {
		List<Firestation> firestationsList = iFirestationService.getFirestationsList();

		if (firestationsList.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(firestationsList, HttpStatus.OK);
	}

	/**
	 * A <code>GetMapping</code> method on the <code>/firestation</code> URI with an
	 * address as <code>PathVariable</code>. It calls the eponymous
	 * <code>IFirestationService</code> method and returns the list of firestations
	 * mapping for this address, with status code 200. If the result is empty, it
	 * returns status code 204.
	 * 
	 * @return <code>Firestation</code> list and a status code.
	 */
	@GetMapping("/firestation/{address}")
	public ResponseEntity<List<Firestation>> getFirestationsByAddress(@PathVariable("address") String stationAddress) {
		List<Firestation> firestationsList = iFirestationService.getFirestationsByAddress(stationAddress);

		if (firestationsList.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(firestationsList, HttpStatus.OK);
	}

	/**
	 * A <code>PostMapping</code> method on the <code>/firestation</code> URI which
	 * calls the eponymous <code>IFirestationService</code> method and returns the
	 * <code>Firestation</code> added with status code 201. If the result is null,
	 * it returns status code 204.
	 * 
	 * @throws BAD_REQUEST if station and address are null or blank in the request
	 *                     body.
	 * 
	 * @return <code>Firestation</code> list and a status code.
	 */
	@PostMapping("/firestation")
	public ResponseEntity<Firestation> addFirestation(@RequestBody Firestation firestation) throws Exception {
		if (firestation.getStation() == null || firestation.getAddress() == null || firestation.getAddress().isBlank()) {
			logger.warn("Requested key-values pairs <firstName> and <lastName> are missing in the request body.");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		Firestation firestationAdded = iFirestationService.addFirestation(firestation);
		if (firestationAdded == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{address}").buildAndExpand(firestationAdded.getAddress()).toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(location);

		return new ResponseEntity<>(firestationAdded, headers, HttpStatus.CREATED);
	}

	/**
	 * A <code>PutMapping</code> method on the <code>/firestation</code> URI with a
	 * station number and an address as <code>PathVariables</code>. It calls the
	 * eponymous <code>IFirestationService</code> method and returns the
	 * <code>Firestation</code> mapping list modified, with status code 200. If the
	 * result is null, it returns status code 204.
	 * 
	 * @return <code>Firestation</code> list and a status code.
	 */
	@PutMapping("/firestation/{station}-{address}")
	public ResponseEntity<List<Firestation>> updateFirestationNumber(@PathVariable("station") Long stationNumber, @PathVariable("address") String stationAddress, @RequestBody Long newStationNumber) {
		List<Firestation> firestationsUpdatedList = iFirestationService.updateFirestationNumber(stationNumber, stationAddress, newStationNumber);

		if (firestationsUpdatedList == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(firestationsUpdatedList, HttpStatus.OK);
	}

	/**
	 * A <code>DeleteMapping</code> method on the <code>/firestation</code> URI with
	 * a station number and an address as <code>PathVariables</code>. It calls the
	 * eponymous <code>IFirestationService</code> method and returns nothing except
	 * status code 204.
	 * 
	 * @return a status code 204.
	 */
	@DeleteMapping("/firestation/{station}-{address}")
	public ResponseEntity<Void> deleteFirestation(@PathVariable("station") Long stationNumber, @PathVariable("address") String stationAddress) {
		iFirestationService.deleteFirestation(stationNumber, stationAddress);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
