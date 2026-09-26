import axiosClient from '../services/axiosClient';
import API_URLS from './url';

const labChargeApi = {
    create: (data) =>
        axiosClient.post(
            API_URLS.LAB_CHARGE.CREATE,
            data
        ),

    getById: (id) =>
        axiosClient.get(
            API_URLS.LAB_CHARGE.GET_BY_ID(id)
        ),

    getByNumber: (chargeNumber) =>
        axiosClient.get(
            API_URLS.LAB_CHARGE.GET_BY_NUMBER(chargeNumber)
        ),

    getByOrderItem: (labOrderItemId) =>
        axiosClient.get(
            API_URLS.LAB_CHARGE.GET_BY_ORDER_ITEM(labOrderItemId)
        ),

    getByStatus: (status) =>
        axiosClient.get(
            API_URLS.LAB_CHARGE.GET_BY_STATUS(status)
        ),

    getByBillingType: (billingType) =>
        axiosClient.get(
            API_URLS.LAB_CHARGE.GET_BY_BILLING_TYPE(billingType)
        ),

    getTotalProviderCharge: (labOrderId) =>
        axiosClient.get(
            API_URLS.LAB_CHARGE.GET_TOTAL_PROVIDER_CHARGE(labOrderId)
        ),

    getTotalMedicareBenefit: (labOrderId) =>
        axiosClient.get(
            API_URLS.LAB_CHARGE.GET_TOTAL_MEDICARE_BENEFIT(labOrderId)
        ),

    getTotalPatientAmount: (labOrderId) =>
        axiosClient.get(
            API_URLS.LAB_CHARGE.GET_TOTAL_PATIENT_AMOUNT(labOrderId)
        ),

    update: (id, data) =>
        axiosClient.put(
            API_URLS.LAB_CHARGE.UPDATE(id),
            data
        ),

    delete: (id) =>
        axiosClient.delete(
            API_URLS.LAB_CHARGE.DELETE(id)
        ),
};

export default labChargeApi;