import axiosClient from '../services/axiosClient';
import API_URLS from './url';

const labVerificationApi = {
    create: (data) =>
        axiosClient.post(
            API_URLS.LAB_VERIFICATION.CREATE,
            data
        ),

    getById: (id) =>
        axiosClient.get(
            API_URLS.LAB_VERIFICATION.GET_BY_ID(id)
        ),

    getByResult: (labResultId) =>
        axiosClient.get(
            API_URLS.LAB_VERIFICATION.GET_BY_RESULT(labResultId)
        ),

    getByStatus: (status) =>
        axiosClient.get(
            API_URLS.LAB_VERIFICATION.GET_BY_STATUS(status)
        ),

    getByVerifiedBy: (verifiedBy) =>
        axiosClient.get(
            API_URLS.LAB_VERIFICATION.GET_BY_VERIFIED_BY(verifiedBy)
        ),

    update: (id, data) =>
        axiosClient.put(
            API_URLS.LAB_VERIFICATION.UPDATE(id),
            data
        ),

    delete: (id) =>
        axiosClient.delete(
            API_URLS.LAB_VERIFICATION.DELETE(id)
        ),
};

export default labVerificationApi;