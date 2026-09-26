import React, { useState } from 'react';
import { FileText, Download, CheckCircle2, Printer } from 'lucide-react';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';

export default function InvoicesView() {
    const [invoice, setInvoice] = useState(null);
    const [form, setForm] = useState({
        patientName: 'Liam Johnson',
        medicareNo: '2345 67890 1',
        providerNo: '9876543B',
        doctorName: 'Dr. Steve Smith',
        daysAdmitted: '3',
        roomRate: '450.00',
        consultationFee: '180.00'
    });

    const generateInvoice = (e) => {
        e.preventDefault();
        const days = parseInt(form.daysAdmitted, 10) || 1;
        const roomTotal = days * parseFloat(form.roomRate);
        const consultTotal = parseFloat(form.consultationFee);
        const grossTotal = roomTotal + consultTotal;
        const medicareRebate = grossTotal * 0.75; // 75% Medicare Coverage
        const gapFee = grossTotal - medicareRebate;

        setInvoice({
            invoiceNo: `INV-${Date.now().toString().slice(-6)}`,
            date: new Date().toLocaleDateString('en-AU'),
            ...form,
            days,
            roomTotal: roomTotal.toFixed(2),
            consultTotal: consultTotal.toFixed(2),
            grossTotal: grossTotal.toFixed(2),
            medicareRebate: medicareRebate.toFixed(2),
            gapFee: gapFee.toFixed(2)
        });
    };

    const downloadPDF = () => {
        if (!invoice) return;

        const doc = new jsPDF();

        // Header Branding
        doc.setFontSize(16);
        doc.setTextColor(30, 41, 59);
        doc.text('AUSTRALIA HEALTH SYSTEM & CLINICAL SERVICES', 14, 20);

        doc.setFontSize(10);
        doc.setTextColor(100);
        doc.text('Hospital Tax Invoice & MBS Settlement Statement', 14, 26);
        doc.text(`Invoice No: ${invoice.invoiceNo}`, 14, 32);
        doc.text(`Issue Date: ${invoice.date}`, 14, 37);

        // Patient & Provider Info Box
        autoTable(doc, {
            startY: 44,
            head: [['Patient Details', 'Admitting Provider / Doctor']],
            body: [
                [
                    `Name: ${invoice.patientName}\nMedicare No: ${invoice.medicareNo}`,
                    `Doctor: ${invoice.doctorName}\nProvider No: ${invoice.providerNo}`
                ]
            ],
            theme: 'plain',
            styles: { fontSize: 9, cellPadding: 3 },
            headStyles: { fillColor: [241, 245, 249], textColor: [15, 23, 42], fontStyle: 'bold' }
        });

        // Itemized Bill Table
        autoTable(doc, {
            startY: doc.lastAutoTable.finalY + 8,
            head: [['Item Description', 'Qty / Days', 'Unit Rate (AUD)', 'Total (AUD)']],
            body: [
                ['Inpatient Ward / Bed Charges', `${invoice.days} days`, `$${invoice.roomRate}`, `$${invoice.roomTotal}`],
                ['Specialist Clinical Consultation', '1 service', `$${invoice.consultationFee}`, `$${invoice.consultTotal}`]
            ],
            theme: 'striped',
            styles: { fontSize: 9, cellPadding: 3 },
            headStyles: { fillColor: [37, 99, 235], textColor: [255, 255, 255] }
        });

        // Totals & Medicare Breakdown Table
        autoTable(doc, {
            startY: doc.lastAutoTable.finalY + 6,
            body: [
                ['Gross Billed Amount:', `$${invoice.grossTotal} AUD`],
                ['Medicare Benefit Schedule (MBS) 75% Rebate:', `-$${invoice.medicareRebate} AUD`],
                ['Total Out-of-Pocket Gap Fee Payable:', `$${invoice.gapFee} AUD`]
            ],
            theme: 'plain',
            styles: { fontSize: 10, halign: 'right', fontStyle: 'bold' },
            columnStyles: {
                0: { halign: 'right', textColor: [71, 85, 105] },
                1: { halign: 'right', textColor: [15, 23, 42] }
            }
        });

        // Footer
        doc.setFontSize(8);
        doc.setTextColor(148, 163, 184);
        doc.text('This is an official computer-generated Tax Invoice compliant with Medicare Australia.', 14, doc.lastAutoTable.finalY + 15);

        // Save & Download PDF
        doc.save(`${invoice.invoiceNo}-${invoice.patientName.replace(/\s+/g, '_')}.pdf`);
    };

    // Direct Browser Print Function
    const handleBrowserPrint = () => {
        window.print();
    };

    return (
        <div className="space-y-6">
            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 print:hidden">
                {/* Invoice Generator Form */}
                <div className="bg-white border border-slate-200/80 rounded-2xl p-6 shadow-xs space-y-4">
                    <h3 className="text-base font-bold text-slate-900 flex items-center gap-2">
                        <FileText className="w-5 h-5 text-blue-600" /> Generate Discharge Tax Invoice
                    </h3>
                    <p className="text-xs text-slate-400">Calculate room charges, consultation and apply Medicare rebates.</p>

                    <form onSubmit={generateInvoice} className="space-y-3.5 pt-2">
                        <div>
                            <label className="block text-xs font-bold text-slate-700 uppercase mb-1">Patient Full Name</label>
                            <input
                                type="text"
                                required
                                className="w-full border border-slate-200 rounded-xl p-2.5 text-sm outline-none"
                                value={form.patientName}
                                onChange={(e) => setForm({ ...form, patientName: e.target.value })}
                            />
                        </div>

                        <div className="grid grid-cols-2 gap-3">
                            <div>
                                <label className="block text-xs font-bold text-slate-700 uppercase mb-1">Medicare Number</label>
                                <input
                                    type="text"
                                    required
                                    className="w-full border border-slate-200 rounded-xl p-2.5 text-sm outline-none font-mono"
                                    value={form.medicareNo}
                                    onChange={(e) => setForm({ ...form, medicareNo: e.target.value })}
                                />
                            </div>
                            <div>
                                <label className="block text-xs font-bold text-slate-700 uppercase mb-1">Provider Number</label>
                                <input
                                    type="text"
                                    required
                                    className="w-full border border-slate-200 rounded-xl p-2.5 text-sm outline-none font-mono"
                                    value={form.providerNo}
                                    onChange={(e) => setForm({ ...form, providerNo: e.target.value })}
                                />
                            </div>
                        </div>

                        <div className="grid grid-cols-3 gap-3">
                            <div>
                                <label className="block text-xs font-bold text-slate-700 uppercase mb-1">Days Admitted</label>
                                <input
                                    type="number"
                                    min="1"
                                    required
                                    className="w-full border border-slate-200 rounded-xl p-2.5 text-sm outline-none font-semibold"
                                    value={form.daysAdmitted}
                                    onChange={(e) => setForm({ ...form, daysAdmitted: e.target.value })}
                                />
                            </div>
                            <div>
                                <label className="block text-xs font-bold text-slate-700 uppercase mb-1">Daily Bed Rate (AUD)</label>
                                <input
                                    type="number"
                                    step="0.01"
                                    required
                                    className="w-full border border-slate-200 rounded-xl p-2.5 text-sm outline-none font-semibold"
                                    value={form.roomRate}
                                    onChange={(e) => setForm({ ...form, roomRate: e.target.value })}
                                />
                            </div>
                            <div>
                                <label className="block text-xs font-bold text-slate-700 uppercase mb-1">Consult Fee (AUD)</label>
                                <input
                                    type="number"
                                    step="0.01"
                                    required
                                    className="w-full border border-slate-200 rounded-xl p-2.5 text-sm outline-none font-semibold"
                                    value={form.consultationFee}
                                    onChange={(e) => setForm({ ...form, consultationFee: e.target.value })}
                                />
                            </div>
                        </div>

                        <button
                            type="submit"
                            className="w-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-2.5 rounded-xl text-sm transition shadow-xs cursor-pointer"
                        >
                            Calculate & Review Invoice
                        </button>
                    </form>
                </div>

                {/* Invoice Preview & Actions */}
                {invoice ? (
                    <div className="bg-white border border-slate-200/80 rounded-2xl p-6 shadow-xs flex flex-col justify-between space-y-4">
                        <div className="space-y-4">
                            <div className="flex justify-between items-start border-b border-slate-100 pb-3">
                                <div>
                                    <span className="text-[10px] font-mono text-slate-400 font-bold uppercase">{invoice.invoiceNo}</span>
                                    <h4 className="font-black text-slate-900 text-base">{invoice.patientName}</h4>
                                </div>
                                <span className="text-xs bg-emerald-50 text-emerald-700 font-bold px-2.5 py-1 rounded-full border border-emerald-200 flex items-center gap-1">
                                    <CheckCircle2 className="w-3 h-3" /> MBS Adjudicated
                                </span>
                            </div>

                            <div className="space-y-2 text-xs">
                                <div className="flex justify-between text-slate-600">
                                    <span>Room Charges ({invoice.days} days @ ${invoice.roomRate}):</span>
                                    <span className="font-semibold text-slate-900">${invoice.roomTotal} AUD</span>
                                </div>
                                <div className="flex justify-between text-slate-600">
                                    <span>Specialist Consultation Fee:</span>
                                    <span className="font-semibold text-slate-900">${invoice.consultTotal} AUD</span>
                                </div>
                                <div className="flex justify-between text-slate-600 pt-2 border-t border-slate-100">
                                    <span>Gross Amount:</span>
                                    <span className="font-bold text-slate-900">${invoice.grossTotal} AUD</span>
                                </div>
                                <div className="flex justify-between text-emerald-700 font-bold">
                                    <span>Medicare 75% Benefit Rebate:</span>
                                    <span>-${invoice.medicareRebate} AUD</span>
                                </div>
                                <div className="flex justify-between text-sm text-slate-900 font-black pt-2 border-t border-slate-200">
                                    <span>Final Out-of-Pocket Gap Fee:</span>
                                    <span>${invoice.gapFee} AUD</span>
                                </div>
                            </div>
                        </div>

                        <div className="flex flex-col sm:flex-row gap-3 pt-2">
                            <button
                                onClick={downloadPDF}
                                className="flex-1 flex items-center justify-center gap-2 bg-emerald-600 hover:bg-emerald-700 text-white font-bold py-2.5 rounded-xl text-xs transition shadow-xs cursor-pointer"
                            >
                                <Download className="w-4 h-4" /> Download PDF
                            </button>
                            <button
                                onClick={handleBrowserPrint}
                                className="flex items-center justify-center gap-2 bg-slate-800 hover:bg-slate-900 text-white font-bold px-4 py-2.5 rounded-xl text-xs transition shadow-xs cursor-pointer"
                            >
                                <Printer className="w-4 h-4" /> Print
                            </button>
                        </div>
                    </div>
                ) : (
                    <div className="bg-slate-50 border border-dashed border-slate-200 rounded-2xl flex flex-col items-center justify-center p-8 text-center text-slate-400">
                        <FileText className="w-10 h-10 mb-2 stroke-1" />
                        <p className="text-xs">No invoice calculated yet. Fill form and calculate to preview and download PDF.</p>
                    </div>
                )}
            </div>
        </div>
    );
}