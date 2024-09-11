import React from 'react';
import {Layout, Menu} from "antd";
import {NavigateFunction, useNavigate} from "react-router-dom";
import {Router} from "../../../routes/Routes.tsx";

interface IAppLayout {
    children:React.ReactNode,
    menu:Router[]
}


export const AppLayout:React.FC<IAppLayout> = ({children, menu}) => {
    const navigate:NavigateFunction = useNavigate();

    const menuItems = menu?.map((item: { label: string; }, index:number) =>({
        key:index.toString(),
        label:`${item?.label}`
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
        <Layout >
                <Menu
                    triggerSubMenuAction={"click"}
                    style={{fontWeight:'900', justifyContent:'center', fontSize:'x-large', padding:'1.2rem 0'}}
                    selectable={false}
                    selectedKeys={selectedLogout(menu)}
                    mode={"horizontal"}
                    theme={"dark"}
                    items={menuItems}
                    onClick={(e)=>{
                        handleRedirect(menu, e.key);
                    }}
                />
            {children}
        </Layout>
    );
};
