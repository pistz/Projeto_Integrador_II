import React from 'react';
import {AppLayout} from "../Layout/AppLayout.tsx";
import {Outlet} from "react-router-dom";
import { useAuth } from '../../../hooks/useAuth.ts';
import { filteredRoutes } from '../../../routes/Routes.tsx';

export const Structure:React.FC = () => {

    const {userRole} = useAuth()
    const filteredMenu = filteredRoutes(userRole);

    return (
        <>
        <AppLayout menu={filteredMenu}>
            <Outlet />
        </AppLayout>
        </>
    );
};