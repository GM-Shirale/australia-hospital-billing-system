import axiosClient from '../services/axiosClient';
import API_URLS from './url';

const billingApi = {
    create: (patientId, data) =>
        axiosClient.post(
            API_URLS.BILL.CREATE_FOR_PATIENT(patientId),
            data
        ),

    getById: (billId) =>
        axiosClient.get(
            API_URLS.BILL.GET_BY_ID(billId)
        ),

    getAll: () =>
        axiosClient.get(API_URLS.BILL.GET_ALL),

    getByPatient: (patientId) =>
        axiosClient.get(
            API_URLS.BILL.GET_BY_PATIENT(patientId)
        ),

    update: (billId, data) =>
        axiosClient.put(
            API_URLS.BILL.UPDATE(billId),
            data
        ),

    delete: (billId) =>
        axiosClient.delete(
            API_URLS.BILL.DELETE(billId)
        ),

    getPdf: (billId) =>
        axiosClient.get(
            API_URLS.BILL.PDF(billId),
            {
                responseType: 'blob',
            }
        ),
};

export default billingApi;