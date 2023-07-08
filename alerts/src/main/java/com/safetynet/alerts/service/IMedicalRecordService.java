package com.safetynet.alerts.service;

import java.util.List;

import com.safetynet.alerts.model.MedicalRecord;

/**
 * <code>MedicalRecordService</code> interface that abstracts it from its
 * implementation in order to achieve better code modularity in compliance with
 * SOLID principles.
 *
 * @author Sébastien Cappon
 * @version 1.0
 */
public interface IMedicalRecordService {
	
	public List<MedicalRecord> getMedicalRecordsList();
	public MedicalRecord getMedicalRecordByFirstNameAndLastName(String firstName, String lastName);
	public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord);
	public MedicalRecord updateMedicalRecord(String firstName, String lastName, MedicalRecord update);
	public void deleteMedicalRecord(String firstName, String lastName);
}
