import axiosClient from '../services/axiosClient';
import API_URLS from './url';   // fixed: was './urls'

const patientApi = {
  // POST /api/patients
  create: (data) =>
    axiosClient.post(API_URLS.PATIENT.CREATE, data),

  // GET /api/patients
  getAll: () =>
    axiosClient.get(API_URLS.PATIENT.GET_ALL),

  // GET /api/patients/{patientId}
  getById: (patientId) =>
    axiosClient.get(API_URLS.PATIENT.GET_BY_ID(patientId)),

  // PUT /api/patients/{patientId}
  update: (patientId, data) =>
    axiosClient.put(API_URLS.PATIENT.UPDATE(patientId), data),

  // DELETE /api/patients/{patientId}
  remove: (patientId) =>
    axiosClient.delete(API_URLS.PATIENT.DELETE(patientId)),
};

export default patientApi;
