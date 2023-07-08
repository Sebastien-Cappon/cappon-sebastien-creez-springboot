package com.safetynet.alerts.service;

import java.util.List;

import com.safetynet.alerts.model.Firestation;

/**
 * <code>FiresttationService</code> interface that abstracts it from its
 * implementation in order to achieve better code modularity in compliance with
 * SOLID principles.
 *
 * @author Sébastien Cappon
 * @version 1.0
 */
public interface IFirestationService {

	public List<Firestation> getFirestationsList();
	public List<Firestation> getFirestationsByAddress(String stationAddress);
	public List<Firestation> getFirestationsByNumberAndAddress(Long stationNumber, String stationAddress);
	public Firestation addFirestation(Firestation firestation);
	public List<Firestation> updateFirestationNumber(Long stationNumber, String stationAddress, Long newStationNumber);
	public void deleteFirestation(Long stationNumber, String stationAddress);
}
