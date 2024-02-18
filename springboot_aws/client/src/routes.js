import React from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";

import Login from './pages/Login';
import Books from './pages/Books';

export default function RoutesApp(){
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Login/>} />
                <Route path="/books" element={<Books/>} />
            </Routes>
        </BrowserRouter>
    );
}