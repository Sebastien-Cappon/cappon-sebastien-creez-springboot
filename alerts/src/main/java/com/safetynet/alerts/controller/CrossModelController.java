package com.safetynet.alerts.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.safetynet.alerts.dto.Child;
import com.safetynet.alerts.dto.FirestationScope;
import com.safetynet.alerts.dto.Household;
import com.safetynet.alerts.dto.Views;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.ICrossModelService;

/**
 * A class that receives requests made from specific URLs that are independent of the CRUD endpoints.
 * 
 * @singularity Responses are filtered using JsonView to match the response pattern desired by the client.
 *
 * @author Sébastien Cappon
 * @version 1.0
 */
@RestController
public class CrossModelController {

	@Autowired
	private ICrossModelService iCrossModelService;

	/**
	 * A <code>GetMapping</code> method on the <code>/firestation</code> URI which
	 * calls the eponymous <code>ICrossModelService</code> method and returns a
	 * <code>FirestationScope</code> (DTO) with status code 200. If the result is
	 * empty, it returns status code 204.
	 * 
	 * @view Display first name, last name, address and phone number.
	 * 
	 * @return <code>FirestationScope</code> (DTO) and a status code.
	 */
	@JsonView(Views.FirestationScopeView.class)
	@GetMapping("/firestation")
	@ResponseBody
	public ResponseEntity<FirestationScope> getPersonsCoveredByFirestation(@RequestParam Long stationNumber) {
		FirestationScope firestationScope = iCrossModelService.getPersonsCoveredByFirestation(stationNumber);

		if (firestationScope.getAdultQuantity() == 0 && firestationScope.getChildQuantity() == 0) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(firestationScope, HttpStatus.OK);
	}

	/**
	 * A <code>GetMapping</code> method on the <code>/childAlert</code> URI which
	 * calls the eponymous <code>ICrossModelService</code> method and returns a
	 * <code>Child</code> (DTO) list with status code 200. If the result is empty,
	 * it returns status code 204.
	 * 
	 * @view Display first name, last name and age.
	 * 
	 * @return <code>Child</code> (DTO) list and a status code.
	 */
	@JsonView(Views.ChildView.class)
	@GetMapping("/childAlert")
	@ResponseBody
	public ResponseEntity<List<Child>> getChildrenFromAddress(@RequestParam String address) {
		List<Child> childList = iCrossModelService.getChildrenFromAddress(address);

		if (childList.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(childList, HttpStatus.OK);
	}

	/**
	 * A <code>GetMapping</code> method on the <code>/phoneAlert</code> URI which
	 * calls the eponymous <code>ICrossModelService</code> method and returns a list
	 * of phone numbers with status code 200. If the result is empty, it returns
	 * status code 204.
	 * 
	 * @return <code>String</code> list of phone numbers and a status code.
	 */
	@GetMapping("/phoneAlert")
	@ResponseBody
	public ResponseEntity<List<String>> getPhoneNumberFromPersonsCoveredByFirestation(@RequestParam Long firestation) {
		List<String> phoneNumberList = iCrossModelService.getPhoneNumberFromPersonsCoveredByFirestation(firestation);

		if (phoneNumberList.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(phoneNumberList, HttpStatus.OK);
	}

	/**
	 * A <code>GetMapping</code> method on the <code>/fire</code> URI which calls
	 * the eponymous <code>ICrossModelService</code> method and returns a
	 * <code>Household</code> (DTO) with status code 200. If the result is empty, it
	 * returns status code 204.
	 * 
	 * @view Display household, station number and station address.
	 * 
	 * @return <code>Household</code> (DTO) and a status code.
	 */
	@JsonView(Views.HouseholdFireView.class)
	@GetMapping("/fire")
	@ResponseBody
	public ResponseEntity<Household> getPersonsAndFirestationFromAddress(@RequestParam String address) {
		Household household = iCrossModelService.getPersonsAndFirestationFromAddress(address);

		if (household.getFirestationsNumber().isEmpty() && household.getHouseholdMembers().isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(household, HttpStatus.OK);
	}

	/**
	 * A <code>GetMapping</code> method on the <code>/flood/stations</code> URI
	 * which calls the eponymous <code>ICrossModelService</code> method and returns
	 * a list of all the <code>Household</code> sorted by addresses covered by
	 * <code>stations</code>. with status code 200. If the result is empty, it
	 * returns status code 204.
	 * 
	 * @view Display first name, last name, phone, age, medications and allergies.
	 * 
	 * @return <code>TreeMap</code> of <code>Long</code> and <code>TreeMap</code> of
	 *         <code>TreeMap</code> <code>Long</code> and <code>Household</code>
	 *         (DTO) and a status code.
	 */
	@JsonView(Views.HouseholdView.class)
	@GetMapping("/flood/stations")
	@ResponseBody
	public ResponseEntity<Map<Long, Map<String, Household>>> getHouseholdsCoveredByFirestations(@RequestParam List<Long> stations) {
		Map<Long, Map<String, Household>> householdAndFirestationsMap = iCrossModelService.getHouseholdsCoveredByFirestations(stations);

		if (householdAndFirestationsMap.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(householdAndFirestationsMap, HttpStatus.OK);
	}

	/**
	 * A <code>GetMapping</code> method on the <code>/personInfo</code> URI which
	 * calls the eponymous <code>ICrossModelService</code> method and returns a
	 * <code>Person</code> (DTO) list with status code 200. If the result is empty,
	 * it returns status code 204.
	 * 
	 * @view Display first name, last name, address, email, age, medications and
	 *       allergies.
	 * 
	 * @return <code>Person</code> (DTO) list and a status code.
	 */
	@JsonView(Views.PersonInfoView.class)
	@GetMapping("/personInfo")
	@ResponseBody
	public ResponseEntity<List<Person>> getPersonsInfoByFirstNameAndLastName(@RequestParam String firstName, @RequestParam String lastName) {
		List<Person> personList = iCrossModelService.getPersonsInfoByFirstNameAndLastName(firstName, lastName);

		if (personList.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(personList, HttpStatus.OK);
	}

	/**
	 * A <code>GetMapping</code> method on the <code>/communityEmail</code> URI
	 * which calls the eponymous <code>ICrossModelService</code> method and returns
	 * the list of email addresses of all the city residents with status code 200.
	 * If the result is empty, it returns status code 204.
	 * 
	 * @return <code>String</code> list and a status code.
	 */
	@GetMapping("/communityEmail")
	@ResponseBody
	public ResponseEntity<List<String>> getEmailsFromCityResidents(@RequestParam String city) {
		List<String> emailList = iCrossModelService.getEmailsFromCityResidents(city);

		if (emailList.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(emailList, HttpStatus.OK);
	}
}
