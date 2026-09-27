import axiosClient from '../services/axiosClient';
import API_URLS from './url';

const medicineBatchApi = {
    create: (data) =>
        axiosClient.post(
            API_URLS.MEDICINE_BATCH.CREATE,
            data
        ),

    getById: (batchId) =>
        axiosClient.get(
            API_URLS.MEDICINE_BATCH.GET_BY_ID(batchId)
        ),

    getAll: () =>
        axiosClient.get(
            API_URLS.MEDICINE_BATCH.GET_ALL
        ),

    getByMedicine: (medicineId) =>
        axiosClient.get(
            API_URLS.MEDICINE_BATCH.GET_BY_MEDICINE(medicineId)
        ),

    update: (batchId, data) =>
        axiosClient.put(
            API_URLS.MEDICINE_BATCH.UPDATE(batchId),
            data
        ),
};

export default medicineBatchApi;