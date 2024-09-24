import { Router } from "../types.ts";
import { CreateHosted } from '../../components/pages/Hosted/CreateHosted/CreateHosted.tsx'

const app:string = '/app/'

export const hosted:Router[] = [
    {
        label:'Criar novo registro ',
        path:"hosted/create",
        fullpath:app,
        element: <CreateHosted />,
        role:['ADMIN', 'BOARD', 'SECRETARY'],
    },
]
