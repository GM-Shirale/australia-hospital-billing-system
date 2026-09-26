import axiosClient from '../services/axiosClient';
import API_URLS from './urls';

const patientApi = {
    create: (data) =>
        axiosClient.post(API_URLS.PATIENT.CREATE, data),

    getAll: () =>
        axiosClient.get(API_URLS.PATIENT.GET_ALL),

    getById: (patientId) =>
        axiosClient.get(API_URLS.PATIENT.GET_BY_ID(patientId)),

    update: (patientId, data) =>
        axiosClient.put(API_URLS.PATIENT.UPDATE(patientId), data),

    delete: (patientId) =>
        axiosClient.delete(API_URLS.PATIENT.DELETE(patientId)),
};

export default patientApi;