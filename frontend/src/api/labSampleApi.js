import axiosClient from '../services/axiosClient';
import API_URLS from './urls';

const labSampleApi = {
    create: (data) =>
        axiosClient.post(
            API_URLS.LAB_SAMPLE.CREATE,
            data
        ),

    getById: (id) =>
        axiosClient.get(
            API_URLS.LAB_SAMPLE.GET_BY_ID(id)
        ),

    getAll: () =>
        axiosClient.get(API_URLS.LAB_SAMPLE.GET_ALL),

    getByOrder: (labOrderId) =>
        axiosClient.get(
            API_URLS.LAB_SAMPLE.GET_BY_ORDER(labOrderId)
        ),

    update: (id, data) =>
        axiosClient.put(
            API_URLS.LAB_SAMPLE.UPDATE(id),
            data
        ),

    delete: (id) =>
        axiosClient.delete(
            API_URLS.LAB_SAMPLE.DELETE(id)
        ),
};

export default labSampleApi;