import axiosClient from '../services/axiosClient';
import API_URLS from './url';

const labOrderApi = {
    create: (data) =>
        axiosClient.post(API_URLS.LAB_ORDER.CREATE, data),

    getById: (id) =>
        axiosClient.get(API_URLS.LAB_ORDER.GET_BY_ID(id)),

    getAll: () =>
        axiosClient.get(API_URLS.LAB_ORDER.GET_ALL),

    update: (id, data) =>
        axiosClient.put(
            API_URLS.LAB_ORDER.UPDATE(id),
            data
        ),

    delete: (id) =>
        axiosClient.delete(
            API_URLS.LAB_ORDER.DELETE(id)
        ),
};

export default labOrderApi;