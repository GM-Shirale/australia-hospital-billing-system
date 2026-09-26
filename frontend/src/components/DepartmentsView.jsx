import React, { useState, useEffect } from 'react';
import { Plus, RefreshCw, AlertCircle } from 'lucide-react';
import axiosClient from '../services/axiosClient';

export default function DepartmentsView() {
    const [departments, setDepartments] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [deptForm, setDeptForm] = useState({ departmentName: '', location: '' });

    const fetchDepartments = async () => {
        setLoading(true);
        setError('');
        try {
            const res = await axiosClient.get('/departments');
            setDepartments(Array.isArray(res.data) ? res.data : res.data.content || []);
        } catch (err) {
            setError(err.response?.data?.message || 'Failed to load departments.');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchDepartments();
    }, []);

    const handleCreateDepartment = async (e) => {
        e.preventDefault();
        try {
            await axiosClient.post('/departments', deptForm);
            setDeptForm({ departmentName: '', location: '' });
            fetchDepartments();
        } catch (err) {
            alert(err.response?.data?.message || 'Error saving department.');
        }
    };

    return (
        <div className="space-y-6">
            {error && (
                <div className="bg-red-50 text-red-700 p-4 rounded-xl text-xs flex items-center gap-2">
                    <AlertCircle className="w-4 h-4" /> {error}
                </div>
            )}

            <div className="bg-white p-6 rounded-2xl border border-slate-200/70 shadow-xs">
                <h3 className="text-base font-bold text-slate-900 mb-3 flex items-center gap-2">
                    <Plus className="w-4 h-4 text-blue-600" /> Create Clinical Department
                </h3>
                <form onSubmit={handleCreateDepartment} className="grid grid-cols-1 md:grid-cols-3 gap-3.5">
                    <input
                        type="text"
                        required
                        placeholder="Department Name (e.g. Cardiology)"
                        className="border border-slate-200 rounded-xl px-3.5 py-2 text-sm outline-none"
                        value={deptForm.departmentName}
                        onChange={(e) => setDeptForm({ ...deptForm, departmentName: e.target.value })}
                    />
                    <input
                        type="text"
                        required
                        placeholder="Location (e.g. Block B, Level 2)"
                        className="border border-slate-200 rounded-xl px-3.5 py-2 text-sm outline-none"
                        value={deptForm.location}
                        onChange={(e) => setDeptForm({ ...deptForm, location: e.target.value })}
                    />
                    <button
                        type="submit"
                        className="bg-blue-600 text-white px-5 py-2 rounded-xl text-sm font-semibold hover:bg-blue-700 transition"
                    >
                        Save Department
                    </button>
                </form>
            </div>

            <div className="bg-white rounded-2xl border border-slate-200/70 shadow-xs overflow-hidden">
                <div className="px-6 py-4 border-b border-slate-100 flex justify-between items-center">
                    <span className="font-bold text-sm">Clinical Units Directory</span>
                    <button onClick={fetchDepartments} className="text-slate-400 hover:text-slate-700">
                        <RefreshCw className={`w-4 h-4 ${loading ? 'animate-spin' : ''}`} />
                    </button>
                </div>
                <table className="w-full text-left text-sm">
                    <thead className="bg-slate-50/75 border-b border-slate-100 text-slate-500 text-xs uppercase font-semibold">
                    <tr>
                        <th className="px-6 py-3.5">Department Name</th>
                        <th className="px-6 py-3.5">Location / Block</th>
                        <th className="px-6 py-3.5">ID</th>
                    </tr>
                    </thead>
                    <tbody className="divide-y divide-slate-100">
                    {departments.map((dept) => (
                        <tr key={dept.id} className="hover:bg-slate-50/70 transition">
                            <td className="px-6 py-4 font-semibold text-slate-900">{dept.departmentName || dept.name}</td>
                            <td className="px-6 py-4 text-slate-600">{dept.location}</td>
                            <td className="px-6 py-4 font-mono text-xs text-slate-400">{dept.id}</td>
                        </tr>
                    ))}
                    {departments.length === 0 && !loading && (
                        <tr>
                            <td colSpan="3" className="px-6 py-6 text-center text-slate-400 text-xs">
                                No departments recorded in database.
                            </td>
                        </tr>
                    )}
                    </tbody>
                </table>
            </div>
        </div>
    );
}