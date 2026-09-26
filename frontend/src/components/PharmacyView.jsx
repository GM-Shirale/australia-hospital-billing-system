import React, { useState } from 'react';
import { Pill, Plus, AlertTriangle, CheckCircle, Search, ShieldCheck } from 'lucide-react';

export default function PharmacyView() {
    const [searchTerm, setSearchTerm] = useState('');
    const [medicines, setMedicines] = useState([
        {
            id: 'MED-101',
            name: 'Amoxicillin + Clavulanic Acid (Augmentin Duo)',
            category: 'Antibiotics',
            dosage: '500mg/125mg',
            stock: 45,
            unitPrice: 28.50,
            isPBS: true,
            expiryDate: '2027-04-30'
        },
        {
            id: 'MED-102',
            name: 'Paracetamol IV Infusion (Perfalgan)',
            category: 'Analgesics / Pain Relief',
            dosage: '1000mg / 100ml',
            stock: 8,
            unitPrice: 15.00,
            isPBS: false,
            expiryDate: '2026-12-15'
        },
        {
            id: 'MED-103',
            name: 'Normal Saline 0.9% IV Infusion',
            category: 'IV Fluids',
            dosage: '1000ml Bag',
            stock: 120,
            unitPrice: 12.00,
            isPBS: true,
            expiryDate: '2028-01-10'
        },
        {
            id: 'MED-104',
            name: 'Pantoprazole IV Injection',
            category: 'Gastrointestinal',
            dosage: '40mg Vial',
            stock: 5,
            unitPrice: 22.00,
            isPBS: true,
            expiryDate: '2026-11-20'
        },
        {
            id: 'MED-105',
            name: 'Atorvastatin Tablets',
            category: 'Cardiovascular',
            dosage: '20mg Daily',
            stock: 60,
            unitPrice: 18.20,
            isPBS: true,
            expiryDate: '2027-09-18'
        }
    ]);

    const [form, setForm] = useState({
        name: '',
        category: 'Antibiotics',
        dosage: '',
        stock: '',
        unitPrice: '',
        isPBS: false,
        expiryDate: ''
    });

    const handleAddMedicine = (e) => {
        e.preventDefault();
        const newMed = {
            id: `MED-${Date.now().toString().slice(-3)}`,
            name: form.name,
            category: form.category,
            dosage: form.dosage,
            stock: parseInt(form.stock, 10),
            unitPrice: parseFloat(form.unitPrice),
            isPBS: form.isPBS,
            expiryDate: form.expiryDate
        };

        setMedicines([...medicines, newMed]);
        setForm({
            name: '',
            category: 'Antibiotics',
            dosage: '',
            stock: '',
            unitPrice: '',
            isPBS: false,
            expiryDate: ''
        });
    };

    const filteredMeds = medicines.filter(m =>
        m.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
        m.category.toLowerCase().includes(searchTerm.toLowerCase())
    );

    return (
        <div className="space-y-6">
            {/* Header Stat Cards */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                <div className="bg-white border border-slate-200/80 rounded-2xl p-5 shadow-xs flex items-center justify-between">
                    <div>
                        <p className="text-xs font-bold text-slate-400 uppercase">Total Medicines</p>
                        <h3 className="text-2xl font-black text-slate-900 mt-1">{medicines.length} Items</h3>
                    </div>
                    <div className="w-10 h-10 bg-blue-50 text-blue-600 rounded-xl flex items-center justify-center">
                        <Pill className="w-5 h-5" />
                    </div>
                </div>

                <div className="bg-white border border-slate-200/80 rounded-2xl p-5 shadow-xs flex items-center justify-between">
                    <div>
                        <p className="text-xs font-bold text-amber-500 uppercase">Low Stock Alert (&le;10)</p>
                        <h3 className="text-2xl font-black text-amber-600 mt-1">
                            {medicines.filter(m => m.stock <= 10).length} Critical
                        </h3>
                    </div>
                    <div className="w-10 h-10 bg-amber-50 text-amber-600 rounded-xl flex items-center justify-center">
                        <AlertTriangle className="w-5 h-5" />
                    </div>
                </div>

                <div className="bg-white border border-slate-200/80 rounded-2xl p-5 shadow-xs flex items-center justify-between">
                    <div>
                        <p className="text-xs font-bold text-emerald-600 uppercase">PBS Subsidised</p>
                        <h3 className="text-2xl font-black text-emerald-700 mt-1">
                            {medicines.filter(m => m.isPBS).length} Approved
                        </h3>
                    </div>
                    <div className="w-10 h-10 bg-emerald-50 text-emerald-600 rounded-xl flex items-center justify-center">
                        <ShieldCheck className="w-5 h-5" />
                    </div>
                </div>
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
                {/* Form: Add New Medicine */}
                <div className="bg-white border border-slate-200/80 rounded-2xl p-6 shadow-xs space-y-4">
                    <h3 className="text-base font-bold text-slate-900 flex items-center gap-2">
                        <Plus className="w-5 h-5 text-blue-600" /> Add Pharmacy Drug
                    </h3>
                    <p className="text-xs text-slate-400">Register new hospital drugs with batch and PBS status.</p>

                    <form onSubmit={handleAddMedicine} className="space-y-3.5 pt-2">
                        <div>
                            <label className="block text-xs font-bold text-slate-700 uppercase mb-1">Drug / Medicine Name</label>
                            <input
                                type="text"
                                required
                                className="w-full border border-slate-200 rounded-xl p-2.5 text-sm outline-none"
                                placeholder="e.g. Ceftriaxone Injection"
                                value={form.name}
                                onChange={(e) => setForm({ ...form, name: e.target.value })}
                            />
                        </div>

                        <div>
                            <label className="block text-xs font-bold text-slate-700 uppercase mb-1">Category</label>
                            <select
                                className="w-full border border-slate-200 rounded-xl p-2.5 text-sm outline-none bg-white"
                                value={form.category}
                                onChange={(e) => setForm({ ...form, category: e.target.value })}
                            >
                                <option value="Antibiotics">Antibiotics</option>
                                <option value="Analgesics / Pain Relief">Analgesics / Pain Relief</option>
                                <option value="Cardiovascular">Cardiovascular</option>
                                <option value="IV Fluids">IV Fluids & Infusions</option>
                                <option value="Gastrointestinal">Gastrointestinal</option>
                                <option value="Respiratory">Respiratory</option>
                            </select>
                        </div>

                        <div className="grid grid-cols-2 gap-3">
                            <div>
                                <label className="block text-xs font-bold text-slate-700 uppercase mb-1">Dosage / Strength</label>
                                <input
                                    type="text"
                                    required
                                    className="w-full border border-slate-200 rounded-xl p-2.5 text-sm outline-none"
                                    placeholder="e.g. 1g IV Vial"
                                    value={form.dosage}
                                    onChange={(e) => setForm({ ...form, dosage: e.target.value })}
                                />
                            </div>

                            <div>
                                <label className="block text-xs font-bold text-slate-700 uppercase mb-1">Stock Quantity</label>
                                <input
                                    type="number"
                                    min="1"
                                    required
                                    className="w-full border border-slate-200 rounded-xl p-2.5 text-sm outline-none"
                                    placeholder="50"
                                    value={form.stock}
                                    onChange={(e) => setForm({ ...form, stock: e.target.value })}
                                />
                            </div>
                        </div>

                        <div className="grid grid-cols-2 gap-3">
                            <div>
                                <label className="block text-xs font-bold text-slate-700 uppercase mb-1">Unit Price (AUD)</label>
                                <input
                                    type="number"
                                    step="0.01"
                                    required
                                    className="w-full border border-slate-200 rounded-xl p-2.5 text-sm outline-none"
                                    placeholder="35.00"
                                    value={form.unitPrice}
                                    onChange={(e) => setForm({ ...form, unitPrice: e.target.value })}
                                />
                            </div>

                            <div>
                                <label className="block text-xs font-bold text-slate-700 uppercase mb-1">Expiry Date</label>
                                <input
                                    type="date"
                                    required
                                    className="w-full border border-slate-200 rounded-xl p-2.5 text-sm outline-none"
                                    value={form.expiryDate}
                                    onChange={(e) => setForm({ ...form, expiryDate: e.target.value })}
                                />
                            </div>
                        </div>

                        <div className="flex items-center gap-2 pt-1">
                            <input
                                type="checkbox"
                                id="isPBS"
                                checked={form.isPBS}
                                onChange={(e) => setForm({ ...form, isPBS: e.target.checked })}
                                className="w-4 h-4 rounded text-blue-600"
                            />
                            <label htmlFor="isPBS" className="text-xs font-semibold text-slate-700">
                                PBS Subsidised Drug (Australia Medicare)
                            </label>
                        </div>

                        <button
                            type="submit"
                            className="w-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-2.5 rounded-xl text-sm transition shadow-xs"
                        >
                            Save Medicine to Stock
                        </button>
                    </form>
                </div>

                {/* Medicines Stock Table */}
                <div className="lg:col-span-2 bg-white border border-slate-200/80 rounded-2xl p-6 shadow-xs space-y-4">
                    <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-3">
                        <div>
                            <h3 className="text-base font-bold text-slate-900">Hospital Pharmacy Inventory</h3>
                            <p className="text-xs text-slate-400">Current stock availability and pricing matrix</p>
                        </div>
                        <div className="relative w-full sm:w-64">
                            <Search className="w-4 h-4 text-slate-400 absolute left-3 top-2.5" />
                            <input
                                type="text"
                                placeholder="Search drug or category..."
                                className="w-full pl-9 pr-3 py-1.5 border border-slate-200 rounded-xl text-xs outline-none focus:ring-1 focus:ring-blue-500"
                                value={searchTerm}
                                onChange={(e) => setSearchTerm(e.target.value)}
                            />
                        </div>
                    </div>

                    <div className="overflow-x-auto">
                        <table className="w-full text-left border-collapse text-xs">
                            <thead>
                            <tr className="border-b border-slate-100 text-slate-400 font-bold uppercase tracking-wider">
                                <th className="py-2.5 px-3">Drug / Strength</th>
                                <th className="py-2.5 px-3">Category</th>
                                <th className="py-2.5 px-3">Stock</th>
                                <th className="py-2.5 px-3">Rate (AUD)</th>
                                <th className="py-2.5 px-3">PBS</th>
                                <th className="py-2.5 px-3">Expiry</th>
                            </tr>
                            </thead>
                            <tbody className="divide-y divide-slate-100 text-slate-700">
                            {filteredMeds.map((med) => (
                                <tr key={med.id} className="hover:bg-slate-50/70 transition">
                                    <td className="py-3 px-3">
                                        <div className="font-bold text-slate-900">{med.name}</div>
                                        <div className="text-[10px] text-slate-400 font-mono">{med.dosage} &bull; {med.id}</div>
                                    </td>
                                    <td className="py-3 px-3">
                      <span className="bg-slate-100 text-slate-600 px-2 py-0.5 rounded-md font-medium">
                        {med.category}
                      </span>
                                    </td>
                                    <td className="py-3 px-3 font-semibold">
                                        {med.stock <= 10 ? (
                                            <span className="text-amber-600 bg-amber-50 px-2 py-0.5 rounded-full font-bold flex items-center gap-1 w-fit">
                          <AlertTriangle className="w-3 h-3" /> {med.stock} low
                        </span>
                                        ) : (
                                            <span className="text-slate-800">{med.stock} units</span>
                                        )}
                                    </td>
                                    <td className="py-3 px-3 font-bold text-slate-900">${med.unitPrice.toFixed(2)}</td>
                                    <td className="py-3 px-3">
                                        {med.isPBS ? (
                                            <span className="text-emerald-700 bg-emerald-50 px-2 py-0.5 rounded-full font-bold">Yes</span>
                                        ) : (
                                            <span className="text-slate-400">No</span>
                                        )}
                                    </td>
                                    <td className="py-3 px-3 font-mono text-slate-500">{med.expiryDate}</td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    );
}