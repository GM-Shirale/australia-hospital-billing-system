import axiosClient from '../services/axiosClient';
import API_URLS from './url';

const medicineStockApi = {
    create: (data) =>
        axiosClient.post(
            API_URLS.MEDICINE_STOCK.CREATE,
            data
        ),

    getById: (stockId) =>
        axiosClient.get(
            API_URLS.MEDICINE_STOCK.GET_BY_ID(stockId)
        ),

    getByMedicine: (medicineId) =>
        axiosClient.get(
            API_URLS.MEDICINE_STOCK.GET_BY_MEDICINE(medicineId)
        ),

    getByBatch: (batchId) =>
        axiosClient.get(
            API_URLS.MEDICINE_STOCK.GET_BY_BATCH(batchId)
        ),

    getActive: () =>
        axiosClient.get(
            API_URLS.MEDICINE_STOCK.GET_ACTIVE
        ),

    getLowStock: () =>
        axiosClient.get(
            API_URLS.MEDICINE_STOCK.GET_LOW_STOCK
        ),

    update: (stockId, data) =>
        axiosClient.put(
            API_URLS.MEDICINE_STOCK.UPDATE(stockId),
            data
        ),

    delete: (stockId) =>
        axiosClient.delete(
            API_URLS.MEDICINE_STOCK.DELETE(stockId)
        ),
};

export default medicineStockApi;