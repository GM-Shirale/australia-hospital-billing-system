import axiosClient from '../services/axiosClient';
import API_URLS from './url';

const refundApi = {
    create: (data) =>
        axiosClient.post(API_URLS.REFUND.CREATE, data),

    getById: (refundId) =>
        axiosClient.get(
            API_URLS.REFUND.GET_BY_ID(refundId)
        ),

    getByTransaction: (transactionId) =>
        axiosClient.get(
            API_URLS.REFUND.GET_BY_TRANSACTION(transactionId)
        ),

    getAll: () =>
        axiosClient.get(API_URLS.REFUND.GET_ALL),
};

export default refundApi;