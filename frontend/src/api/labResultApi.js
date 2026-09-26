import axiosClient from '../services/axiosClient';
import API_URLS from './urls';

const labResultApi = {
    create: (data) =>
        axiosClient.post(
            API_URLS.LAB_RESULT.CREATE,
            data
        ),

    getById: (id) =>
        axiosClient.get(
            API_URLS.LAB_RESULT.GET_BY_ID(id)
        ),

    getAll: () =>
        axiosClient.get(API_URLS.LAB_RESULT.GET_ALL),

    getBySample: (labSampleId) =>
        axiosClient.get(
            API_URLS.LAB_RESULT.GET_BY_SAMPLE(labSampleId)
        ),

    getByTest: (labTestId) =>
        axiosClient.get(
            API_URLS.LAB_RESULT.GET_BY_TEST(labTestId)
        ),

    update: (id, data) =>
        axiosClient.put(
            API_URLS.LAB_RESULT.UPDATE(id),
            data
        ),

    delete: (id) =>
        axiosClient.delete(
            API_URLS.LAB_RESULT.DELETE(id)
        ),
};

export default labResultApi;