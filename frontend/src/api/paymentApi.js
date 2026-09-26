import axiosClient from '../services/axiosClient';
import API_URLS from './urls';

const paymentApi = {
    create: (data) =>
        axiosClient.post(API_URLS.PAYMENT.CREATE, data),

    getById: (paymentId) =>
        axiosClient.get(
            API_URLS.PAYMENT.GET_BY_ID(paymentId)
        ),

    getByBill: (billId) =>
        axiosClient.get(
            API_URLS.PAYMENT.GET_BY_BILL(billId)
        ),

    getAll: () =>
        axiosClient.get(API_URLS.PAYMENT.GET_ALL),
};

export default paymentApi;