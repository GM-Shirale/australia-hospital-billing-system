import axiosClient from '../services/axiosClient';
import API_URLS from './url';

const patientDocumentApi = {
    upload: (patientId, formData) =>
        axiosClient.post(
            API_URLS.PATIENT_DOCUMENT.UPLOAD(patientId),
            formData
        ),

    getById: (documentId) =>
        axiosClient.get(
            API_URLS.PATIENT_DOCUMENT.GET_BY_ID(documentId)
        ),

    getByPatient: (patientId) =>
        axiosClient.get(
            API_URLS.PATIENT_DOCUMENT.GET_BY_PATIENT(patientId)
        ),

    update: (documentId, data) =>
        axiosClient.put(
            API_URLS.PATIENT_DOCUMENT.UPDATE(documentId),
            data
        ),

    updateVerification: (documentId, data) =>
        axiosClient.put(
            API_URLS.PATIENT_DOCUMENT.UPDATE_VERIFICATION(documentId),
            data
        ),

    delete: (documentId) =>
        axiosClient.delete(
            API_URLS.PATIENT_DOCUMENT.DELETE(documentId)
        ),
};

export default patientDocumentApi;