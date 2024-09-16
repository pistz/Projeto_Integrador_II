import React from 'react'
import { NavLink } from 'react-router-dom'
type Nav = {
    to:string,
    title:string
}

export const Link:React.FC<Nav> = ({to, title}) => {
  return (
    <NavLink 
        to={to}
        style={{textDecoration:'none', margin:"0 1rem"}}
    
    >{title}</NavLink>
  )
}
