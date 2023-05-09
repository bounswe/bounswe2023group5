import React, { useState } from 'react';
import "./LoginPage.scss";
import TextField from '@mui/material/TextField'
import Button from '@mui/material/Button'
import { useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';

function LoginPage() {
    const { register, handleSubmit } = useForm();
    const navigate = useNavigate();
    const onSubmit = ({ email }) => {
        localStorage.setItem("email", email)
        window.dispatchEvent(new Event('storage'))
        navigate("/api")
    }

    return <div className='login-page'>
        <span className='login-text'>
            Please provide an e-mail to use this service :)
        </span>
        <form onSubmit={handleSubmit(onSubmit)}>
            <TextField
                type="email"
                id="email"
                label="E-mail"
                {...register("email")}
            />
            <Button size="large" type='submit' variant="contained" color="primary">
                Enter
            </Button>
        </form>
    </div>;
}

export default LoginPage;