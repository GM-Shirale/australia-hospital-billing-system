import React, { useState } from 'react';
import { FlaskConical, Plus, CheckCircle, Clock } from 'lucide-react';

const INITIAL_TESTS = [
    { id: '1', testCode: 'MBS-65070', testName: 'Full Blood Count (FBC)', category: 'HAEMATOLOGY', cost: 42.50, status: 'COMPLETED' },
    { id: '2', testCode: 'MBS-66500', testName: 'Liver Function Test (LFT)', category: 'BIOCHEMISTRY', cost: 58.00, status: 'PROCESSING' }
];

export default function LaboratoryView() {
    const [labTests, setLabTests] = useState(INITIAL_TESTS);
    const [form, setForm] = useState({
        testCode: '',
        testName: '',
        category: 'HAEMATOLOGY',
        cost: ''
    });

    const handleCreateTest = (e) => {
        e.preventDefault();
        const newTest = {
            id: Date.now().toString(),
            testCode: form.testCode,
            testName: form.testName,
            category: form.category,
            cost: parseFloat(form.cost),
            status: 'AVAILABLE'
        };
        setLabTests([...labTests, newTest]);
        setForm({ testCode: '', testName: '', category: 'HAEMATOLOGY', cost: '' });
    };

    return (
        <div className="space-y-6">
            {/* Test Creation Form */}
            <div className="bg-white p-6 rounded-2xl border border-slate-200/70 shadow-xs">
                <h3 className="text-base font-bold text-slate-900 mb-3 flex items-center gap-2">
                    <Plus className="w-4 h-4 text-blue-600" /> Add Pathology / Lab Diagnostic Test
                </h3>
                <form onSubmit={handleCreateTest} className="grid grid-cols-1 md:grid-cols-5 gap-3.5">
                    <input
                        type="text"
                        required
                        placeholder="MBS Item Code (e.g. MBS-65070)"
                        className="border border-slate-200 rounded-xl px-3.5 py-2 text-sm outline-none font-mono"
                        value={form.testCode}
                        onChange={(e) => setForm({ ...form, testCode: e.target.value })}
                    />
                    <input
                        type="text"
                        required
                        placeholder="Diagnostic Test Name"
                        className="border border-slate-200 rounded-xl px-3.5 py-2 text-sm outline-none"
                        value={form.testName}
                        onChange={(e) => setForm({ ...form, testName: e.target.value })}
                    />
                    <select
                        className="border border-slate-200 rounded-xl px-3 py-2 text-sm outline-none bg-white font-medium text-slate-700"
                        value={form.category}
                        onChange={(e) => setForm({ ...form, category: e.target.value })}
                    >
                        <option value="HAEMATOLOGY">Haematology</option>
                        <option value="BIOCHEMISTRY">Biochemistry</option>
                        <option value="MICROBIOLOGY">Microbiology</option>
                        <option value="HISTOPATHOLOGY">Histopathology</option>
                    </select>
                    <input
                        type="number"
                        step="0.01"
                        required
                        placeholder="MBS Tariff (AUD)"
                        className="border border-slate-200 rounded-xl px-3.5 py-2 text-sm outline-none"
                        value={form.cost}
                        onChange={(e) => setForm({ ...form, cost: e.target.value })}
                    />
                    <button
                        type="submit"
                        className="bg-blue-600 text-white px-5 py-2 rounded-xl text-sm font-semibold hover:bg-blue-700 transition shadow-xs"
                    >
                        Register Test
                    </button>
                </form>
            </div>

            {/* Table Directory */}
            <div className="bg-white rounded-2xl border border-slate-200/70 shadow-xs overflow-hidden">
                <div className="px-6 py-4 border-b border-slate-100 flex justify-between items-center">
                    <h4 className="font-bold text-slate-800 text-sm flex items-center gap-2">
                        <FlaskConical className="w-4 h-4 text-purple-600" /> MBS Pathology & Laboratory Registry
                    </h4>
                    <span className="text-xs text-slate-400">Total Catalog: {labTests.length} tests</span>
                </div>
                <table className="w-full text-left text-sm">
                    <thead className="bg-slate-50/75 border-b border-slate-100 text-slate-500 text-xs uppercase font-semibold">
                    <tr>
                        <th className="px-6 py-3.5">MBS Code</th>
                        <th className="px-6 py-3.5">Test Details</th>
                        <th className="px-6 py-3.5">Discipline / Category</th>
                        <th className="px-6 py-3.5">Rebate Tariff</th>
                        <th className="px-6 py-3.5">Operational Status</th>
                    </tr>
                    </thead>
                    <tbody className="divide-y divide-slate-100">
                    {labTests.map((t, idx) => (
                        <tr key={idx} className="hover:bg-slate-50/70 transition">
                            <td className="px-6 py-4 font-mono font-bold text-xs text-purple-600">{t.testCode}</td>
                            <td className="px-6 py-4 font-semibold text-slate-900">{t.testName}</td>
                            <td className="px-6 py-4 text-xs font-medium text-slate-600">{t.category}</td>
                            <td className="px-6 py-4 font-bold text-slate-900">${t.cost.toFixed(2)} AUD</td>
                            <td className="px-6 py-4">
                  <span className={`text-[11px] px-2.5 py-1 rounded-full font-bold inline-flex items-center gap-1 ${
                      t.status === 'COMPLETED' ? 'bg-emerald-50 text-emerald-700 border border-emerald-200' : 'bg-amber-50 text-amber-700 border border-amber-200'
                  }`}>
                    {t.status === 'COMPLETED' ? <CheckCircle className="w-3 h-3" /> : <Clock className="w-3 h-3" />}
                      {t.status}
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