import React from 'react';
import { useNavigate } from 'react-router-dom';
import HospitalLogo from './HospitalLogo';
import {
    LayoutDashboard,
    Building2,
    Stethoscope,
    Bed,
    Receipt,
    LogOut,
    Users,
    FileText,
    FlaskConical,
    ShieldCheck,
    Pill,
    RefreshCw,
    History
} from 'lucide-react';

export default function Layout({ activeTab, setActiveTab, children }) {
    const navigate = useNavigate();

    // Read the current user profile from browser storage
    const user = JSON.parse(localStorage.getItem('currentUser')) || { role: 'ADMIN', email: 'admin@hospital.com' };

    // Clear session and redirect to login page
    const handleLogout = () => {
        localStorage.removeItem('currentUser');
        navigate('/login');
    };

    const role = user.role;

    return (
        <div className="h-screen w-screen bg-slate-50 text-slate-800 flex flex-col font-sans overflow-hidden">
            {/* Top header navigation */}
            <header className="h-16 bg-white border-b border-slate-200/80 px-8 flex justify-between items-center shadow-xs shrink-0 z-30">
                <div className="flex items-center gap-4">
                    <HospitalLogo />
                </div>

                <div className="flex items-center gap-4">
                    <div className="flex items-center gap-2.5 bg-slate-50 border border-slate-200/70 px-4 py-1.5 rounded-full text-xs font-semibold text-slate-700 shadow-2xs">
                        <span className="w-2 h-2 rounded-full bg-emerald-500 animate-pulse"></span>
                        <span className="text-slate-400 font-normal">Facility:</span> Sydney Central Hospital
                    </div>

                    <button
                        onClick={() => window.location.reload()}
                        className="p-2 bg-slate-50 hover:bg-slate-100 text-slate-600 hover:text-slate-900 rounded-xl border border-slate-200 transition shadow-2xs"
                        title="Refresh System Data"
                    >
                        <RefreshCw className="w-4 h-4" />
                    </button>

                    <div className="text-right pl-2 border-l border-slate-200">
                        <p className="text-xs font-bold text-slate-900 leading-tight">{user.email}</p>
                        <span className="inline-block text-[10px] font-bold px-2 py-0.5 rounded bg-blue-50 text-blue-700 border border-blue-200">
                            ROLE: {role}
                        </span>
                    </div>

                    <button
                        onClick={handleLogout}
                        className="flex items-center gap-1.5 text-xs text-rose-600 hover:bg-rose-50 px-3 py-1.5 rounded-xl font-bold transition border border-rose-200"
                    >
                        <LogOut className="w-3.5 h-3.5" /> Logout
                    </button>
                </div>
            </header>

            {/* Split workspace body */}
            <div className="flex flex-1 overflow-hidden h-[calc(100vh-4rem)]">
                {/* Left navigation sidebar */}
                <aside className="w-64 bg-white border-r border-slate-200 p-4 space-y-1.5 overflow-y-auto shrink-0 select-none">
                    {/* Executive Dashboard Overview */}
                    <button
                        onClick={() => setActiveTab('dashboard')}
                        className={`w-full flex items-center space-x-3 px-4 py-3 rounded-xl text-sm font-semibold transition ${
                            activeTab === 'dashboard' ? 'bg-blue-600 text-white shadow-xs' : 'text-slate-600 hover:bg-slate-100'
                        }`}
                    >
                        <LayoutDashboard className="w-4 h-4" />
                        <span>Overview Dashboard</span>
                    </button>

                    {/* Doctor registration and provider management */}
                    {(role === 'ADMIN' || role === 'DOCTOR') && (
                        <button
                            onClick={() => setActiveTab('doctors')}
                            className={`w-full flex items-center space-x-3 px-4 py-3 rounded-xl text-sm font-semibold transition ${
                                activeTab === 'doctors' ? 'bg-blue-600 text-white shadow-xs' : 'text-slate-600 hover:bg-slate-100'
                            }`}
                        >
                            <Stethoscope className="w-4 h-4" />
                            <span>Doctors & Providers</span>
                        </button>
                    )}

                    {/* Hospital departments setup */}
                    {role === 'ADMIN' && (
                        <button
                            onClick={() => setActiveTab('departments')}
                            className={`w-full flex items-center space-x-3 px-4 py-3 rounded-xl text-sm font-semibold transition ${
                                activeTab === 'departments' ? 'bg-blue-600 text-white shadow-xs' : 'text-slate-600 hover:bg-slate-100'
                            }`}
                        >
                            <Building2 className="w-4 h-4" />
                            <span>Departments</span>
                        </button>
                    )}

                    {/* Patient admission and registration */}
                    <button
                        onClick={() => setActiveTab('patients')}
                        className={`w-full flex items-center space-x-3 px-4 py-3 rounded-xl text-sm font-semibold transition ${
                            activeTab === 'patients' ? 'bg-blue-600 text-white shadow-xs' : 'text-slate-600 hover:bg-slate-100'
                        }`}
                    >
                        <Users className="w-4 h-4" />
                        <span>Patients & Admission</span>
                    </button>

                    {/* Patient medical background and past history */}
                    <button
                        onClick={() => setActiveTab('medical-history')}
                        className={`w-full flex items-center space-x-3 px-4 py-3 rounded-xl text-sm font-semibold transition ${
                            activeTab === 'medical-history' ? 'bg-blue-600 text-white shadow-xs' : 'text-slate-600 hover:bg-slate-100'
                        }`}
                    >
                        <History className="w-4 h-4" />
                        <span>Patient Medical History</span>
                    </button>

                    {/* Room allocation and bed inventory */}
                    {(role === 'ADMIN' || role === 'BILLING') && (
                        <button
                            onClick={() => setActiveTab('rooms')}
                            className={`w-full flex items-center space-x-3 px-4 py-3 rounded-xl text-sm font-semibold transition ${
                                activeTab === 'rooms' ? 'bg-blue-600 text-white shadow-xs' : 'text-slate-600 hover:bg-slate-100'
                            }`}
                        >
                            <Bed className="w-4 h-4" />
                            <span>Rooms & Beds</span>
                        </button>
                    )}

                    {/* Pharmacy inventory and medication stock */}
                    <button
                        onClick={() => setActiveTab('pharmacy')}
                        className={`w-full flex items-center space-x-3 px-4 py-3 rounded-xl text-sm font-semibold transition ${
                            activeTab === 'pharmacy' ? 'bg-blue-600 text-white shadow-xs' : 'text-slate-600 hover:bg-slate-100'
                        }`}
                    >
                        <Pill className="w-4 h-4" />
                        <span>Pharmacy & Stock</span>
                    </button>

                    {/* Lab investigations and pathology reports */}
                    <button
                        onClick={() => setActiveTab('laboratory')}
                        className={`w-full flex items-center space-x-3 px-4 py-3 rounded-xl text-sm font-semibold transition ${
                            activeTab === 'laboratory' ? 'bg-blue-600 text-white shadow-xs' : 'text-slate-600 hover:bg-slate-100'
                        }`}
                    >
                        <FlaskConical className="w-4 h-4" />
                        <span>Laboratory & Pathology</span>
                    </button>

                    {/* Core hospital billing module */}
                    {(role === 'ADMIN' || role === 'BILLING') && (
                        <button
                            onClick={() => setActiveTab('billing')}
                            className={`w-full flex items-center space-x-3 px-4 py-3 rounded-xl text-sm font-semibold transition ${
                                activeTab === 'billing' ? 'bg-blue-600 text-white shadow-xs' : 'text-slate-600 hover:bg-slate-100'
                            }`}
                        >
                            <Receipt className="w-4 h-4" />
                            <span>Central Billing Engine</span>
                        </button>
                    )}

                    {/* Health insurance policies and claims */}
                    <button
                        onClick={() => setActiveTab('insurance')}
                        className={`w-full flex items-center space-x-3 px-4 py-3 rounded-xl text-sm font-semibold transition ${
                            activeTab === 'insurance' ? 'bg-blue-600 text-white shadow-xs' : 'text-slate-600 hover:bg-slate-100'
                        }`}
                    >
                        <ShieldCheck className="w-4 h-4" />
                        <span>Insurance & Claims</span>
                    </button>

                    {/* Discharge summaries and billing invoices */}
                    {(role === 'ADMIN' || role === 'BILLING') && (
                        <button
                            onClick={() => setActiveTab('invoices')}
                            className={`w-full flex items-center space-x-3 px-4 py-3 rounded-xl text-sm font-semibold transition ${
                                activeTab === 'invoices' ? 'bg-blue-600 text-white shadow-xs' : 'text-slate-600 hover:bg-slate-100'
                            }`}
                        >
                            <FileText className="w-4 h-4" />
                            <span>Invoices & Discharge</span>
                        </button>
                    )}
                </aside>

                {/* Main active view that scrolls independently */}
                <main className="flex-1 p-8 overflow-y-auto h-full">
                    {children}
                </main>
            </div>
        </div>
    );
}