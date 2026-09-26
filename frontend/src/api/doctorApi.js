import axiosClient from '../services/axiosClient';
import API_URLS from './urls';

const doctorApi = {
    create: (data) =>
        axiosClient.post(
            API_URLS.DOCTOR.CREATE,
            data
        ),

    getAll: () =>
        axiosClient.get(
            API_URLS.DOCTOR.GET_ALL
        ),

    getById: (doctorId) =>
        axiosClient.get(
            API_URLS.DOCTOR.GET_BY_ID(doctorId)
        ),

    getByProviderNumber: (providerNo) =>
        axiosClient.get(
            API_URLS.DOCTOR.GET_BY_PROVIDER_NUMBER,
            {
                params: { providerNo },
            }
        ),

    getByDepartment: (departmentId) =>
        axiosClient.get(
            API_URLS.DOCTOR.GET_BY_DEPARTMENT(departmentId)
        ),

    update: (doctorId, data) =>
        axiosClient.put(
            API_URLS.DOCTOR.UPDATE(doctorId),
            data
        ),

    delete: (doctorId) =>
        axiosClient.delete(
            API_URLS.DOCTOR.DELETE(doctorId)
        ),

    consultationCharge: (data) =>
        axiosClient.post(
            API_URLS.DOCTOR.CONSULTATION_CHARGE,
            data
        ),
};

export default doctorApi;