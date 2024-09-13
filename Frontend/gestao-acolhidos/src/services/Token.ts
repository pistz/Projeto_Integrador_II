const tokenId:string = String(process.env.TOKEN_ID);

export const getTokenFromLocalStorage = () =>{
    const token_id = getTokenId();
    const getToken = sessionStorage.getItem(token_id);
    if(getToken){
        const token = JSON.parse(getToken);
        return token;
    }
}

export const authHeader = () => {
    const token = getTokenFromLocalStorage();
    return {
        headers: {
            Authorization: "Bearer " + token,
        },
    };
};

export const getTokenId = () =>{
    return tokenId;
}