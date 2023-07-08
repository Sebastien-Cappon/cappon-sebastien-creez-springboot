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

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.IPersonService;

/**
 * A class that receives requests made from the usual CRUD endpoints for the persons.
 * 
 * @author Sébastien Cappon
 * @version 1.0
 */
@RestController
public class PersonController {
	private static final Logger logger = LoggerFactory.getLogger(PersonController.class);

	@Autowired
	private IPersonService iPersonService;

	/**
	 * A <code>GetMapping</code> method on the <code>/persons</code> URI which calls
	 * the eponymous <code>IPersonService</code> method and returns the list of all
	 * the persons with status code 200. If the result is empty, it returns status
	 * code 204.
	 * 
	 * @return <code>Person</code> list and a status code.
	 */
	@GetMapping("/persons")
	public ResponseEntity<List<Person>> getPersonsList() {
		List<Person> personsList = iPersonService.getPersonsList();

		if (personsList.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(personsList, HttpStatus.OK);
	}
	
	/**
	 * A <code>GetMapping</code> method on the <code>/person</code> URI with
	 * a first name and a last name as <code>PathVariables</code>. It calls the
	 * eponymous <code>IPersonService</code> method and returns the
	 * <code>PErson</code> asked, with status code 200. If the result is
	 * empty, it returns status code 204.
	 * 
	 * @return A <code>Person</code> and a status code.
	 */
	@GetMapping("/person/{firstName}-{lastName}")
	public ResponseEntity<Person> getPersonByFirstNameAndLastName(@PathVariable("firstName") String firstName, @PathVariable("lastName") String lastName) {
		Person person = iPersonService.getPersonByFirstNameAndLastName(firstName, lastName);

		if (person == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(person, HttpStatus.OK);
	}

	/**
	 * A <code>PostMapping</code> method on the <code>/person</code> URI which calls
	 * the eponymous <code>IPersonService</code> method and returns the
	 * <code>Person</code> added with status code 201. If the result is null, it
	 * returns status code 204.
	 * 
	 * @throws BAD_REQUEST if first name and last name are null or blank in the
	 *                     request body.
	 * 
	 * @return A <code>Person</code> and a status code.
	 */
	@PostMapping("/person")
	public ResponseEntity<Person> addPerson(@RequestBody Person person) throws Exception {
		if (person.getFirstName() == null || person.getFirstName().isBlank() || person.getLastName() == null || person.getLastName().isBlank()) {
			logger.warn("Requested key-values pairs <firstName> or <lastName> are missing in the request body.");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		Person personAdded = iPersonService.addPerson(person);
		if (personAdded == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{firstName}-{lastName}").buildAndExpand(personAdded.getFirstName(), personAdded.getLastName()).toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(location);

		return new ResponseEntity<>(personAdded, headers, HttpStatus.CREATED);
	}

	/**
	 * A <code>PutMapping</code> method on the <code>/person</code> URI with
	 * a first name and a last name as <code>PathVariables</code>. It calls the
	 * eponymous <code>IPersonService</code> method and returns the modified
	 * <code>Person</code>, with status code 200. If the result is null, it
	 * returns status code 204.
	 * 
	 * @return A <code>Person</code> and a status code.
	 */
	@PutMapping("/person/{firstName}-{lastName}")
	public ResponseEntity<Person> updatePerson(@PathVariable("firstName") String firstName, @PathVariable("lastName") String lastName, @RequestBody Person update) {
		Person personUpdated = iPersonService.updatePerson(firstName, lastName, update);

		if (personUpdated == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(personUpdated, HttpStatus.OK);
	}

	/**
	 * A <code>DeleteMapping</code> method on the <code>/person</code> URI with a
	 * first name and a last name as <code>PathVariables</code>. It calls the
	 * eponymous <code>IPersonService</code> method and returns nothing except
	 * status code 204.
	 * 
	 * @return a status code 204.
	 */
	@DeleteMapping("/person/{firstName}-{lastName}")
	public ResponseEntity<Void> deletePerson(@PathVariable("firstName") String firstName, @PathVariable("lastName") String lastName) {
		iPersonService.deletePerson(firstName, lastName);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
}
