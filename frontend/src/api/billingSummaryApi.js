import axiosClient from '../services/axiosClient';
import API_URLS from './urls';

const billingSummaryApi = {
    getHospitalSummary: () =>
        axiosClient.get(
            API_URLS.BILLING_SUMMARY.GET_HOSPITAL_SUMMARY
        ),

    getPatientSummary: (patientId) =>
        axiosClient.get(
            API_URLS.BILLING_SUMMARY.GET_PATIENT_SUMMARY(patientId)
        ),
};

export default billingSummaryApi;