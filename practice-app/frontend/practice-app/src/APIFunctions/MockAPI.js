import axios from "axios"

export async function MockPOST(body) {
    try {
        //const response = await axios.post('https://httpbin.org/post', body);
        console.log(body);
    } catch (error) {
        console.error(error);
    }
}

export async function MockGET() {
    const response = await fetch("https://jsonplaceholder.typicode.com/todos")
    return await response.json()
}
