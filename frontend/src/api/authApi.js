import axiosClient from '../services/axiosClient';
import API_URLS from './url';

const authApi = {
    adminLogin: (data) =>
        axiosClient.post(API_URLS.AUTH.ADMIN_LOGIN, data),

    userLogin: (data) =>
        axiosClient.post(API_URLS.AUTH.USER_LOGIN, data),

    logout: () =>
        axiosClient.post(API_URLS.AUTH.LOGOUT),
};

export default authApi;