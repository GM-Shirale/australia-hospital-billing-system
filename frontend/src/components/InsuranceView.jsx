import React, { useState } from 'react';
import { ShieldCheck, FileCheck2, AlertCircle } from 'lucide-react';

export default function InsuranceView() {
    const [claimStatus, setClaimStatus] = useState(null);
    const [form, setForm] = useState({
        policyNumber: 'POL-MBS-9921',
        billAmount: '450.00',
        insuranceType: 'MEDICARE'
    });

    const handleClaimSubmit = (e) => {
        e.preventDefault();
        const claimed = parseFloat(form.billAmount);
        const approved = claimed * 0.85; // 85% coverage demo
        const coPay = claimed - approved;

        setClaimStatus({
            status: 'APPROVED',
            approvedAmount: approved.toFixed(2),
            coPaymentAmount: coPay.toFixed(2),
            policyNumber: form.policyNumber
        });
    };

    return (
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
            {/* Submit Claim Form */}
            <div className="bg-white border border-slate-200/80 rounded-2xl p-6 shadow-xs space-y-4">
                <h3 className="text-base font-bold text-slate-900 flex items-center gap-2">
                    <ShieldCheck className="w-5 h-5 text-blue-600" /> Insurance Claim Adjudication
                </h3>
                <p className="text-xs text-slate-400">Direct integration with Medicare & Private Insurers.</p>

                <form onSubmit={handleClaimSubmit} className="space-y-3.5 pt-2">
                    <div>
                        <label className="block text-xs font-bold text-slate-700 uppercase mb-1">Insurance Policy Number</label>
                        <input
                            type="text"
                            required
                            className="w-full border border-slate-200 rounded-xl p-2.5 text-sm font-mono outline-none"
                            value={form.policyNumber}
                            onChange={(e) => setForm({ ...form, policyNumber: e.target.value })}
                        />
                    </div>

                    <div>
                        <label className="block text-xs font-bold text-slate-700 uppercase mb-1">Insurance Type</label>
                        <select
                            className="w-full border border-slate-200 rounded-xl p-2.5 text-sm bg-white outline-none"
                            value={form.insuranceType}
                            onChange={(e) => setForm({ ...form, insuranceType: e.target.value })}
                        >
                            <option value="MEDICARE">Medicare (Australian Govt)</option>
                            <option value="PRIVATE">Private Health Insurance (Bupa/Medibank)</option>
                        </select>
                    </div>

                    <div>
                        <label className="block text-xs font-bold text-slate-700 uppercase mb-1">Claimed Bill Amount (AUD)</label>
                        <input
                            type="number"
                            step="0.01"
                            required
                            className="w-full border border-slate-200 rounded-xl p-2.5 text-sm outline-none font-bold text-slate-800"
                            value={form.billAmount}
                            onChange={(e) => setForm({ ...form, billAmount: e.target.value })}
                        />
                    </div>

                    <button
                        type="submit"
                        className="w-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-2.5 rounded-xl text-sm transition shadow-xs"
                    >
                        Submit & Adjudicate Claim
                    </button>
                </form>
            </div>

            {/* Claim Result Card */}
            {claimStatus ? (
                <div className="bg-white border border-slate-200/80 rounded-2xl p-6 shadow-xs flex flex-col justify-between space-y-4">
                    <div className="space-y-4">
                        <div className="flex justify-between items-start border-b border-slate-100 pb-3">
                            <div>
                                <span className="text-[10px] font-mono text-slate-400 font-bold uppercase">{claimStatus.policyNumber}</span>
                                <h4 className="font-black text-slate-900 text-base">Claim Adjudication Summary</h4>
                            </div>
                            <span className="text-xs bg-emerald-50 text-emerald-700 font-bold px-2.5 py-1 rounded-full border border-emerald-200">
                {claimStatus.status}
              </span>
                        </div>

                        <div className="space-y-2 text-xs">
                            <div className="flex justify-between text-slate-600">
                                <span>Insurer Approved Coverage:</span>
                                <span className="font-semibold text-emerald-700">${claimStatus.approvedAmount} AUD</span>
                            </div>
                            <div className="flex justify-between text-slate-600">
                                <span>Patient Co-Payment (Out of Pocket):</span>
                                <span className="font-semibold text-slate-900">${claimStatus.coPaymentAmount} AUD</span>
                            </div>
                        </div>
                    </div>
                </div>
            ) : (
                <div className="bg-slate-50 border border-dashed border-slate-200 rounded-2xl flex flex-col items-center justify-center p-8 text-center text-slate-400">
                    <FileCheck2 className="w-10 h-10 mb-2 stroke-1" />
                    <p className="text-xs">No active claims adjudicated yet. Fill form and submit to view breakdown.</p>
                </div>
            )}
        </div>
    );
}