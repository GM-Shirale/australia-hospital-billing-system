import axiosClient from '../services/axiosClient';
import API_URLS from './urls';

const departmentApi = {
    create: (data) =>
        axiosClient.post(
            API_URLS.DEPARTMENT.CREATE,
            data
        ),

    getAll: () =>
        axiosClient.get(
            API_URLS.DEPARTMENT.GET_ALL
        ),

    getById: (departmentId) =>
        axiosClient.get(
            API_URLS.DEPARTMENT.GET_BY_ID(departmentId)
        ),

    update: (departmentId, data) =>
        axiosClient.put(
            API_URLS.DEPARTMENT.UPDATE(departmentId),
            data
        ),

    delete: (departmentId) =>
        axiosClient.delete(
            API_URLS.DEPARTMENT.DELETE(departmentId)
        ),
};

export default departmentApi;