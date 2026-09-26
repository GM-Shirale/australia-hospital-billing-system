import axiosClient from '../services/axiosClient';
import API_URLS from './url';

const pharmacyChargeApi = {
    create: (data) =>
        axiosClient.post(
            API_URLS.PHARMACY_CHARGE.CREATE,
            data
        ),

    getById: (pharmacyChargeId) =>
        axiosClient.get(
            API_URLS.PHARMACY_CHARGE.GET_BY_ID(pharmacyChargeId)
        ),

    getByNumber: (chargeNumber) =>
        axiosClient.get(
            API_URLS.PHARMACY_CHARGE.GET_BY_NUMBER(chargeNumber)
        ),

    getByDispensing: (dispensingId) =>
        axiosClient.get(
            API_URLS.PHARMACY_CHARGE.GET_BY_DISPENSING(dispensingId)
        ),

    getByPatient: (patientId) =>
        axiosClient.get(
            API_URLS.PHARMACY_CHARGE.GET_BY_PATIENT(patientId)
        ),

    getByMedicine: (medicineId) =>
        axiosClient.get(
            API_URLS.PHARMACY_CHARGE.GET_BY_MEDICINE(medicineId)
        ),

    getByStatus: (status) =>
        axiosClient.get(
            API_URLS.PHARMACY_CHARGE.GET_BY_STATUS(status)
        ),

    getByBillingType: (billingType) =>
        axiosClient.get(
            API_URLS.PHARMACY_CHARGE.GET_BY_BILLING_TYPE(billingType)
        ),
};

export default pharmacyChargeApi;