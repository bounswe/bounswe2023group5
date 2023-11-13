import { createBrowserRouter, redirect } from "react-router-dom";
import HelloWorld from "./Pages/HelloWorld/HelloWorld";
import MainLayout from "./Layout/MainLayout/MainLayout";
import ForgotPassword from "./Pages/ForgotPassword/ForgotPassword";
import ChangePassword from "./Pages/ChangePassword/ChangePassword";
import Login from "./Pages/Login/Login";
import Register from "./Pages/Register/Register";
import GameDetails from "./Pages/GameDetails/GameDetails";
import axios from "axios";
import Games from "./Pages/Games/Games";
import ForumPostForm from "./Pages/ForumPostForm/ForumPostForm";
import ForumPost from "./Pages/ForumPost/ForumPost";

axios.defaults.headers.common["Content-Type"] = "application/json";

const router = createBrowserRouter([
  {
    path: "/",
    loader: async () => {
      return redirect("/games");
    },
  },
  {
    element: <MainLayout />,

    children: [
      {
        path: "hello",
        element: <HelloWorld />,
      },
      {
        path: "game/:gameId",
        element: <GameDetails />,
      },
      {
        path: "forum",
        children: [
          {
            path: "form",
            element: <ForumPostForm />,
          },
          {
            path: "detail/:postId",
            element: <ForumPost />,
          },
        ],
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
    path: "login",
    element: <Login />,
  },
  {
    path: "register",
    element: <Register />,
  },
]);

export { router };
