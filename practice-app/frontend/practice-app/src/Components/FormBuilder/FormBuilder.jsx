import React, { useRef } from 'react';

import { useForm } from "react-hook-form";
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField'
import { FormControlLabel, InputLabel, Select, Switch } from '@mui/material';
import { FormControl } from '@mui/base';
import "./FormBuilder.scss";


function FormBuilder({ inputs, buttonText, onSubmit }) {
  /*
  const form = [
    {
      type: "text",
      name: "test",
      label: "Test",
    },
    {
      type: "text",
      name: "test2",
      label: "Test2"
    },
    {
      type: "select",
      name: "selecttest",
      label: "select    test",

      options: [
        {
          name: "option1asdads",
          value: "value1"
        },
        {
          name: "option2",
          value: "value2"
        }
      ]
    }
  ]*/

  const { register, handleSubmit } = useForm();

  function buildForm(inputs) {
    return inputs.map((item, index) => {
      if (item.type == "text") {
        return <div className='form-item' key={index}>
          <TextField
            fullWidth

            key={item.name}
            id={item.name}
            label={item.label}
            {...register(item.name)}
          />
        </div>
      }
      if (item.type == "number") {
        return <div className='form-item' key={index}>
          <TextField
            fullWidth
            inputProps={{ inputMode: 'numeric', pattern: '[0-9]*' }}
            key={item.name}
            id={item.name}
            label={item.label}
            helperText="Please only input numbers."
            {...register(item.name)}
          />
        </div>
      }
      if (item.type == "bool") {
        return <div className='form-item' key={index}>
          <FormControlLabel
            key={item.name}

            control={<Switch
              id={item.name}
              {...register(item.name)}
            />}
            label={item.label}
          />
          
        </div>
      }
      if (item.type == "select") {

        return <div className='form-item' key={index}>
          <TextField
            fullWidth
            select
            SelectProps={{
              native: true,
            }}
            key={item.name}
            id={item.name}
            label={item.label}
            {...register(item.name)}
          >

            {item.options?.map(option =>
              <option key={option.value} value={option.value}>{option.name}</option>
            )}
          </TextField>
        </div>
      }
    }
    )
  }
  return (
    <div className='form-builder' key='form-builder'>

      <form className='form' onSubmit={handleSubmit(onSubmit)}>
        <div className='form-items'>
          {
            buildForm(inputs)
          }
        </div>

        <div className='button-container'>
          <Button variant="outlined" size="large" label="Submit" type="submit"> {buttonText} </Button>
        </div>

      </form>


    </div>


  );
}

export default FormBuilder;