import React from 'react';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import {Login} from "../components/pages/Login/Login.tsx";

// const routes = [
//     {
//         path:"home",
//         element:<></>,
//         role:['ADMIN', 'BOARD', 'SECRETARY']
//     }
// ];

export const RoutesReference:React.FC = () => {

    return (
        <BrowserRouter>
            <Routes>
                <Route path={'/'} element={<Login />}/>
                <Route path='*' element={<Login />} />
                <Route path='/login' element={<Login />}/>
            </Routes>
        </BrowserRouter>
    )
}