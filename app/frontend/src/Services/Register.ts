
async function postLogin(formData: any){
    return fetch(import.meta.env.VITE_APP_API_URL + "/auth/register", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(formData),
    });
  };

export default postLogin;