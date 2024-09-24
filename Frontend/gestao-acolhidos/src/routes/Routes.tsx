import React, { useEffect, useState } from 'react';
import {BrowserRouter, Navigate, Route, Routes} from "react-router-dom";
import {Login} from "../components/pages/Login/Login.tsx";
import {Home} from "../components/pages/Home/Home.tsx";
import {Structure} from "../components/shared/Structure/Structure.tsx";
import { useAuth } from '../hooks/useAuth.ts';
import { HostedServices } from '../components/pages/Hosted/HostedServices.tsx';
import { Router } from './types.ts';
import { hosted } from './HostedRoutes/HostedRoutes.tsx';
import { Config } from '../components/pages/Config/Config.tsx';
import {  ReceptionComponent } from '../components/pages/Reception/Reception.tsx';
import { Logout } from '../components/pages/Logout/Logout.tsx';
import { QueryReport } from '../components/pages/Reception/Reports/QueryReports/QueryReport.tsx';
import { useTableData } from '../hooks/useTableData.ts';
import { Reception } from '../entity/Reception/Reception.ts';

const mainRoutes:Router[] = [
    {
        label:'Inicio',
        path:"home",
        element:<Home />,
        role:['ADMIN', 'BOARD', 'SECRETARY'],
    },
    {
        label:'Pernoite',
        path:"reception",
        element:<ReceptionComponent />,
        role:['ADMIN', 'BOARD', 'SECRETARY']
    },
    {
        label:'Acolhidos',
        path:"hosted",
        element:<HostedServices />,
        role:['ADMIN', 'BOARD', 'SECRETARY'],

    },
    {
        label:'Configurações',
        path:"config",
        element:<Config />,
        role:['ADMIN', 'BOARD', 'SECRETARY']
    },
    {
        label:'Sair',
        path:"logout",
        element:<Logout />,
        role:['ADMIN', 'BOARD', 'SECRETARY']
    },
];


// eslint-disable-next-line react-refresh/only-export-components
export function filteredRoutes(userRole:string):Router[]{
    const filtered:Router[] = mainRoutes.filter((e) => e.role.includes(userRole));
    return filtered;
}

const RoutesReference:React.FC = () => {

    const {signed} = useAuth();
    const {receptionTableData} = useTableData();

    const [reportData, setReportData] = useState<Reception[]>(receptionTableData)

    const ForbiddenAcces:React.FC =()=>{
        return (<Navigate to='/' />)
    }

    useEffect(() =>{
        if(receptionTableData){
            setReportData(receptionTableData)
        }
    },[setReportData, receptionTableData])


    return (
        <BrowserRouter>
            <Routes>
                <Route path={'/'} element={<Login />}/>
                <Route path='*' element={<Login />} />
                <Route path='/login' element={<Login />}/>

                <Route path='/app/*' element={signed? <Structure /> : <ForbiddenAcces />}>
                    {mainRoutes.map((_,index) => <Route path={mainRoutes[index].path} element={mainRoutes[index].element} key={`${index} mainRoute`}/>)}
                    {hosted.map((_,index)=> <Route path={hosted[index].path} element={hosted[index].element} key={`${index} hostedRoutes`} />)}
                    <Route path='report/generatedValue' element={<QueryReport entity={reportData}/>} key={'receptionTableData'}/>
                </Route>
            </Routes>
        </BrowserRouter>
    )
}

export default RoutesReference;

