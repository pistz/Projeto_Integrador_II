import { useContext } from 'react';
import AuthContext from '../context/authContext/authContext';


export const useAuth = () => {
    const context = useContext(AuthContext);

    return context;
}