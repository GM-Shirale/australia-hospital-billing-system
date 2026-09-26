import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { UserPlus, Mail, Lock, User, Hospital } from 'lucide-react';

export default function Register() {
    const navigate = useNavigate();
    const [form, setForm] = useState({
        name: '',
        email: '',
        password: '',
        role: 'BILLING',
        department: 'Cardiology'
    });

    const handleRegister = (e) => {
        e.preventDefault();
        alert(`Account registered successfully for ${form.name} with Role: ${form.role}`);
        navigate('/login');
    };

    return (
        <div className="min-h-screen bg-slate-50 flex items-center justify-center p-4">
            <div className="max-w-md w-full bg-white rounded-3xl p-8 border border-slate-200/80 shadow-lg space-y-6">
                <div className="text-center space-y-2">
                    <div className="inline-flex p-3 bg-emerald-50 text-emerald-600 rounded-2xl mb-1">
                        <UserPlus className="w-8 h-8" />
                    </div>
                    <h2 className="text-2xl font-black text-slate-900">Register Staff Member</h2>
                    <p className="text-xs text-slate-500">Hospital Internal User Onboarding</p>
                </div>

                <form onSubmit={handleRegister} className="space-y-3.5">
                    <div>
                        <label className="block text-xs font-bold text-slate-700 uppercase mb-1">Full Name</label>
                        <input
                            type="text"
                            required
                            placeholder="e.g. John Miller"
                            className="w-full border border-slate-200 rounded-xl px-3.5 py-2.5 text-sm focus:ring-2 focus:ring-emerald-500 outline-none"
                            value={form.name}
                            onChange={(e) => setForm({ ...form, name: e.target.value })}
                        />
                    </div>

                    <div>
                        <label className="block text-xs font-bold text-slate-700 uppercase mb-1">Official Email</label>
                        <input
                            type="email"
                            required
                            placeholder="staff@hospital.com"
                            className="w-full border border-slate-200 rounded-xl px-3.5 py-2.5 text-sm focus:ring-2 focus:ring-emerald-500 outline-none"
                            value={form.email}
                            onChange={(e) => setForm({ ...form, email: e.target.value })}
                        />
                    </div>

                    <div>
                        <label className="block text-xs font-bold text-slate-700 uppercase mb-1">Assign Role</label>
                        <select
                            className="w-full border border-slate-200 rounded-xl px-3.5 py-2.5 text-sm bg-white focus:ring-2 focus:ring-emerald-500 outline-none"
                            value={form.role}
                            onChange={(e) => setForm({ ...form, role: e.target.value })}
                        >
                            <option value="ADMIN">Hospital Administrator</option>
                            <option value="DOCTOR">Doctor / Practitioner</option>
                            <option value="BILLING">Billing Clerk</option>
                        </select>
                    </div>

                    <div>
                        <label className="block text-xs font-bold text-slate-700 uppercase mb-1">Password</label>
                        <input
                            type="password"
                            required
                            className="w-full border border-slate-200 rounded-xl px-3.5 py-2.5 text-sm focus:ring-2 focus:ring-emerald-500 outline-none"
                            value={form.password}
                            onChange={(e) => setForm({ ...form, password: e.target.value })}
                        />
                    </div>

                    <button
                        type="submit"
                        className="w-full bg-emerald-600 hover:bg-emerald-700 text-white font-bold py-2.5 rounded-xl text-sm transition mt-2 shadow-sm"
                    >
                        Register Staff Member
                    </button>
                </form>

                <div className="text-center pt-2 border-t border-slate-100">
                    <p className="text-xs text-slate-500">
                        Already have an account?{' '}
                        <Link to="/login" className="text-emerald-600 font-bold hover:underline">
                            Back to Sign In
                        </Link>
                    </p>
                </div>
            </div>
        </div>
    );
}