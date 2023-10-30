import { createBrowserRouter } from "react-router-dom";
import HelloWorld from "./Pages/HelloWorld/HelloWorld";
import MainLayout from "./Layout/MainLayout/MainLayout";
import ForgotPassword from "./Pages/ForgotPassword/ForgotPassword";
import ChangePassword from "./Pages/ChangePassword/ChangePassword";
import Login from "./Pages/Login/Login";
import Register from "./Pages/Register/Register";
import GameDetails from "./Pages/GameDetails/GameDetails";
import axios from "axios";
import Games from "./Pages/Games/Games";

axios.defaults.headers.common["Content-Type"] = "application/json";

const router = createBrowserRouter([
  {
    path: "/",
    element: <MainLayout />,
    children: [
      {
        path: "hello",
        element: <HelloWorld />,
      },
      {
        path: "/game/:gameId",
        element: <GameDetails />,
      },
      {
        path: "games",
        element: <Games />,
      },
    ],
  },
  {
    path: "ForgotPassword",
    element: <ForgotPassword />,
  },
  {
    path: "change",
    element: <ChangePassword />,
  },
  {
    path: "/login",
    element: <Login />,
  },
  {
    path: "/register",
    element: <Register />,
  },
]);

export { router };
