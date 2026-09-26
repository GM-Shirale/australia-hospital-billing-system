import axiosClient from '../services/axiosClient';
import API_URLS from './urls';

const labTestApi = {
    create: (data) =>
        axiosClient.post(API_URLS.LAB_TEST.CREATE, data),

    getById: (id) =>
        axiosClient.get(API_URLS.LAB_TEST.GET_BY_ID(id)),

    getAll: () =>
        axiosClient.get(API_URLS.LAB_TEST.GET_ALL),

    update: (id, data) =>
        axiosClient.put(
            API_URLS.LAB_TEST.UPDATE(id),
            data
        ),

    delete: (id) =>
        axiosClient.delete(
            API_URLS.LAB_TEST.DELETE(id)
        ),
};

export default labTestApi;