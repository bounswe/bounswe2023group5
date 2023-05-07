import React from 'react';
import { useLoaderData } from 'react-router-dom';
import FormBuilder from '../../Components/FormBuilder/FormBuilder';
import NavBar from '../../Components/NavBar/NavBar';


function APIPage({}) {
    const apiData = useLoaderData();
    const onSubmit = async (data) => {
        if(apiData.postFunction) {
            await apiData.postFunction(data)
        } else {
            console.log(`You tried to post but dont have a post function set, heres what you posted \n ${JSON.stringify(data)}`)
        }
    }
    return (
        
    <div>
        <NavBar></NavBar>
        <FormBuilder inputs={apiData.form.inputs} buttonText={apiData.form.buttonText} onSubmit={onSubmit}/>
    </div>);
}

export default APIPage;