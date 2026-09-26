import axiosClient from '../services/axiosClient';
import API_URLS from './urls';

const billItemApi = {
    create: (billId, data) =>
        axiosClient.post(
            API_URLS.BILL_ITEM.CREATE_FOR_BILL(billId),
            data
        ),

    getById: (billItemId) =>
        axiosClient.get(
            API_URLS.BILL_ITEM.GET_BY_ID(billItemId)
        ),

    getByBill: (billId) =>
        axiosClient.get(
            API_URLS.BILL_ITEM.GET_BY_BILL(billId)
        ),

    update: (billItemId, data) =>
        axiosClient.put(
            API_URLS.BILL_ITEM.UPDATE(billItemId),
            data
        ),

    delete: (billItemId) =>
        axiosClient.delete(
            API_URLS.BILL_ITEM.DELETE(billItemId)
        ),
};

export default billItemApi;