import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.scss';
import reportWebVitals from './reportWebVitals';
import {
  createBrowserRouter,
  RouterProvider,
} from "react-router-dom";
import MainContainer from './Containers/MainContainer/MainContainer';
import Test from './Pages/Test/Test';
import FormBuilder from './Components/FormBuilder/FormBuilder';
import APIPage from './Pages/APIPage/APIPage';
import apidata from './apidata';
import NavBar from './Components/NavBar/NavBar';



const router = createBrowserRouter([
  {
    path: "/",
    element: <MainContainer />,
    children: [
      {
        path: "test",
        element: <Test />
      },
      {
        path: "api/:name",
        loader: ({params})=> apidata[params.name],
        element: <APIPage/>
      },
      {
        path: "NavBar",
        element: <NavBar />
      }
    ]
  },
  
]);

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
