import axiosClient from '../services/axiosClient';
import API_URLS from './urls';

const insurancePolicyApi = {
    create: (data) =>
        axiosClient.post(
            API_URLS.INSURANCE_POLICY.CREATE,
            data
        ),

    getById: (policyId) =>
        axiosClient.get(
            API_URLS.INSURANCE_POLICY.GET_BY_ID(policyId)
        ),

    getByPatient: (patientId) =>
        axiosClient.get(
            API_URLS.INSURANCE_POLICY.GET_BY_PATIENT(patientId)
        ),
};

export default insurancePolicyApi;