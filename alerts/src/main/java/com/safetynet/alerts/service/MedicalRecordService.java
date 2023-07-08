package com.safetynet.alerts.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.DataRepository;

/**
 * A service class which performs the business processes relating to the POJO
 * <code>MedicalRecord</code> before calling the repository.
 * 
 * @singularity In this case, this class is simply a bridge between the
 *              controller and the repository. CRUD processing is carried out at
 *              repository level.
 *
 * @author Sébastien Cappon
 * @version 1.0
 */
@Service
public class MedicalRecordService implements IMedicalRecordService {

	@Autowired
	private DataRepository repository;

	public List<MedicalRecord> getMedicalRecordsList() {
		return repository.getMedicalRecordsList();
	}

	public MedicalRecord getMedicalRecordByFirstNameAndLastName(String firstName, String lastName) {
		return repository.getMedicalRecordByFirstNameAndLastName(firstName, lastName);
	}

	public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord) {
		return repository.addMedicalRecord(medicalRecord);
	}

	public MedicalRecord updateMedicalRecord(String firstName, String lastName, MedicalRecord update) {
		return repository.updateMedicalRecord(firstName, lastName, update);
	}

	public void deleteMedicalRecord(String firstName, String lastName) {
		repository.deleteMedicalRecord(firstName, lastName);
	}
}
