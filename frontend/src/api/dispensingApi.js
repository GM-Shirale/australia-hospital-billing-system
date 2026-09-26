import axiosClient from '../services/axiosClient';
import API_URLS from './urls';

const dispensingApi = {
    create: (data) =>
        axiosClient.post(
            API_URLS.DISPENSING.CREATE,
            data
        ),

    getById: (dispensingId) =>
        axiosClient.get(
            API_URLS.DISPENSING.GET_BY_ID(dispensingId)
        ),

    getByPrescriptionItem: (prescriptionItemId) =>
        axiosClient.get(
            API_URLS.DISPENSING.GET_BY_PRESCRIPTION_ITEM(
                prescriptionItemId
            )
        ),

    getByStock: (stockId) =>
        axiosClient.get(
            API_URLS.DISPENSING.GET_BY_STOCK(stockId)
        ),

    getByPrescription: (prescriptionId) =>
        axiosClient.get(
            API_URLS.DISPENSING.GET_BY_PRESCRIPTION(prescriptionId)
        ),
};

export default dispensingApi;