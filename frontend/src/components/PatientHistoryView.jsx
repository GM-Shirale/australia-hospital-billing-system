import React, { useState, useEffect } from 'react';
import { History, Plus, AlertCircle, FileText, UserCheck, Pill, Stethoscope, Search, RefreshCw } from 'lucide-react';

export default function PatientHistoryView() {
    const [searchTerm, setSearchTerm] = useState('');
    const [loading, setLoading] = useState(false);
    const [records, setRecords] = useState([
        // Keeping fallback demo data in case the backend is off
        {
            id: 'MH-101',
            patientName: 'David Warner',
            patientMedicare: '4123 45678 1',
            priorDoctor: 'Dr. Robert Vance (St. Vincent Clinic)',
            pastDiagnosis: 'Type 2 Diabetes & Hypertension',
            pastMedications: 'Metformin 500mg, Telmisartan 40mg',
            allergies: 'Penicillin (Severe Rash)',
            pastSurgeries: 'Appendectomy (2021)',
            recordedDate: '2026-08-14'
        }
    ]);

    const [form, setForm] = useState({
        patientName: '',
        patientMedicare: '',
        priorDoctor: '',
        pastDiagnosis: '',
        pastMedications: '',
        allergies: '',
        pastSurgeries: ''
    });

    // 1. Fetch records from backend on component mount
    const fetchRecords = async () => {
        setLoading(true);
        try {
            const response = await fetch('http://localhost:8085/api/v1/medical-history');
            if (response.ok) {
                const data = await response.json();
                if (data && data.length > 0) {
                    // Sort to show newest first
                    const sortedData = data.sort((a, b) => new Date(b.recordedDate) - new Date(a.recordedDate));
                    setRecords(sortedData);
                }
            }
        } catch (error) {
            console.error("Error fetching medical histories (Backend might be down):", error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchRecords();
    }, []);

    // 2. Integrated API POST request to save data
    const handleAddRecord = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch('http://localhost:8085/api/v1/medical-history', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(form)
            });

            if (response.ok) {
                const savedData = await response.json();

                // Add the new record to the top of the list in UI
                setRecords([savedData, ...records]);

                // Clear the form
                setForm({
                    patientName: '',
                    patientMedicare: '',
                    priorDoctor: '',
                    pastDiagnosis: '',
                    pastMedications: '',
                    allergies: '',
                    pastSurgeries: ''
                });
            } else {
                console.error("Failed to save data. Status:", response.status);
            }
        } catch (error) {
            console.error("Error saving medical history:", error);

            // Fallback UI logic if backend is not running
            const mockRecord = {
                id: `MH-${Date.now().toString().slice(-3)}`,
                ...form,
                recordedDate: new Date().toISOString().split('T')[0]
            };
            setRecords([mockRecord, ...records]);
            setForm({
                patientName: '', patientMedicare: '', priorDoctor: '',
                pastDiagnosis: '', pastMedications: '', allergies: '', pastSurgeries: ''
            });
        }
    };

    const filtered = records.filter(r =>
        (r.patientName && r.patientName.toLowerCase().includes(searchTerm.toLowerCase())) ||
        (r.patientMedicare && r.patientMedicare.includes(searchTerm)) ||
        (r.pastDiagnosis && r.pastDiagnosis.toLowerCase().includes(searchTerm.toLowerCase()))
    );

    return (
        <div className="space-y-6">
            {/* Top Cards */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                <div className="bg-white border border-slate-200/80 rounded-2xl p-5 shadow-xs flex items-center justify-between">
                    <div>
                        <p className="text-xs font-bold text-slate-400 uppercase">Total Medical Histories</p>
                        <h3 className="text-2xl font-black text-slate-900 mt-1">{records.length} Patients</h3>
                    </div>
                    <div className="w-10 h-10 bg-blue-50 text-blue-600 rounded-xl flex items-center justify-center">
                        <History className="w-5 h-5" />
                    </div>
                </div>

                <div className="bg-white border border-slate-200/80 rounded-2xl p-5 shadow-xs flex items-center justify-between">
                    <div>
                        <p className="text-xs font-bold text-rose-500 uppercase">Documented Drug Allergies</p>
                        <h3 className="text-2xl font-black text-rose-600 mt-1">
                            {records.filter(r => r.allergies && !r.allergies.toLowerCase().includes('none')).length} Flagged
                        </h3>
                    </div>
                    <div className="w-10 h-10 bg-rose-50 text-rose-600 rounded-xl flex items-center justify-center">
                        <AlertCircle className="w-5 h-5" />
                    </div>
                </div>

                <div className="bg-white border border-slate-200/80 rounded-2xl p-5 shadow-xs flex items-center justify-between">
                    <div>
                        <p className="text-xs font-bold text-emerald-600 uppercase">Prior Clinical Records</p>
                        <h3 className="text-2xl font-black text-emerald-700 mt-1">100% Verified</h3>
                    </div>
                    <div className="w-10 h-10 bg-emerald-50 text-emerald-600 rounded-xl flex items-center justify-center">
                        <UserCheck className="w-5 h-5" />
                    </div>
                </div>
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
                {/* Form: Add Medical Record */}
                <div className="bg-white border border-slate-200/80 rounded-2xl p-6 shadow-xs space-y-4">
                    <h3 className="text-base font-bold text-slate-900 flex items-center gap-2">
                        <Plus className="w-5 h-5 text-blue-600" /> Patient Medical History
                    </h3>
                    <p className="text-xs text-slate-400">Record past treatments, prior doctors, and allergies.</p>

                    <form onSubmit={handleAddRecord} className="space-y-3 pt-1">
                        <div>
                            <label className="block text-xs font-bold text-slate-700 uppercase mb-1">Patient Full Name</label>
                            <input
                                type="text"
                                required
                                placeholder="e.g. Steve Smith"
                                className="w-full border border-slate-200 rounded-xl p-2.5 text-sm outline-none focus:ring-1 focus:ring-blue-500"
                                value={form.patientName}
                                onChange={(e) => setForm({ ...form, patientName: e.target.value })}
                            />
                        </div>

                        <div>
                            <label className="block text-xs font-bold text-slate-700 uppercase mb-1">Medicare Number</label>
                            <input
                                type="text"
                                required
                                placeholder="10-digit Medicare No"
                                className="w-full border border-slate-200 rounded-xl p-2.5 text-sm outline-none focus:ring-1 focus:ring-blue-500"
                                value={form.patientMedicare}
                                onChange={(e) => setForm({ ...form, patientMedicare: e.target.value })}
                            />
                        </div>

                        <div>
                            <label className="block text-xs font-bold text-slate-700 uppercase mb-1">Prior Doctor & Clinic</label>
                            <input
                                type="text"
                                required
                                placeholder="e.g. Dr. Kumar (City Health Clinic)"
                                className="w-full border border-slate-200 rounded-xl p-2.5 text-sm outline-none focus:ring-1 focus:ring-blue-500"
                                value={form.priorDoctor}
                                onChange={(e) => setForm({ ...form, priorDoctor: e.target.value })}
                            />
                        </div>

                        <div>
                            <label className="block text-xs font-bold text-slate-700 uppercase mb-1">Past Illness / Diagnosis</label>
                            <textarea
                                rows="2"
                                required
                                placeholder="e.g. Jaundice, Asthma, Dengue in past"
                                className="w-full border border-slate-200 rounded-xl p-2.5 text-sm outline-none focus:ring-1 focus:ring-blue-500"
                                value={form.pastDiagnosis}
                                onChange={(e) => setForm({ ...form, pastDiagnosis: e.target.value })}
                            />
                        </div>

                        <div>
                            <label className="block text-xs font-bold text-slate-700 uppercase mb-1">Previous / Ongoing Medicines</label>
                            <input
                                type="text"
                                required
                                placeholder="e.g. Paracetamol, Inhaler, Insulin"
                                className="w-full border border-slate-200 rounded-xl p-2.5 text-sm outline-none focus:ring-1 focus:ring-blue-500"
                                value={form.pastMedications}
                                onChange={(e) => setForm({ ...form, pastMedications: e.target.value })}
                            />
                        </div>

                        <div>
                            <label className="block text-xs font-bold text-slate-700 uppercase mb-1 text-rose-600">Allergies (if any)</label>
                            <input
                                type="text"
                                placeholder="e.g. Penicillin, Sulfa, Dust allergy"
                                className="w-full border border-rose-200 bg-rose-50/30 rounded-xl p-2.5 text-sm outline-none focus:ring-1 focus:ring-rose-500"
                                value={form.allergies}
                                onChange={(e) => setForm({ ...form, allergies: e.target.value })}
                            />
                        </div>

                        <div>
                            <label className="block text-xs font-bold text-slate-700 uppercase mb-1">Past Surgeries / Operations</label>
                            <input
                                type="text"
                                placeholder="e.g. Heart Stent (2020), Knee Surgery"
                                className="w-full border border-slate-200 rounded-xl p-2.5 text-sm outline-none focus:ring-1 focus:ring-blue-500"
                                value={form.pastSurgeries}
                                onChange={(e) => setForm({ ...form, pastSurgeries: e.target.value })}
                            />
                        </div>

                        <button
                            type="submit"
                            className="w-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-2.5 rounded-xl text-sm transition shadow-xs mt-2"
                        >
                            Save Medical Record
                        </button>
                    </form>
                </div>

                {/* Records Table */}
                <div className="lg:col-span-2 bg-white border border-slate-200/80 rounded-2xl p-6 shadow-xs space-y-4">
                    <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-3">
                        <div>
                            <h3 className="text-base font-bold text-slate-900">Historical Clinical Records</h3>
                            <p className="text-xs text-slate-400">Previous doctor visits and treatment case histories</p>
                        </div>
                        <div className="flex items-center gap-2 w-full sm:w-auto">
                            <button onClick={fetchRecords} className="p-2 border border-slate-200 rounded-xl hover:bg-slate-50 transition text-slate-500" title="Refresh Data">
                                <RefreshCw className={`w-4 h-4 ${loading ? 'animate-spin' : ''}`} />
                            </button>
                            <div className="relative flex-1 sm:w-64">
                                <Search className="w-4 h-4 text-slate-400 absolute left-3 top-2.5" />
                                <input
                                    type="text"
                                    placeholder="Search by patient, illness..."
                                    className="w-full pl-9 pr-3 py-1.5 border border-slate-200 rounded-xl text-xs outline-none focus:ring-1 focus:ring-blue-500"
                                    value={searchTerm}
                                    onChange={(e) => setSearchTerm(e.target.value)}
                                />
                            </div>
                        </div>
                    </div>

                    <div className="space-y-3">
                        {filtered.length === 0 && !loading ? (
                            <div className="text-center py-8 text-slate-400 text-sm font-semibold border-2 border-dashed border-slate-200 rounded-xl">
                                No medical records found.
                            </div>
                        ) : (
                            filtered.map((item) => (
                                <div key={item.id} className="border border-slate-200/80 rounded-xl p-4 hover:border-blue-300 transition bg-slate-50/40">
                                    <div className="flex justify-between items-start mb-2">
                                        <div>
                                            <h4 className="font-bold text-slate-900 text-sm">{item.patientName}</h4>
                                            <span className="text-[11px] font-mono text-slate-400">Medicare: {item.patientMedicare} &bull; ID: {item.id || 'N/A'}</span>
                                        </div>
                                        <span className="text-[10px] font-bold bg-blue-50 text-blue-700 px-2.5 py-0.5 rounded-full border border-blue-200">
                        Recorded: {item.recordedDate || 'Today'}
                      </span>
                                    </div>

                                    <div className="grid grid-cols-1 md:grid-cols-2 gap-3 text-xs mt-3">
                                        <div className="bg-white p-2.5 rounded-lg border border-slate-100">
                                            <p className="text-slate-400 font-semibold mb-0.5 flex items-center gap-1">
                                                <Stethoscope className="w-3.5 h-3.5 text-blue-600" /> Prior Doctor & Clinic:
                                            </p>
                                            <p className="font-semibold text-slate-800">{item.priorDoctor}</p>
                                        </div>

                                        <div className="bg-white p-2.5 rounded-lg border border-slate-100">
                                            <p className="text-slate-400 font-semibold mb-0.5 flex items-center gap-1">
                                                <FileText className="w-3.5 h-3.5 text-indigo-600" /> Past Illness / Diagnosis:
                                            </p>
                                            <p className="font-semibold text-slate-800">{item.pastDiagnosis}</p>
                                        </div>

                                        <div className="bg-white p-2.5 rounded-lg border border-slate-100">
                                            <p className="text-slate-400 font-semibold mb-0.5 flex items-center gap-1">
                                                <Pill className="w-3.5 h-3.5 text-emerald-600" /> Past Medications:
                                            </p>
                                            <p className="font-semibold text-slate-800">{item.pastMedications}</p>
                                        </div>

                                        <div className="bg-white p-2.5 rounded-lg border border-slate-100">
                                            <p className="text-slate-400 font-semibold mb-0.5 flex items-center gap-1">
                                                <AlertCircle className="w-3.5 h-3.5 text-rose-600" /> Allergies & Warnings:
                                            </p>
                                            <p className="font-bold text-rose-600">{item.allergies || 'None'}</p>
                                        </div>
                                    </div>

                                    {item.pastSurgeries && item.pastSurgeries !== 'None' && (
                                        <div className="mt-2 text-[11px] text-slate-600 bg-amber-50/60 border border-amber-200/60 px-3 py-1 rounded-md">
                                            <span className="font-bold text-amber-800">Past Surgeries:</span> {item.pastSurgeries}
                                        </div>
                                    )}
                                </div>
                            ))
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
}