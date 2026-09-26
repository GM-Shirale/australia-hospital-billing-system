import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { ShieldCheck, Lock, Mail, UserCheck } from 'lucide-react';

export default function Login() {
    const navigate = useNavigate();
    const [email, setEmail] = useState('admin@hospital.com');
    const [password, setPassword] = useState('password123');
    const [role, setRole] = useState('ADMIN');

    const handleLogin = (e) => {
        e.preventDefault();
        // User data session/localStorage madhe store kara
        const userSession = {
            email,
            role,
            tenantId: 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
            hospitalName: 'Sydney Central Hospital'
        };
        localStorage.setItem('currentUser', JSON.stringify(userSession));

        // Dashboard var redirect kara
        navigate('/');
    };

    return (
        <div className="min-h-screen bg-slate-50 flex items-center justify-center p-4">
            <div className="max-w-md w-full bg-white rounded-3xl p-8 border border-slate-200/80 shadow-lg space-y-6">
                <div className="text-center space-y-2">
                    <div className="inline-flex p-3 bg-blue-50 text-blue-600 rounded-2xl mb-1">
                        <ShieldCheck className="w-8 h-8" />
                    </div>
                    <h2 className="text-2xl font-black text-slate-900">Hospital Staff Portal</h2>
                    <p className="text-xs text-slate-500">Sign in to access MBS Clinical & Billing Services</p>
                </div>

                <form onSubmit={handleLogin} className="space-y-4">
                    <div>
                        <label className="block text-xs font-bold text-slate-700 uppercase mb-1.5">Staff Email</label>
                        <div className="relative">
                            <Mail className="w-4 h-4 text-slate-400 absolute left-3.5 top-3" />
                            <input
                                type="email"
                                required
                                className="w-full border border-slate-200 rounded-xl pl-10 pr-3.5 py-2.5 text-sm focus:ring-2 focus:ring-blue-500 outline-none"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                            />
                        </div>
                    </div>

                    <div>
                        <label className="block text-xs font-bold text-slate-700 uppercase mb-1.5">Password</label>
                        <div className="relative">
                            <Lock className="w-4 h-4 text-slate-400 absolute left-3.5 top-3" />
                            <input
                                type="password"
                                required
                                className="w-full border border-slate-200 rounded-xl pl-10 pr-3.5 py-2.5 text-sm focus:ring-2 focus:ring-blue-500 outline-none"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                            />
                        </div>
                    </div>

                    {/* <div>
                        <label className="block text-xs font-bold text-slate-700 uppercase mb-1.5">Select Role (Demo Testing)</label>
                        <div className="relative">
                            <UserCheck className="w-4 h-4 text-slate-400 absolute left-3.5 top-3" />
                            <select
                                className="w-full border border-slate-200 rounded-xl pl-10 pr-3.5 py-2.5 text-sm bg-white focus:ring-2 focus:ring-blue-500 outline-none font-medium"
                                value={role}
                                onChange={(e) => setRole(e.target.value)}
                            >
                                <option value="ADMIN">ADMIN (Full Control: All Modules)</option>
                                <option value="DOCTOR">DOCTOR (Practitioners & Notes)</option>
                                <option value="BILLING">BILLING CLERK (Rooms, Beds & Invoices)</option>
                            </select>
                        </div>
                    </div> */}

                    <button
                        type="submit"
                        className="w-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-2.5 rounded-xl text-sm transition shadow-sm"
                    >
                        Authorize & Sign In
                    </button>
                </form>

                <div className="text-center pt-2 border-t border-slate-100">
                    <p className="text-xs text-slate-500">
                        Need staff registration?{' '}
                        <Link to="/register" className="text-blue-600 font-bold hover:underline">
                            Create Staff Account
                        </Link>
                    </p>
                </div>
            </div>
        </div>
    );
}