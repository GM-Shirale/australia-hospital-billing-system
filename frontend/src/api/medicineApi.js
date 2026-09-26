import axiosClient from '../services/axiosClient';
import API_URLS from './urls';

const medicineApi = {
    create: (data) =>
        axiosClient.post(
            API_URLS.MEDICINE.CREATE,
            data
        ),

    getById: (medicineId) =>
        axiosClient.get(
            API_URLS.MEDICINE.GET_BY_ID(medicineId)
        ),

    getAll: () =>
        axiosClient.get(
            API_URLS.MEDICINE.GET_ALL
        ),

    search: (search) =>
        axiosClient.get(
            API_URLS.MEDICINE.SEARCH,
            {
                params: { search },
            }
        ),

    update: (medicineId, data) =>
        axiosClient.put(
            API_URLS.MEDICINE.UPDATE(medicineId),
            data
        ),

    updateStatus: (medicineId, status) =>
        axiosClient.patch(
            API_URLS.MEDICINE.UPDATE_STATUS(medicineId),
            null,
            {
                params: { status },
            }
        ),
};

export default medicineApi;