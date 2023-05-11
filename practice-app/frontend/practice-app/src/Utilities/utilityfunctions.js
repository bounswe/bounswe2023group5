export function getAPIUrl(){
    return process.env.REACT_APP_API_URL + process.env.REACT_APP_BASE_URL;
}

export function getUserEmail(){
    return localStorage.getItem("email")
}