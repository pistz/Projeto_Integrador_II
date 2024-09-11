import React from 'react'
import welcome from '../../../assets/logo1.jpeg'
import { imgStyle, innerDivStyle, mainDivStyle } from './styles.ts'

export const Home:React.FC = () =>{

    return (
        <>
            <div style={mainDivStyle}>
                <h1>
                    Gestão de Acolhidos
                </h1>
                <div style={innerDivStyle}>
                    <img style={imgStyle} src={welcome} alt="welcome"/>
                </div>
                <p>
                    versão 1.0
                </p>
            </div>
        </>
    )
}