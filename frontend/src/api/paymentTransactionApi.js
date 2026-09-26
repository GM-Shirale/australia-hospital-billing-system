import axiosClient from '../services/axiosClient';
import API_URLS from './urls';

const paymentTransactionApi = {
    create: (data) =>
        axiosClient.post(
            API_URLS.PAYMENT_TRANSACTION.CREATE,
            data
        ),

    getById: (transactionId) =>
        axiosClient.get(
            API_URLS.PAYMENT_TRANSACTION.GET_BY_ID(transactionId)
        ),

    getByPayment: (paymentId) =>
        axiosClient.get(
            API_URLS.PAYMENT_TRANSACTION.GET_BY_PAYMENT(paymentId)
        ),
};

export default paymentTransactionApi;