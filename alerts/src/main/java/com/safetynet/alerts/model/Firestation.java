package com.safetynet.alerts.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.safetynet.alerts.dto.Views;

/**
 * Model class which creates the POJO (Plain Old Java Object)
 * <code>Firestation</code>. It contains getters and setters, as well as an
 * override <code>toSring()</code> method for display in the console.
 *
 * @author Sébastien Cappon
 * @version 1.0
 */
public class Firestation {
	@JsonView(Views.StationView.class)
	private Long station;
	@JsonView(Views.StationAddressView.class)
	private String address;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getStation() {
		return station;
	}

	public void setStation(Long station) {
		this.station = station;
	}

	/**
	 * An override method for user-friendly display of <code>Firestation</code>
	 * attributes in the console. Not necessary, except for test purposes.
	 * 
	 * @return <code>String</code> containing all the attributes of
	 *         <code>Firestation</code>.
	 */
	@Override
	public String toString() {
		return "\nfirestation{address=" + address + ", station=" + station + "}";
	}
}
