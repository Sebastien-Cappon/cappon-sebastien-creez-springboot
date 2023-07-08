package com.safetynet.alerts.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;
import com.safetynet.alerts.dto.Views;

/**
 * Model class which creates the POJO (Plain Old Java Object)
 * <code>Person</code>. It contains getters and setters, as well as an override
 * <code>toSring()</code> method for display in the console.
 *
 * @singularity The POJOs <code>Person</code> and <code>MedicalRecord</code> are
 *              merged here. All <code>MedicalRecord</code> attributes are
 *              <code>protected</code> instead of <code>private</code>. However,
 *              <code>age</code> is NOT a <code>MedicalRecord</code> attributes.
 *              It's calculated from the <code>birthdate</code>.
 *
 * @author Sébastien Cappon
 * @version 1.0
 */
public class Person {
	@JsonView(Views.FirstNameView.class)
	protected String firstName;
	@JsonView(Views.LastNameView.class)
	protected String lastName;
	@JsonView(Views.AddressView.class)
	private String address;
	@JsonView(Views.CityView.class)
	private String city;
	@JsonView(Views.ZipView.class)
	private String zip;
	@JsonView(Views.PhoneView.class)
	private String phone;
	@JsonView(Views.EmailView.class)
	private String email;

	@JsonView(Views.BirthdateView.class)
	protected LocalDate birthdate;
	@JsonView(Views.AgeView.class)
	protected int age;
	@JsonView(Views.MedicationsView.class)
	protected List<String> medications;
	@JsonView(Views.AllergiesView.class)
	protected List<String> allergies;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(LocalDate birthdate) {
		this.birthdate = birthdate;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public List<String> getMedications() {
		return medications;
	}

	public void setMedications(List<String> medications) {
		this.medications = medications;
	}

	public List<String> getAllergies() {
		return allergies;
	}

	public void setAllergies(List<String> allergies) {
		this.allergies = allergies;
	}

	/**
	 * An override method for user-friendly display of <code>Person<code> attributes
	 * in the console. Not necessary, except for test purposes.
	 * 
	 * @return <code>String<code> containing all the attributes of
	 *         <code>Person<code>.
	 */
	@Override
	public String toString() {
		return ("\nperson{" + "firstName=" + firstName + ", " + "lastName=" + lastName + ", " + "address=" + address
				+ ", " + "city=" + city + ", " + "zip=" + zip + ", " + "phone=" + phone + ", " + "mail=" + email + ", "
				+ "age=" + age + ", " + "medications=" + medications + "allergies=" + allergies + "}");
	}
}
