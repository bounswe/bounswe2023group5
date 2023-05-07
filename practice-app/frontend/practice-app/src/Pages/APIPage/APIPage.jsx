import React from 'react';
import { useLoaderData } from 'react-router-dom';
import FormBuilder from '../../Components/FormBuilder/FormBuilder';
import NavBar from '../../Components/NavBar/NavBar';


function APIPage({}) {
    const apiData = useLoaderData();
    const onSubmit = (data) => console.log(data);
    return (
        
    <div>
        <NavBar></NavBar>
        <FormBuilder inputs={apiData.form.inputs} buttonText={apiData.form.buttonText} onSubmit={onSubmit}/>
    </div>);
}

export default APIPage;