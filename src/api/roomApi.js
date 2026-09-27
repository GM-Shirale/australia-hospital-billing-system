import axiosClient from "../services/axiosClient";
import API_URLS from "./urls";

const roomApi = {
  create: (data) => axiosClient.post(API_URLS.ROOM.CREATE, data),

  getAll: () => axiosClient.get(API_URLS.ROOM.GET_ALL),

  getById: (roomId) => axiosClient.get(API_URLS.ROOM.GET_BY_ID(roomId)),

  getAvailable: () => axiosClient.get(API_URLS.ROOM.GET_AVAILABLE),

  getByDepartment: (departmentId) =>
    axiosClient.get(API_URLS.ROOM.GET_BY_DEPARTMENT(departmentId)),

  update: (roomId, data) => axiosClient.put(API_URLS.ROOM.UPDATE(roomId), data),

  updateStatus: (roomId, status) =>
    axiosClient.patch(API_URLS.ROOM.UPDATE_STATUS(roomId), null, {
      params: { status },
    }),

  delete: (roomId) => axiosClient.delete(API_URLS.ROOM.DELETE(roomId)),
};

export default roomApi;
