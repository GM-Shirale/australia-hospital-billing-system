import React, { useState } from 'react';
import { Users, UserPlus, HeartPulse } from 'lucide-react';

const INITIAL_PATIENTS = [
    { id: '1', mrn: 'MRN-9021', name: 'James Wilson', gender: 'MALE', age: 45, medicareNumber: '2938475611', status: 'ADMITTED', assignedBed: 'ICU-101 (BED-B)' },
    { id: '2', mrn: 'MRN-8842', name: 'Sarah Jenkins', gender: 'FEMALE', age: 34, medicareNumber: '8839201948', status: 'OPD', assignedBed: 'N/A' }
];

export default function PatientsView() {
    const [patients, setPatients] = useState(INITIAL_PATIENTS);
    const [patientForm, setPatientForm] = useState({
        name: '', gender: 'MALE', age: '', medicareNumber: '', admissionType: 'OPD'
    });

    const handleRegisterPatient = (e) => {
        e.preventDefault();
        const newEntry = {
            id: Date.now().toString(),
            mrn: `MRN-${Math.floor(1000 + Math.random() * 9000)}`,
            name: patientForm.name,
            gender: patientForm.gender,
            age: patientForm.age,
            medicareNumber: patientForm.medicareNumber,
            status: patientForm.admissionType === 'IPD' ? 'ADMITTED' : 'OPD',
            assignedBed: patientForm.admissionType === 'IPD' ? 'GEN-204 (BED-01)' : 'N/A'
        };
        setPatients([...patients, newEntry]);
        setPatientForm({ name: '', gender: 'MALE', age: '', medicareNumber: '', admissionType: 'OPD' });
    };

    return (
        <div className="space-y-6">
            <div className="bg-white p-6 rounded-2xl border border-slate-200/70 shadow-xs">
                <h3 className="text-base font-bold text-slate-900 mb-3 flex items-center gap-2">
                    <UserPlus className="w-4 h-4 text-blue-600" /> Patient Registration & Admission (MBS)
                </h3>
                <form onSubmit={handleRegisterPatient} className="grid grid-cols-1 md:grid-cols-6 gap-3.5">
                    <input
                        type="text" required placeholder="Patient Full Name" className="border border-slate-200 rounded-xl px-3.5 py-2 text-sm outline-none"
                        value={patientForm.name} onChange={(e) => setPatientForm({...patientForm, name: e.target.value})}
                    />
                    <select
                        className="border border-slate-200 rounded-xl px-3 py-2 text-sm outline-none bg-white"
                        value={patientForm.gender} onChange={(e) => setPatientForm({...patientForm, gender: e.target.value})}>
                        <option value="MALE">Male</option>
                        <option value="FEMALE">Female</option>
                        <option value="OTHER">Other</option>
                    </select>
                    <input
                        type="number" required placeholder="Age" className="border border-slate-200 rounded-xl px-3.5 py-2 text-sm outline-none"
                        value={patientForm.age} onChange={(e) => setPatientForm({...patientForm, age: e.target.value})}
                    />
                    <input
                        type="text" required placeholder="Medicare Card No" className="border border-slate-200 rounded-xl px-3.5 py-2 text-sm font-mono outline-none"
                        value={patientForm.medicareNumber} onChange={(e) => setPatientForm({...patientForm, medicareNumber: e.target.value})}
                    />
                    <select
                        className="border border-slate-200 rounded-xl px-3 py-2 text-sm outline-none bg-white"
                        value={patientForm.admissionType} onChange={(e) => setPatientForm({...patientForm, admissionType: e.target.value})}>
                        <option value="OPD">OPD Consultation</option>
                        <option value="IPD">Inpatient Admission (IPD)</option>
                    </select>
                    <button type="submit" className="bg-blue-600 text-white px-4 py-2 rounded-xl text-sm font-semibold hover:bg-blue-700 transition">
                        Register
                    </button>
                </form>
            </div>

            <div className="bg-white rounded-2xl border border-slate-200/70 shadow-xs overflow-hidden">
                <div className="px-6 py-4 border-b border-slate-100 flex justify-between items-center">
                    <h4 className="font-bold text-slate-800 text-sm">Admitted & Active Patients Directory</h4>
                    <span className="text-xs text-slate-400">Total: {patients.length}</span>
                </div>
                <table className="w-full text-left text-sm">
                    <thead className="bg-slate-50/75 border-b border-slate-100 text-slate-500 text-xs uppercase font-semibold">
                    <tr>
                        <th className="px-6 py-3.5">MRN Code</th>
                        <th className="px-6 py-3.5">Patient Details</th>
                        <th className="px-6 py-3.5">Medicare Number</th>
                        <th className="px-6 py-3.5">Assigned Bed</th>
                        <th className="px-6 py-3.5">Admission State</th>
                    </tr>
                    </thead>
                    <tbody className="divide-y divide-slate-100">
                    {patients.map((p, idx) => (
                        <tr key={idx} className="hover:bg-slate-50/70 transition">
                            <td className="px-6 py-4 font-mono font-bold text-blue-600 text-xs">{p.mrn}</td>
                            <td className="px-6 py-4 font-semibold text-slate-900">{p.name} <span className="text-xs text-slate-400 font-normal">({p.gender}, {p.age}y)</span></td>
                            <td className="px-6 py-4 font-mono text-xs">{p.medicareNumber}</td>
                            <td className="px-6 py-4 text-xs font-semibold text-slate-700">{p.assignedBed}</td>
                            <td className="px-6 py-4">
                  <span className={`text-[11px] px-2.5 py-0.5 rounded-full font-bold ${
                      p.status === 'ADMITTED' ? 'bg-amber-50 text-amber-700 border border-amber-200' : 'bg-emerald-50 text-emerald-700 border border-emerald-200'
                  }`}>
                    {p.status}
                  </span>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
}