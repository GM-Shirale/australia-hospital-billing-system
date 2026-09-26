import axiosClient from '../services/axiosClient';
import API_URLS from './url';

const labResultValueApi = {
    create: (data) =>
        axiosClient.post(
            API_URLS.LAB_RESULT_VALUE.CREATE,
            data
        ),

    getById: (id) =>
        axiosClient.get(
            API_URLS.LAB_RESULT_VALUE.GET_BY_ID(id)
        ),

    getAll: () =>
        axiosClient.get(
            API_URLS.LAB_RESULT_VALUE.GET_ALL
        ),

    getByResult: (labResultId) =>
        axiosClient.get(
            API_URLS.LAB_RESULT_VALUE.GET_BY_RESULT(labResultId)
        ),

    getByParameter: (labParameterId) =>
        axiosClient.get(
            API_URLS.LAB_RESULT_VALUE.GET_BY_PARAMETER(labParameterId)
        ),

    update: (id, data) =>
        axiosClient.put(
            API_URLS.LAB_RESULT_VALUE.UPDATE(id),
            data
        ),

    delete: (id) =>
        axiosClient.delete(
            API_URLS.LAB_RESULT_VALUE.DELETE(id)
        ),
};

export default labResultValueApi;