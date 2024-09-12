import React from 'react';
import {BrowserRouter, Navigate, Route, Routes} from "react-router-dom";
import {Login} from "../components/pages/Login/Login.tsx";
import {Home} from "../components/pages/Home/Home.tsx";
import {Structure} from "../components/shared/Structure/Structure.tsx";
import {Button} from "antd";
import { useAuth } from '../hooks/useAuth.ts';

export type Router = {
    label:string,
    path:string,
    element:React.ReactElement,
    role:string[]
}
const routes:Router[] = [
    {
        label:'Inicio',
        path:"home",
        element:<Home />,
        role:['ADMIN', 'BOARD', 'SECRETARY']
    },
    {
        label:'Acolhimento Noturno',
        path:"reception",
        element:<Home />,
        role:['ADMIN', 'BOARD', 'SECRETARY']
    },
    {
        label:'Cadastro do Acolhido',
        path:"hosted",
        element:<Home />,
        role:['ADMIN', 'BOARD', 'SECRETARY']
    },
    {
        label:'Configurações',
        path:"config",
        element:<Home />,
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
    const filtered:Router[] = routes.filter((e) => e.role.includes(userRole));
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
                <Route path='/app' element={signed? <Structure /> : <ForbiddenAcces />}>
                    {routes.map((_,index) => <Route path={routes[index].path} element={routes[index].element} key={index} />)}
                </Route>
            </Routes>
        </BrowserRouter>
    )
}

export default RoutesReference;

