import React, { createContext, useState } from 'react';
import { AuthContextData, IChildren } from '../types';


const AuthContext = createContext<AuthContextData>({} as AuthContextData);

export const AuthProvider: React.FC<IChildren> = ({ children }:IChildren) => {

    const [signed, setSigned] = useState<boolean>(false);

    const [userEmail, setUserEmail] = useState<string>("");

    const [userRole, setUserRole] = useState<string>("");
    
    return (
    
    <AuthContext.Provider value={{ signed, setSigned,
                userEmail, 
                setUserEmail, 
                userRole, 
                setUserRole, 
                }}>
        {children}
    </AuthContext.Provider>
    );
};

export default AuthContext;