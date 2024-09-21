
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
    findById:endpoints.host+'hosted/find/',
    create:endpoints.host+'hosted/create',
    edit:endpoints.host+'hosted/update-main-info/',
    deleteHosted:endpoints.host+'hosted/delete/',
    updateDocs:endpoints.host+'hosted/update-docs/',
    updateRefAddress:endpoints.host+'hosted/update-ref-address/',
    updatePoliceReport:endpoints.host+'hosted/update-police-report/',
    updateFamilyComposition:endpoints.host+'hosted/update-has-family/',
    updateFamilyTable:endpoints.host+'hosted/update-family-member/',
    updateSituationalRisk:endpoints.host+'hosted/update-risk/',
    updateSocialPrograms:endpoints.host+'hosted/update-socials/',
    updateMedicalRecord:endpoints.host+'hosted/update-medical-record/',
    updateCustomTreatments:endpoints.host+'hosted/update-treatments/'
}

export const configRoutes = {
    getCapacity:endpoints.host+'capacity/get',
    updateCapacity:endpoints.host+'capacity/update'
}

export const receptionRoutes = {
    getAll:endpoints.host+'night-recepion/find/all',
    createReception:endpoints.host+'night-reception/create',
    deleteReception:endpoints.host+'night-reception/delete/',
}