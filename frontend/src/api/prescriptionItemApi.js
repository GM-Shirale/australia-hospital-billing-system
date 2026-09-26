import axiosClient from '../services/axiosClient';
import API_URLS from './urls';

const prescriptionItemApi = {
    create: (data) =>
        axiosClient.post(
            API_URLS.PRESCRIPTION_ITEM.CREATE,
            data
        ),

    getById: (prescriptionItemId) =>
        axiosClient.get(
            API_URLS.PRESCRIPTION_ITEM.GET_BY_ID(
                prescriptionItemId
            )
        ),

    getByPrescription: (prescriptionId) =>
        axiosClient.get(
            API_URLS.PRESCRIPTION_ITEM.GET_BY_PRESCRIPTION(
                prescriptionId
            )
        ),

    getByMedicine: (medicineId) =>
        axiosClient.get(
            API_URLS.PRESCRIPTION_ITEM.GET_BY_MEDICINE(
                medicineId
            )
        ),

    update: (prescriptionItemId, data) =>
        axiosClient.put(
            API_URLS.PRESCRIPTION_ITEM.UPDATE(
                prescriptionItemId
            ),
            data
        ),

    delete: (prescriptionItemId) =>
        axiosClient.delete(
            API_URLS.PRESCRIPTION_ITEM.DELETE(
                prescriptionItemId
            )
        ),
};

export default prescriptionItemApi;