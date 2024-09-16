import { ReactNode } from 'react'
import { TableContainerStyle } from './styles'


interface TableContainerProps {
    children:ReactNode
}

export const TableContainer:React.FC<TableContainerProps> = ({children}) => {

  return (

    <div style={TableContainerStyle}>
            {children}
    </div>

  )
}