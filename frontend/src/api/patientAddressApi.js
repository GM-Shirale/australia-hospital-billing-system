import axiosClient from '../services/axiosClient';
import API_URLS from './urls';

const patientAddressApi = {
    create: (patientId, data) =>
        axiosClient.post(
            API_URLS.PATIENT_ADDRESS.CREATE(patientId),
            data
        ),

    getById: (addressId) =>
        axiosClient.get(
            API_URLS.PATIENT_ADDRESS.GET_BY_ID(addressId)
        ),

    getByPatient: (patientId) =>
        axiosClient.get(
            API_URLS.PATIENT_ADDRESS.GET_BY_PATIENT(patientId)
        ),

    update: (addressId, data) =>
        axiosClient.put(
            API_URLS.PATIENT_ADDRESS.UPDATE(addressId),
            data
        ),

    delete: (addressId) =>
        axiosClient.delete(
            API_URLS.PATIENT_ADDRESS.DELETE(addressId)
        ),
};

export default patientAddressApi;