import axiosClient from "../services/axiosClient";
import API_URLS from "./urls";

const adminApi = {
  createAdmin: (data) => axiosClient.post(API_URLS.ADMIN.CREATE, data),

  getAdminById: (adminId) => axiosClient.get(API_URLS.ADMIN.GET_BY_ID(adminId)),

  updateAdmin: (adminId, data) =>
    axiosClient.put(API_URLS.ADMIN.UPDATE(adminId), data),

  activateAdmin: (adminId) => axiosClient.put(API_URLS.ADMIN.ACTIVATE(adminId)),

  deactivateAdmin: (adminId) =>
    axiosClient.put(API_URLS.ADMIN.DEACTIVATE(adminId)),
};

export default adminApi;
