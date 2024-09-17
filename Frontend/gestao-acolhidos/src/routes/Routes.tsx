import React from 'react';
import {BrowserRouter, Navigate, Route, Routes} from "react-router-dom";
import {Login} from "../components/pages/Login/Login.tsx";
import {Home} from "../components/pages/Home/Home.tsx";
import {Structure} from "../components/shared/Structure/Structure.tsx";
import {Button} from "antd";
import { useAuth } from '../hooks/useAuth.ts';
import { HostedServices } from '../components/pages/Hosted/HostedServices.tsx';
import { Router } from './types.ts';
import { hosted } from './HostedRoutes/HostedRoutes.tsx';
import { Config } from '../components/pages/Config/Config.tsx';

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
        element:<Home />,
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
        element:<Button onClick={()=> {sessionStorage.clear(); window.location.reload()}}>Clique aqui para confirmar a saída</Button>,
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

    const ForbiddenAcces:React.FC =()=>{
        return (<Navigate to='/' />)
    }

    return (
        <BrowserRouter>
            <Routes>
                <Route path={'/'} element={<Login />}/>
                <Route path='*' element={<Login />} />
                <Route path='/login' element={<Login />}/>

                <Route path='/app/*' element={signed? <Structure /> : <ForbiddenAcces />}>
                    {mainRoutes.map((_,index) => <Route path={mainRoutes[index].path} element={mainRoutes[index].element} key={index}/>)}
                    {hosted.map((_,index)=> <Route path={hosted[index].path} element={hosted[index].element} key={index+10} />)}
                </Route>
            </Routes>
        </BrowserRouter>
    )
}

export default RoutesReference;

