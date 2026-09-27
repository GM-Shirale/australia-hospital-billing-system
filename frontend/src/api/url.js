const API_URLS = {

    // =========================
    // AUTH - /api
    // =========================
    AUTH: {
        ADMIN_LOGIN: '/auth/admin/login',
        USER_LOGIN: '/auth/user/login',
        LOGOUT: '/auth/logout',
    },

    // =========================
    // ADMIN - /api
    // =========================
    ADMIN: {
        CREATE: '/admin',
        GET_BY_ID: (adminId) => `/admin/${adminId}`,
        UPDATE: (adminId) => `/admin/${adminId}`,
        ACTIVATE: (adminId) => `/admin/${adminId}/activate`,
        DEACTIVATE: (adminId) => `/admin/${adminId}/deactivate`,
    },

    // =========================
    // USER - /api
    // =========================
    USER: {
        CREATE: '/admin/users',
        GET_ALL: '/admin/users',
        GET_BY_ID: (userId) => `/admin/users/${userId}`,
        UPDATE: (userId) => `/admin/users/${userId}`,
        ACTIVATE: (userId) => `/admin/users/${userId}/activate`,
        DEACTIVATE: (userId) => `/admin/users/${userId}/deactivate`,
        DELETE: (userId) => `/admin/users/${userId}`,
    },

    // =========================
    // PATIENT - /api
    // =========================
    PATIENT: {
        CREATE: '/patients',
        GET_ALL: '/patients',
        GET_BY_ID: (patientId) => `/patients/${patientId}`,
        UPDATE: (patientId) => `/patients/${patientId}`,
        DELETE: (patientId) => `/patients/${patientId}`,
    },

    // =========================
    // PATIENT ADDRESS - /api
    // =========================
    PATIENT_ADDRESS: {
        CREATE: (patientId) =>
            `/patients/${patientId}/addresses`,

        GET_BY_ID: (addressId) =>
            `/patients/addresses/${addressId}`,

        GET_BY_PATIENT: (patientId) =>
            `/patients/${patientId}/addresses`,

        UPDATE: (addressId) =>
            `/patients/addresses/${addressId}`,

        DELETE: (addressId) =>
            `/patients/addresses/${addressId}`,
    },

    // =========================
    // PATIENT DOCUMENT - /api
    // =========================
    PATIENT_DOCUMENT: {
        UPLOAD: (patientId) =>
            `/patients/${patientId}/documents`,

        GET_BY_ID: (documentId) =>
            `/patients/documents/${documentId}`,

        GET_BY_PATIENT: (patientId) =>
            `/patients/${patientId}/documents`,

        UPDATE: (documentId) =>
            `/patients/documents/${documentId}`,

        UPDATE_VERIFICATION: (documentId) =>
            `/patients/${documentId}/verification`,

        DELETE: (documentId) =>
            `/patients/documents/${documentId}`,
    },

    // =========================
    // ADMISSION - /api
    // =========================
    ADMISSION: {
        CREATE_FOR_PATIENT: (patientId) =>
            `/admissions/patient/${patientId}`,

        GET_BY_ID: (admissionId) =>
            `/admissions/${admissionId}`,

        GET_ALL: '/admissions',

        GET_BY_PATIENT: (patientId) =>
            `/admissions/patient/${patientId}`,

        UPDATE: (admissionId) =>
            `/admissions/${admissionId}`,

        DISCHARGE: (admissionId) =>
            `/admissions/${admissionId}/discharge`,

        DELETE: (admissionId) =>
            `/admissions/${admissionId}`,
    },

    // =========================
    // BILL - /api
    // =========================
    BILL: {
        CREATE_FOR_PATIENT: (patientId) =>
            `/bills/patient/${patientId}`,

        GET_BY_ID: (billId) =>
            `/bills/${billId}`,

        GET_ALL: '/bills',

        GET_BY_PATIENT: (patientId) =>
            `/bills/patient/${patientId}`,

        UPDATE: (billId) =>
            `/bills/${billId}`,

        DELETE: (billId) =>
            `/bills/${billId}`,

        PDF: (billId) =>
            `/bills/${billId}/pdf`,
    },

    // =========================
    // BILL ITEM - /api
    // =========================
    BILL_ITEM: {
        CREATE_FOR_BILL: (billId) =>
            `/bill-items/bill/${billId}`,

        GET_BY_ID: (billItemId) =>
            `/bill-items/${billItemId}`,

        GET_BY_BILL: (billId) =>
            `/bill-items/bill/${billId}`,

        UPDATE: (billItemId) =>
            `/bill-items/${billItemId}`,

        DELETE: (billItemId) =>
            `/bill-items/${billItemId}`,
    },

    // =========================
    // BILLING SUMMARY - /api
    // =========================
    BILLING_SUMMARY: {
        GET_HOSPITAL_SUMMARY: '/billing-summary',

        GET_PATIENT_SUMMARY: (patientId) =>
            `/billing-summary/patient/${patientId}`,
    },

    // =========================
    // INVOICE - /api
    // =========================
    INVOICE: {
        CREATE: '/invoices',

        GET_BY_ID: (invoiceId) =>
            `/invoices/${invoiceId}`,

        GET_BY_BILL: (billId) =>
            `/invoices/bill/${billId}`,

        GET_ALL: '/invoices',
    },

    // =========================
    // PAYMENT - /api
    // =========================
    PAYMENT: {
        CREATE: '/payments',

        GET_BY_ID: (paymentId) =>
            `/payments/${paymentId}`,

        GET_BY_BILL: (billId) =>
            `/payments/bill/${billId}`,

        GET_ALL: '/payments',
    },

    // =========================
    // PAYMENT TRANSACTION - /api
    // =========================
    PAYMENT_TRANSACTION: {
        CREATE: '/payment-transactions',

        GET_BY_ID: (transactionId) =>
            `/payment-transactions/${transactionId}`,

        GET_BY_PAYMENT: (paymentId) =>
            `/payment-transactions/payment/${paymentId}`,
    },

    // =========================
    // REFUND - /api
    // =========================
    REFUND: {
        CREATE: '/refunds',

        GET_BY_ID: (refundId) =>
            `/refunds/${refundId}`,

        GET_BY_TRANSACTION: (transactionId) =>
            `/refunds/transaction/${transactionId}`,

        GET_ALL: '/refunds',
    },

    // =========================
    // LAB ORDER - /api
    // =========================
    LAB_ORDER: {
        CREATE: '/laboratory/order',

        GET_BY_ID: (id) =>
            `/laboratory/order/${id}`,

        GET_ALL: '/laboratory/order',

        UPDATE: (id) =>
            `/laboratory/order/${id}`,

        DELETE: (id) =>
            `/laboratory/order/${id}`,
    },

    // =========================
    // LAB ORDER ITEM - /api
    // =========================
    LAB_ORDER_ITEM: {
        CREATE: '/laboratory/order-items',

        GET_BY_ID: (id) =>
            `/laboratory/order-items/${id}`,

        GET_ALL: '/laboratory/order-items',

        GET_BY_ORDER: (labOrderId) =>
            `/laboratory/order-items/order/${labOrderId}`,

        UPDATE: (id) =>
            `/laboratory/order-items/${id}`,

        DELETE: (id) =>
            `/laboratory/order-items/${id}`,
    },

    // =========================
    // LAB TEST - /api
    // =========================
    LAB_TEST: {
        CREATE: '/laboratory/tests',

        GET_BY_ID: (id) =>
            `/laboratory/tests/${id}`,

        GET_ALL: '/laboratory/tests',

        UPDATE: (id) =>
            `/laboratory/tests/${id}`,

        DELETE: (id) =>
            `/laboratory/tests/${id}`,
    },

    // =========================
    // LAB PARAMETER - /api
    // =========================
    LAB_PARAMETER: {
        CREATE: '/laboratory/parameters',

        GET_BY_ID: (id) =>
            `/laboratory/parameters/${id}`,

        GET_ALL: '/laboratory/parameters',

        GET_BY_TEST: (labTestId) =>
            `/laboratory/parameters/lab-test/${labTestId}`,

        UPDATE: (id) =>
            `/laboratory/parameters/${id}`,

        DELETE: (id) =>
            `/laboratory/parameters/${id}`,
    },

    // =========================
    // LAB SAMPLE - /api
    // =========================
    LAB_SAMPLE: {
        CREATE: '/laboratory/samples',

        GET_BY_ID: (id) =>
            `/laboratory/samples/${id}`,

        GET_ALL: '/laboratory/samples',

        GET_BY_ORDER: (labOrderId) =>
            `/laboratory/samples/order/${labOrderId}`,

        UPDATE: (id) =>
            `/laboratory/samples/${id}`,

        DELETE: (id) =>
            `/laboratory/samples/${id}`,
    },

    // =========================
    // LAB RESULT - /api
    // =========================
    LAB_RESULT: {
        CREATE: '/laboratory/results',

        GET_BY_ID: (id) =>
            `/laboratory/results/${id}`,

        GET_ALL: '/laboratory/results',

        GET_BY_SAMPLE: (labSampleId) =>
            `/laboratory/results/sample/${labSampleId}`,

        GET_BY_TEST: (labTestId) =>
            `/laboratory/results/test/${labTestId}`,

        UPDATE: (id) =>
            `/laboratory/results/${id}`,

        DELETE: (id) =>
            `/laboratory/results/${id}`,
    },

    // =========================
    // LAB RESULT VALUE - /api
    // =========================
    LAB_RESULT_VALUE: {
        CREATE: '/laboratory/result-values',

        GET_BY_ID: (id) =>
            `/laboratory/result-values/${id}`,

        GET_ALL: '/laboratory/result-values',

        GET_BY_RESULT: (labResultId) =>
            `/laboratory/result-values/result/${labResultId}`,

        GET_BY_PARAMETER: (labParameterId) =>
            `/laboratory/result-values/parameter/${labParameterId}`,

        UPDATE: (id) =>
            `/laboratory/result-values/${id}`,

        DELETE: (id) =>
            `/laboratory/result-values/${id}`,
    },

    // =========================
    // LAB REPORT - /api
    // =========================
    LAB_REPORT: {
        CREATE: '/laboratory/reports',

        GET_BY_ID: (id) =>
            `/laboratory/reports/${id}`,

        GET_BY_NUMBER: (reportNumber) =>
            `/laboratory/reports/number/${reportNumber}`,

        GET_BY_ORDER: (labOrderId) =>
            `/laboratory/reports/order/${labOrderId}`,

        GET_BY_PATIENT: (patientId) =>
            `/laboratory/reports/patient/${patientId}`,

        GET_BY_STATUS: (status) =>
            `/laboratory/reports/status/${status}`,

        UPDATE: (id) =>
            `/laboratory/reports/${id}`,

        DELETE: (id) =>
            `/laboratory/reports/${id}`,

        PDF: (id) =>
            `/laboratory/reports/${id}/pdf`,
    },

    // =========================
    // LAB CHARGE - /api
    // =========================
    LAB_CHARGE: {
        CREATE: '/laboratory/charges',

        GET_BY_ID: (id) =>
            `/laboratory/charges/${id}`,

        GET_BY_NUMBER: (chargeNumber) =>
            `/laboratory/charges/number/${chargeNumber}`,

        GET_BY_ORDER_ITEM: (labOrderItemId) =>
            `/laboratory/charges/order-item/${labOrderItemId}`,

        GET_BY_STATUS: (status) =>
            `/laboratory/charges/status/${status}`,

        GET_BY_BILLING_TYPE: (billingType) =>
            `/laboratory/charges/billing-type/${billingType}`,

        GET_TOTAL_PROVIDER_CHARGE: (labOrderId) =>
            `/laboratory/charges/order/${labOrderId}/total-provider-charge`,

        GET_TOTAL_MEDICARE_BENEFIT: (labOrderId) =>
            `/laboratory/charges/order/${labOrderId}/total-medicare-benefit`,

        GET_TOTAL_PATIENT_AMOUNT: (labOrderId) =>
            `/laboratory/charges/order/${labOrderId}/total-patient-amount`,

        UPDATE: (id) =>
            `/laboratory/charges/${id}`,

        DELETE: (id) =>
            `/laboratory/charges/${id}`,
    },

    // =========================
    // LAB VERIFICATION - /api
    // =========================
    LAB_VERIFICATION: {
        CREATE: '/laboratory/verifications',

        GET_BY_ID: (id) =>
            `/laboratory/verifications/${id}`,

        GET_BY_RESULT: (labResultId) =>
            `/laboratory/verifications/result/${labResultId}`,

        GET_BY_STATUS: (status) =>
            `/laboratory/verifications/status/${status}`,

        GET_BY_VERIFIED_BY: (verifiedBy) =>
            `/laboratory/verifications/verified-by/${verifiedBy}`,

        UPDATE: (id) =>
            `/laboratory/verifications/${id}`,

        DELETE: (id) =>
            `/laboratory/verifications/${id}`,
    },

    // =========================
    // DISPENSING - /api
    // =========================
    DISPENSING: {
        CREATE: '/pharmacy/dispensing',

        GET_BY_ID: (dispensingId) =>
            `/pharmacy/dispensing/${dispensingId}`,

        GET_BY_PRESCRIPTION_ITEM: (prescriptionItemId) =>
            `/pharmacy/dispensing/prescription-item/${prescriptionItemId}`,

        GET_BY_STOCK: (stockId) =>
            `/pharmacy/dispensing/stock/${stockId}`,

        GET_BY_PRESCRIPTION: (prescriptionId) =>
            `/pharmacy/dispensing/prescription/${prescriptionId}`,
    },

    // =========================
    // MEDICINE BATCH - /api
    // =========================
    MEDICINE_BATCH: {
        CREATE: '/pharmacy/medicine-batches',

        GET_BY_ID: (batchId) =>
            `/pharmacy/medicine-batches/${batchId}`,

        GET_ALL: '/pharmacy/medicine-batches',

        GET_BY_MEDICINE: (medicineId) =>
            `/pharmacy/medicine-batches/medicine/${medicineId}`,

        UPDATE: (batchId) =>
            `/pharmacy/medicine-batches/${batchId}`,
    },

    // =========================
    // MEDICINE - /api
    // =========================
    MEDICINE: {
        CREATE: '/pharmacy/medicines',

        GET_BY_ID: (medicineId) =>
            `/pharmacy/medicines/${medicineId}`,

        GET_ALL: '/pharmacy/medicines',

        SEARCH: '/pharmacy/medicines',

        UPDATE: (medicineId) =>
            `/pharmacy/medicines/${medicineId}`,

        UPDATE_STATUS: (medicineId) =>
            `/pharmacy/medicines/${medicineId}/status`,
    },

    // =========================
    // MEDICINE STOCK - /api
    // =========================
    MEDICINE_STOCK: {
        CREATE: '/pharmacy/stocks',

        GET_BY_ID: (stockId) =>
            `/pharmacy/stocks/${stockId}`,

        GET_BY_MEDICINE: (medicineId) =>
            `/pharmacy/stocks/medicine/${medicineId}`,

        GET_BY_BATCH: (batchId) =>
            `/pharmacy/stocks/batch/${batchId}`,

        GET_ACTIVE: '/pharmacy/stocks/active',

        GET_LOW_STOCK: '/pharmacy/stocks/low-stock',

        UPDATE: (stockId) =>
            `/pharmacy/stocks/${stockId}`,

        DELETE: (stockId) =>
            `/pharmacy/stocks/${stockId}`,
    },

    // =========================
    // PHARMACY CHARGE - /api
    // =========================
    PHARMACY_CHARGE: {
        CREATE: '/pharmacy/charges',

        GET_BY_ID: (pharmacyChargeId) =>
            `/pharmacy/charges/${pharmacyChargeId}`,

        GET_BY_NUMBER: (chargeNumber) =>
            `/pharmacy/charges/number/${chargeNumber}`,

        GET_BY_DISPENSING: (dispensingId) =>
            `/pharmacy/charges/dispensing/${dispensingId}`,

        GET_BY_PATIENT: (patientId) =>
            `/pharmacy/charges/patient/${patientId}`,

        GET_BY_MEDICINE: (medicineId) =>
            `/pharmacy/charges/medicine/${medicineId}`,

        GET_BY_STATUS: (status) =>
            `/pharmacy/charges/status/${status}`,

        GET_BY_BILLING_TYPE: (billingType) =>
            `/pharmacy/charges/billing-type/${billingType}`,
    },

    // =========================
    // PRESCRIPTION - /api
    // =========================
    PRESCRIPTION: {
        CREATE: '/pharmacy/prescriptions',

        GET_BY_ID: (prescriptionId) =>
            `/pharmacy/prescriptions/${prescriptionId}`,

        GET_ALL: '/pharmacy/prescriptions',

        UPDATE: (prescriptionId) =>
            `/pharmacy/prescriptions/${prescriptionId}`,
    },

    // =========================
    // PRESCRIPTION ITEM - /api
    // =========================
    PRESCRIPTION_ITEM: {
        CREATE: '/pharmacy/prescription-items',

        GET_BY_ID: (prescriptionItemId) =>
            `/pharmacy/prescription-items/${prescriptionItemId}`,

        GET_BY_PRESCRIPTION: (prescriptionId) =>
            `/pharmacy/prescription-items/prescription/${prescriptionId}`,

        GET_BY_MEDICINE: (medicineId) =>
            `/pharmacy/prescription-items/medicine/${medicineId}`,

        UPDATE: (prescriptionItemId) =>
            `/pharmacy/prescription-items/${prescriptionItemId}`,

        DELETE: (prescriptionItemId) =>
            `/pharmacy/prescription-items/${prescriptionItemId}`,
    },

    // =========================
    // DEPARTMENT - /api/v1
    // =========================
    DEPARTMENT: {
        CREATE: '/v1/departments',

        GET_ALL: '/v1/departments',

        GET_BY_ID: (departmentId) =>
            `/v1/departments/${departmentId}`,

        UPDATE: (departmentId) =>
            `/v1/departments/${departmentId}`,

        DELETE: (departmentId) =>
            `/v1/departments/${departmentId}`,
    },

    // =========================
    // DOCTOR - /api/v1
    // =========================
    DOCTOR: {
        CREATE: '/v1/doctors',

        GET_ALL: '/v1/doctors',

        GET_BY_ID: (doctorId) =>
            `/v1/doctors/${doctorId}`,

        GET_BY_PROVIDER_NUMBER: '/v1/doctors/by-provider-number',

        GET_BY_DEPARTMENT: (departmentId) =>
            `/v1/doctors/department/${departmentId}`,

        UPDATE: (doctorId) =>
            `/v1/doctors/${doctorId}`,

        DELETE: (doctorId) =>
            `/v1/doctors/${doctorId}`,

        CONSULTATION_CHARGE: '/v1/doctors/consultation-charge',
    },

    // =========================
    // INSURANCE POLICY - /api/v1
    // =========================
    INSURANCE_POLICY: {
        CREATE: '/v1/insurance/policies',

        GET_BY_ID: (policyId) =>
            `/v1/insurance/policies/${policyId}`,

        GET_BY_PATIENT: (patientId) =>
            `/v1/insurance/policies/patient/${patientId}`,
    },

    // =========================
    // ROOM - /api/v1
    // =========================
    ROOM: {
        CREATE: '/v1/rooms',

        GET_BY_ID: (roomId) =>
            `/v1/rooms/${roomId}`,

        GET_ALL: '/v1/rooms',

        GET_AVAILABLE: '/v1/rooms/available',

        GET_BY_DEPARTMENT: (departmentId) =>
            `/v1/rooms/department/${departmentId}`,

        UPDATE: (roomId) =>
            `/v1/rooms/${roomId}`,

        UPDATE_STATUS: (roomId) =>
            `/v1/rooms/${roomId}/status`,

        DELETE: (roomId) =>
            `/v1/rooms/${roomId}`,
    },
};

export default API_URLS;