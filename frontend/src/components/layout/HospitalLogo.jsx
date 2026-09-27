import React from 'react';
import { Activity } from 'lucide-react';

export default function HospitalLogo() {
    return (
        <div className="flex items-center gap-3.5 group cursor-pointer">
            {/* 3D Gradient Icon Container with Glow Effect */}
            <div className="relative">
                <div className="w-11 h-11 rounded-2xl bg-gradient-to-tr from-blue-700 via-indigo-600 to-cyan-400 flex items-center justify-center text-white shadow-lg shadow-blue-500/25 ring-2 ring-white/80 group-hover:scale-105 transition-all duration-300">
                    <Activity className="w-6 h-6 animate-pulse text-cyan-200" />
                </div>
                {/* Active Ping Dot */}
                <span className="absolute -top-0.5 -right-0.5 flex h-3 w-3">
          <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-cyan-400 opacity-75"></span>
          <span className="relative inline-flex rounded-full h-3 w-3 bg-cyan-500 border-2 border-white"></span>
        </span>
            </div>

            {/* Brand Name & Typography */}
            <div className="flex flex-col">
                <div className="flex items-center gap-1.5">
          <span className="text-lg font-black tracking-tight bg-gradient-to-r from-slate-950 via-slate-800 to-blue-700 bg-clip-text text-transparent">
            AUSTRA<span className="text-blue-600">CARE</span>
          </span>
                    <span className="text-[9px] font-black uppercase tracking-widest bg-blue-50 text-blue-700 border border-blue-200/80 px-1.5 py-0.5 rounded-md">
            MBS PRO
          </span>
                </div>
                <p className="text-[11px] font-semibold text-slate-400 tracking-tight flex items-center gap-1">
                    Hospital Billing & Clinical Engine
                </p>
            </div>
        </div>
    );
}