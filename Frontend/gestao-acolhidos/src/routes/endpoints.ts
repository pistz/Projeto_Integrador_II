
export const endpoints = {
    host:process.env.BASE_URL,
}

export const authRoutes = {
    login:endpoints.host+'auth/login',
    userRole:endpoints.host+'auth/validate'
}

export const hostedRoutes = {
    findAll:endpoints.host+'hosted/find/all',
    create:endpoints.host+'hosted/create'
}

export const configRoutes = {
    getCapacity:endpoints.host+'capacity/get',
    updateCapacity:endpoints.host+'capacity/update'
}