package com.safetynet.alerts.service;

import java.util.List;

import com.safetynet.alerts.model.Person;

/**
 * <code>PersonService</code> interface that abstracts it from its
 * implementation in order to achieve better code modularity in compliance with
 * SOLID principles.
 *
 * @author Sébastien Cappon
 * @version 1.0
 */
public interface IPersonService {

	public List<Person> getPersonsList();
	public Person getPersonByFirstNameAndLastName(String firstName, String lastName);
	public Person addPerson(Person person);
	public Person updatePerson(String firstName, String lastName, Person update);
	public void deletePerson(String firstName, String lastName);
}
