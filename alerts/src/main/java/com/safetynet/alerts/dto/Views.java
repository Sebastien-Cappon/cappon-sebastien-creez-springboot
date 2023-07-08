package com.safetynet.alerts.dto;

/**
 * A class that contains a lot of interfaces that inherit from each other.These
 * are used to filter the attributes to be returned in response to requests sent
 * to the API.
 *
 * @author Sébastien Cappon
 * @version 1.0
 */
public class Views {
	public interface FirstNameView {}
	public interface LastNameView {}
	public interface AddressView {}
	public interface CityView {}
	public interface ZipView {}
	public interface PhoneView {}
	public interface EmailView {}
	public interface AgeView {}
	public interface BirthdateView {}
	public interface MedicationsView {}
	public interface AllergiesView {}
	
	public interface StationView {}
	public interface StationAddressView {}
	
	public interface FirestationScopeView extends FirstNameView, LastNameView, AddressView, PhoneView {}
	public interface PersonInfoView extends FirstNameView, LastNameView, AddressView, EmailView, AgeView, MedicationsView, AllergiesView {}  
	public interface ChildView extends FirstNameView, LastNameView, AgeView {}
	public interface HouseholdView extends FirstNameView, LastNameView, PhoneView, AgeView, MedicationsView, AllergiesView {}
	public interface HouseholdFireView extends HouseholdView, StationView, StationAddressView {}
	
}