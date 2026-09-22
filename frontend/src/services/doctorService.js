import apiClient from "./apiClient";

const doctorService = {
  getAllDoctors: (tenantId) => {
    return apiClient.get("/api/v1/doctors", {
      headers: {
        "X-Tenant-ID": tenantId,
      },
    });
  },

  getDoctorById: (doctorId, tenantId) => {
    return apiClient.get(`/api/v1/doctors/${doctorId}`, {
      headers: {
        "X-Tenant-ID": tenantId,
      },
    });
  },

  getDoctorByProviderNumber: (providerNo) => {
    return apiClient.get("/api/v1/doctors/by-provider-number", {
      params: {
        providerNo,
      },
    });
  },

  getDoctorsByDepartment: (departmentId, tenantId) => {
    return apiClient.get(
      `/api/v1/doctors/department/${departmentId}`,
      {
        headers: {
          "X-Tenant-ID": tenantId,
        },
      }
    );
  },

  createDoctor: (doctorData) => {
    return apiClient.post("/api/v1/doctors", doctorData);
  },

  updateDoctor: (doctorId, tenantId, doctorData) => {
    return apiClient.put(
      `/api/v1/doctors/${doctorId}`,
      doctorData,
      {
        headers: {
          "X-Tenant-ID": tenantId,
        },
      }
    );
  },

  deleteDoctor: (doctorId, tenantId) => {
    return apiClient.delete(
      `/api/v1/doctors/${doctorId}`,
      {
        headers: {
          "X-Tenant-ID": tenantId,
        },
      }
    );
  },

  getConsultationCharge: (requestData) => {
    return apiClient.post(
      "/api/v1/doctors/consultation-charge",
      requestData
    );
  },
};

export default doctorService;