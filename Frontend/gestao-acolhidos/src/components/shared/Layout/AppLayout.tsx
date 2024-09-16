import React from 'react';
import {Layout, Menu} from "antd";
import {NavigateFunction, Outlet, useNavigate} from "react-router-dom";
import Sider from 'antd/es/layout/Sider';
import { Router } from '../../../routes/types';

interface IAppLayout {
    menu:Router[],
    children:React.ReactNode
}


export const AppLayout:React.FC<IAppLayout> = ({menu}) => {
    const navigate:NavigateFunction = useNavigate();

    const menuItems = menu?.map((item: { label: string}, index:number) =>({
        key:index.toString(),
        label:`${item?.label}`,
    }));

    const handleRedirect = (pages:Router[], key:string) =>{
        if(pages){
            const index = Number(key);
            navigate(`${pages[index]?.path}`);
        }
    }

    const selectedLogout = (menu:Router[]):string[] =>{
        return menu.map((value,index) => value.path === 'logout' ? index.toString() : '0')
            .filter(e => e !== '0')
    }

    return (
        <>
        <Layout hasSider={true} style={{minHeight:'110vh'}}>
            <Sider 
                    breakpoint='lg'
                    collapsible
                    collapsedWidth='0'
                    style={{backgroundColor:"#2C3333", height:'120wh'}}
            >
                <Menu
                    triggerSubMenuAction={"click"}
                    style={{fontWeight:'700', justifyContent:'center', fontSize:'small', padding:'1.2rem 0', backgroundColor:'#2C3333'}}
                    selectable={false}
                    selectedKeys={selectedLogout(menu)}
                    mode={"vertical"}
                    theme={"dark"}
                    items={menuItems}
                    onClick={(e)=>{
                        handleRedirect(menu, e.key);
                    }}
                />
            </Sider>
            <Outlet />
        </Layout>
        </>
    );
};
