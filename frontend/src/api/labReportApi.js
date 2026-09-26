import axiosClient from '../services/axiosClient';
import API_URLS from './urls';

const labReportApi = {
    create: (data) =>
        axiosClient.post(
            API_URLS.LAB_REPORT.CREATE,
            data
        ),

    getById: (id) =>
        axiosClient.get(
            API_URLS.LAB_REPORT.GET_BY_ID(id)
        ),

    getByNumber: (reportNumber) =>
        axiosClient.get(
            API_URLS.LAB_REPORT.GET_BY_NUMBER(reportNumber)
        ),

    getByOrder: (labOrderId) =>
        axiosClient.get(
            API_URLS.LAB_REPORT.GET_BY_ORDER(labOrderId)
        ),

    getByPatient: (patientId) =>
        axiosClient.get(
            API_URLS.LAB_REPORT.GET_BY_PATIENT(patientId)
        ),

    getByStatus: (status) =>
        axiosClient.get(
            API_URLS.LAB_REPORT.GET_BY_STATUS(status)
        ),

    update: (id, data) =>
        axiosClient.put(
            API_URLS.LAB_REPORT.UPDATE(id),
            data
        ),

    delete: (id) =>
        axiosClient.delete(
            API_URLS.LAB_REPORT.DELETE(id)
        ),

    getPdf: (id) =>
        axiosClient.get(
            API_URLS.LAB_REPORT.PDF(id),
            {
                responseType: 'blob',
            }
        ),
};

export default labReportApi;