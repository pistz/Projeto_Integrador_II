import React from 'react';
// import {filteredRoutes} from "../../../routes/Routes.tsx";
import {AppLayout} from "../Layout/AppLayout.tsx";
import {Outlet} from "react-router-dom";
import {routes} from "../../../routes/Routes.tsx";

export const Structure:React.FC = () => {

    //TODO - incluir context hook
    // const filteredMenu = filteredRoutes(userRole);

    return (
        <>
            <AppLayout menu={routes}>
                <Outlet />
            </AppLayout>
        </>
    );
};