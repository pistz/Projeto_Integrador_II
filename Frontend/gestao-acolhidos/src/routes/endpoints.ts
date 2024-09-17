
export const endpoints = {
    host:process.env.BASE_URL,
}

export const userRoutes = {
    getAll:endpoints.host+'user/find/all',
    register:endpoints.host+'user/register',
    delete:endpoints.host+'user/delete/'
}

export const authRoutes = {
    login:endpoints.host+'auth/login',
    userRole:endpoints.host+'auth/validate'
}

export const hostedRoutes = {
    findAll:endpoints.host+'hosted/find/all',
    create:endpoints.host+'hosted/create',
    edit:endpoints.host+'hosted/update-main-info/',
    updateDocs:endpoints.host+'hosted/update-docs/'
}

export const configRoutes = {
    getCapacity:endpoints.host+'capacity/get',
    updateCapacity:endpoints.host+'capacity/update'
}