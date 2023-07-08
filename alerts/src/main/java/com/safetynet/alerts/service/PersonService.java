package com.safetynet.alerts.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataRepository;

/**
 * A service class which performs the business processes relating to the POJO
 * <code>Person</code> before calling the repository.
 * 
 * @singularity In this case, this class is simply a bridge between the
 *              controller and the repository. CRUD processing is carried out at
 *              repository level.
 *
 * @author Sébastien Cappon
 * @version 1.0
 */
@Service
public class PersonService implements IPersonService {

	@Autowired
	private DataRepository repository;

	public List<Person> getPersonsList() {
		return repository.getPersonsList();
	}

	public Person getPersonByFirstNameAndLastName(String firstName, String lastName) {
		return repository.getPersonByFirstNameAndLastName(firstName, lastName);
	}

	public Person addPerson(Person person) {
		return repository.addPerson(person);
	}

	public Person updatePerson(String firstName, String lastName, Person update) {
		return repository.updatePerson(firstName, lastName, update);
	}

	public void deletePerson(String firstName, String lastName) {
		repository.deletePerson(firstName, lastName);
	}
}
