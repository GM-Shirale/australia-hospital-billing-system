import axiosClient from '../services/axiosClient';
import API_URLS from './urls';

const userApi = {
    createUser: (data) =>
        axiosClient.post(API_URLS.USER.CREATE, data),

    getAllUsers: () =>
        axiosClient.get(API_URLS.USER.GET_ALL),

    getUserById: (userId) =>
        axiosClient.get(API_URLS.USER.GET_BY_ID(userId)),

    updateUser: (userId, data) =>
        axiosClient.put(API_URLS.USER.UPDATE(userId), data),

    activateUser: (userId) =>
        axiosClient.put(API_URLS.USER.ACTIVATE(userId)),

    deactivateUser: (userId) =>
        axiosClient.put(API_URLS.USER.DEACTIVATE(userId)),

    deleteUser: (userId) =>
        axiosClient.delete(API_URLS.USER.DELETE(userId)),
};

export default userApi;