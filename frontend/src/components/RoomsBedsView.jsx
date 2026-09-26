import React, { useState } from 'react';
import { Plus } from 'lucide-react';

const INITIAL_ROOMS = [
    { id: '1', roomNo: 'ICU-101', bedNo: 'BED-A', roomType: 'ICU', dailyRate: 450.00, status: 'AVAILABLE' },
    { id: '2', roomNo: 'GEN-204', bedNo: 'BED-01', roomType: 'GENERAL', dailyRate: 180.00, status: 'AVAILABLE' }
];

export default function RoomsBedsView() {
    const [rooms, setRooms] = useState(INITIAL_ROOMS);
    const [roomForm, setRoomForm] = useState({ roomNo: '', bedNo: '', roomType: 'GENERAL', dailyRate: 250.00 });

    const handleCreateRoom = (e) => {
        e.preventDefault();
        setRooms([...rooms, { ...roomForm, id: Date.now().toString(), status: 'AVAILABLE' }]);
        setRoomForm({ roomNo: '', bedNo: '', roomType: 'GENERAL', dailyRate: 250.00 });
    };

    return (
        <div className="space-y-6">
            <div className="bg-white p-6 rounded-2xl border border-slate-200/70 shadow-xs">
                <h3 className="text-base font-bold text-slate-900 mb-3 flex items-center gap-2">
                    <Plus className="w-4 h-4 text-blue-600" /> Allocate Ward & Bed
                </h3>
                <form onSubmit={handleCreateRoom} className="grid grid-cols-1 md:grid-cols-5 gap-3.5">
                    <input
                        type="text" required placeholder="Room No" className="border border-slate-200 rounded-xl px-3.5 py-2 text-sm outline-none"
                        value={roomForm.roomNo} onChange={(e) => setRoomForm({...roomForm, roomNo: e.target.value})}
                    />
                    <input
                        type="text" required placeholder="Bed No" className="border border-slate-200 rounded-xl px-3.5 py-2 text-sm outline-none"
                        value={roomForm.bedNo} onChange={(e) => setRoomForm({...roomForm, bedNo: e.target.value})}
                    />
                    <select
                        className="border border-slate-200 rounded-xl px-3 py-2 text-sm outline-none bg-white"
                        value={roomForm.roomType} onChange={(e) => setRoomForm({...roomForm, roomType: e.target.value})}>
                        <option value="GENERAL">General Ward</option>
                        <option value="ICU">Intensive Care Unit (ICU)</option>
                    </select>
                    <input
                        type="number" step="0.01" required placeholder="Tariff AUD" className="border border-slate-200 rounded-xl px-3 py-2 text-sm outline-none"
                        value={roomForm.dailyRate} onChange={(e) => setRoomForm({...roomForm, dailyRate: e.target.value})}
                    />
                    <button type="submit" className="bg-blue-600 text-white px-5 py-2 rounded-xl text-sm font-semibold hover:bg-blue-700 transition">
                        Add Bed
                    </button>
                </form>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                {rooms.map((room, idx) => (
                    <div key={idx} className="bg-white border border-slate-200/80 rounded-2xl p-5 shadow-xs">
                        <h4 className="font-extrabold text-lg text-slate-900">{room.roomNo} ({room.roomType})</h4>
                        <p className="text-xs text-slate-500">Bed: {room.bedNo}</p>
                        <p className="mt-2 text-xs font-bold text-slate-800">${room.dailyRate} AUD/day</p>
                    </div>
                ))}
            </div>
        </div>
    );
}