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
export const loginFormStyles:React.CSSProperties = {
    display:'flex',
    flexDirection:'column',
    margin:'1rem 1rem',
    position:'relative',
    alignContent:'center',
    justifyContent:'center'
}

export const LoginFormStyle:React.CSSProperties = {
    maxWidth: 60 ,
    display:'flex',
    flexDirection:'column',
    alignItems:'center',
    justifyContent:'center',
    margin:'3rem auto',
}


export const formStyles:React.CSSProperties = {
    padding:'0 1rem',
    height:'2rem',
    width:'20rem',
}

export const buttonsFormStyles:React.CSSProperties = {
    display:'flex',
    flexDirection:"row",
    alignItems:'center',
    justifyContent:'center',
    textAlign:'center',
}

export const LoginButtonStyle:React.CSSProperties = {
    margin:'0.5rem 1rem',
    height:'2rem',
    position:'relative',
    width:'12rem',
    backgroundColor:'#272B2A'
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