import React from 'react'
import welcome from '../../../assets/Designer.jpeg'
import { centerImgStyle, innerDivStyle, mainDivStyle } from './styles.ts'
import { Divider } from 'antd'

export const Home:React.FC = () =>{

    return (
        <>
            <div style={mainDivStyle}>
                <Divider variant='dotted'>Bem vindo</Divider>
                <div style={innerDivStyle}>
                    <img style={centerImgStyle} src={welcome} alt="welcome"/>
                </div>
                <footer style={{marginTop:'4rem'}}>
                    <p>
                        Gestão de Acolhidos - versão 1.0
                    </p>
                </footer>
                
            </div>
            
        </>
    )
}