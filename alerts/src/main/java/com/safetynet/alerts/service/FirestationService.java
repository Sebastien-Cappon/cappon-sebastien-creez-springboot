package com.safetynet.alerts.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.repository.DataRepository;

/**
 * A service class which performs the business processes relating to the POJO
 * <code>Firestation</code> before calling the repository.
 * 
 * @singularity In this case, this class is simply a bridge between the
 *              controller and the repository. CRUD processing is carried out at
 *              repository level.
 *
 * @author Sébastien Cappon
 * @version 1.0
 */
@Service
public class FirestationService implements IFirestationService {

	@Autowired
	private DataRepository repository;

	@Override
	public List<Firestation> getFirestationsList() {
		return repository.getFirestationsList();
	}

	@Override
	public List<Firestation> getFirestationsByAddress(String stationAddress) {
		return repository.getFirestationsByAddress(stationAddress);
	}

	@Override
	public List<Firestation> getFirestationsByNumberAndAddress(Long stationNumber, String stationAddress) {
		return repository.getFirestationsByNumberAndAddress(stationNumber, stationAddress);
	}

	@Override
	public Firestation addFirestation(Firestation firestation) {
		return repository.addFirestation(firestation);
	}

	@Override
	public List<Firestation> updateFirestationNumber(Long stationNumber, String stationAddress, Long newStationNumber) {
		return repository.updateFirestationNumber(stationNumber, stationAddress, newStationNumber);
	}

	@Override
	public void deleteFirestation(Long stationNumber, String stationAddress) {
		repository.deleteFirestation(stationNumber, stationAddress);
	}
}
