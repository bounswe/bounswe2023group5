import axios from "axios"

export async function MockPOST(body) {
    try {

        const response = await axios.post('https://httpbin.org/post', body);
        console.log(response.data);

    } catch (error) {
        console.error(error);
    }
}

export async function MockGET() {
    const response = await fetch("https://jsonplaceholder.typicode.com/todos")
    return await response.json()
}


export async function MockGET2() {
    const response = await fetch('https://api.publicapis.org/entries')
    return await response.json()
}

