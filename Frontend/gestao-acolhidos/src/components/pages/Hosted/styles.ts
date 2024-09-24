import React from "react"

export const mainDivStyle:React.CSSProperties = {
    display: 'flex',
    justifyContent:'flex-start',
    flexDirection:'column',
    alignItems:'center',
    margin:'1rem auto',
    width:'100%',
    scrollBehavior:'smooth'
}

export const tableContainer:React.CSSProperties = {
    display:'flex', 
    flexDirection:'column',
    flexWrap:'wrap', 
    width:'100%', 
    alignItems:'center', 
    justifyContent:'center', 
    overflow:'auto', 
    position:"relative"
}

export const subMenuDivStyle:React.CSSProperties = {
    padding:"1rem 1rem", 
    display:"flex", 
    flexDirection:'row', 
    justifyContent:'center', 
    alignItems:"center",
    margin:'1rem'
}

export const hostedInputForm:React.CSSProperties = {
    width:'20rem'
}