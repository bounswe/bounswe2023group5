import axios from "axios";

async function postEmail  (email:string) {
    return fetch(import.meta.env.VITE_APP_API_URL + "/auth/forgot-password", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({email})
    });
  }


async function postPassword (newPassword:any) {
    const authorizationHeader = axios.defaults.headers.common['Authorization'];
    const headers:any = {
      'Content-Type': 'application/json',
    };
  
    if (authorizationHeader) {
      headers['Authorization'] = authorizationHeader;
    }

    return fetch(import.meta.env.VITE_APP_API_URL + "/auth/change-forgot-password", {
      method: "POST",
      headers:headers,
      body: JSON.stringify(newPassword)
      });
  }


async function postCode (data:any) {
    return fetch(import.meta.env.VITE_APP_API_URL + "/auth/verify-reset-code", {
    method: "POST",
    headers: {
    "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
});
}
export {postPassword, postCode, postEmail};
