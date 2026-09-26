import React, { useState } from 'react';
import { Plus } from 'lucide-react';

const INITIAL_DOCTORS = [
    { id: '1', firstName: 'Hillary', lastName: 'Quilton', specialization: 'Cardiology', medicareProviderNumber: '373cbr6737c3', consultationFee: 150.00 },
    { id: '2', firstName: 'Steve', lastName: 'Smith', specialization: 'Neurology', medicareProviderNumber: '243abc9981d2', consultationFee: 220.00 }
];

export default function DoctorsView() {
    const [doctors, setDoctors] = useState(INITIAL_DOCTORS);
    const [doctorForm, setDoctorForm] = useState({
        firstName: '', lastName: '', specialization: '', medicareProviderNumber: '', consultationFee: 150.00
    });

    const handleCreateDoctor = (e) => {
        e.preventDefault();
        setDoctors([...doctors, { ...doctorForm, id: Date.now().toString() }]);
        setDoctorForm({ firstName: '', lastName: '', specialization: '', medicareProviderNumber: '', consultationFee: 150.00 });
    };

    return (
        <div className="space-y-6">
            <div className="bg-white p-6 rounded-2xl border border-slate-200/70 shadow-xs">
                <h3 className="text-base font-bold text-slate-900 mb-3 flex items-center gap-2">
                    <Plus className="w-4 h-4 text-blue-600" /> Register Medical Practitioner
                </h3>
                <form onSubmit={handleCreateDoctor} className="grid grid-cols-1 md:grid-cols-5 gap-3.5">
                    <input
                        type="text" required placeholder="First Name" className="border border-slate-200 rounded-xl px-3.5 py-2 text-sm outline-none"
                        value={doctorForm.firstName} onChange={(e) => setDoctorForm({...doctorForm, firstName: e.target.value})}
                    />
                    <input
                        type="text" required placeholder="Last Name" className="border border-slate-200 rounded-xl px-3.5 py-2 text-sm outline-none"
                        value={doctorForm.lastName} onChange={(e) => setDoctorForm({...doctorForm, lastName: e.target.value})}
                    />
                    <input
                        type="text" required placeholder="Specialization" className="border border-slate-200 rounded-xl px-3.5 py-2 text-sm outline-none"
                        value={doctorForm.specialization} onChange={(e) => setDoctorForm({...doctorForm, specialization: e.target.value})}
                    />
                    <input
                        type="text" required placeholder="Medicare Provider No" className="border border-slate-200 rounded-xl px-3.5 py-2 text-sm font-mono outline-none"
                        value={doctorForm.medicareProviderNumber} onChange={(e) => setDoctorForm({...doctorForm, medicareProviderNumber: e.target.value})}
                    />
                    <button type="submit" className="bg-blue-600 text-white px-4 py-2 rounded-xl text-sm font-semibold hover:bg-blue-700 transition">
                        Register
                    </button>
                </form>
            </div>

            <div className="bg-white rounded-2xl border border-slate-200/70 shadow-xs overflow-hidden">
                <table className="w-full text-left text-sm">
                    <thead className="bg-slate-50/75 border-b border-slate-100 text-slate-500 text-xs uppercase font-semibold">
                    <tr>
                        <th className="px-6 py-3.5">Doctor Name</th>
                        <th className="px-6 py-3.5">Specialization</th>
                        <th className="px-6 py-3.5">Medicare Provider No</th>
                        <th className="px-6 py-3.5">Consultation Rate</th>
                    </tr>
                    </thead>
                    <tbody className="divide-y divide-slate-100">
                    {doctors.map((doc, idx) => (
                        <tr key={idx} className="hover:bg-slate-50/70 transition">
                            <td className="px-6 py-4 font-semibold text-slate-900">Dr. {doc.firstName} {doc.lastName}</td>
                            <td className="px-6 py-4 text-slate-600">{doc.specialization}</td>
                            <td className="px-6 py-4 font-mono text-xs font-semibold text-blue-600">{doc.medicareProviderNumber}</td>
                            <td className="px-6 py-4 font-bold text-slate-900">${doc.consultationFee} AUD</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
}