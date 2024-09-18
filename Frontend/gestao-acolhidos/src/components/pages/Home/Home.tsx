import React from 'react'
import welcome from '../../../assets/logo1.jpeg'
import { centerImgStyle, innerDivStyle, mainDivStyle } from './styles.ts'

export const Home:React.FC = () =>{

    return (
        <>
            <div style={mainDivStyle}>
                <h1>
                    Gestão de Acolhidos
                </h1>
                <div style={innerDivStyle}>
                    <img style={centerImgStyle} src={welcome} alt="welcome"/>
                </div>
                <footer style={{marginTop:'4rem'}}>
                    <p>
                        versão 1.0
                    </p>
                </footer>
                
            </div>
            
        </>
    )
}