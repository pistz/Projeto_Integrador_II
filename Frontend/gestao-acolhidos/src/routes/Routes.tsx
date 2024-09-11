import React from 'react';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import {Login} from "../components/pages/Login/Login.tsx";
import {Home} from "../components/pages/Home/Home.tsx";
import {Structure} from "../components/shared/Structure/Structure.tsx";
import {Button} from "antd";

export type Router = {
    label:string,
    path:string,
    element:React.ReactElement,
    role:string[]
}
export const routes:Router[] = [
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
        label:'Logout',
        path:"logout",
        element:<Button>Confirma?</Button>,
        role:['ADMIN', 'BOARD', 'SECRETARY']
    },
];

export function filteredRoutes(userRole:string):Router[]{
    const filtered:Router[] = routes.filter((e) => e.role.includes(userRole));
    return filtered;
}

export const RoutesReference:React.FC = () => {

    // const ForbiddenAcces:React.FC =()=>{
    //     return (<Navigate to='/' />)
    // }

    return (
        <BrowserRouter>
            <Routes>
                <Route path={'/'} element={<Login />}/>
                <Route path='*' element={<Login />} />
                <Route path='/login' element={<Login />}/>
                <Route path='/app' element={<Structure />}>
                    {routes.map((_,index) => <Route path={routes[index].path} element={routes[index].element} key={index} />)}
                </Route>
            </Routes>
        </BrowserRouter>
    )
}