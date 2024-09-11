import styled from "styled-components";

export const MainContainer = styled.div`
    display: flex;
    position: relative;
    flex-direction: column;
    width: 100%;
    justify-content: center;
    align-items: center;
    margin: 0 auto;
    padding-top: 8rem;
    height: 100%;
`

export const LoginInputStyle:React.CSSProperties = {
    position:'relative',
    width:'16rem',
}

export const LoginFormStyle:React.CSSProperties = {
    maxWidth: 40 ,
    display:'flex',
    flexDirection:'column',
    alignItems:'center',
    justifyContent:'center',
    margin:'3rem auto',
}

export const LoginButtonStyle:React.CSSProperties = {
    position:'relative',
    alignSelf:'center',
    justifySelf:'center'
}

export const LoginH2Style:React.CSSProperties = {
    textOrientation:'inherit',
    textAlign:'center',
    fontSize:'x-large',
    fontWeight:'900'
}

export const LoginSpanStyle:React.CSSProperties = {
    padding:'4rem 0',
    margin:'0 auto',
    display:'flex',
    alignSelf:"center",
    justifySelf:'center',
    flexDirection:'row'
}