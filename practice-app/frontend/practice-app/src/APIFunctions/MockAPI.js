import axios from "axios"

export async function MockPOST(body) {
    try {

        const response = await axios.post('https://httpbin.org/post', body);
        console.log(response.data);
    } catch (error) {
        console.error(error);
    }
}

