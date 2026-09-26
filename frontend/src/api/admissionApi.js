import axiosClient from '../services/axiosClient';
import API_URLS from './url';

const admissionApi = {
    create: (patientId, data) =>
        axiosClient.post(
            API_URLS.ADMISSION.CREATE_FOR_PATIENT(patientId),
            data
        ),

    getById: (admissionId) =>
        axiosClient.get(
            API_URLS.ADMISSION.GET_BY_ID(admissionId)
        ),

    getAll: () =>
        axiosClient.get(API_URLS.ADMISSION.GET_ALL),

    getByPatient: (patientId) =>
        axiosClient.get(
            API_URLS.ADMISSION.GET_BY_PATIENT(patientId)
        ),

    update: (admissionId, data) =>
        axiosClient.put(
            API_URLS.ADMISSION.UPDATE(admissionId),
            data
        ),

    discharge: (admissionId) =>
        axiosClient.put(
            API_URLS.ADMISSION.DISCHARGE(admissionId)
        ),

    delete: (admissionId) =>
        axiosClient.delete(
            API_URLS.ADMISSION.DELETE(admissionId)
        ),
};

export default admissionApi;