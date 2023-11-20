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
import { default as AdminMain } from "./Pages/Admin/Main/Main";
import CreateTag from "./Pages/Admin/Tag/CreateTag/CreateTag";
import UpdateTag from "./Pages/Admin/Tag/UpdateTag/UpdateTag";
import CreateGame from "./Pages/Admin/Game/CreateGame/CreateGame";
import DeleteTag from "./Pages/Admin/Tag/DeleteTag/DeleteTag";
import BanUser from "./Pages/Admin/User/BanUser/BanUser";
import GiveAdminPermission from "./Pages/Admin/User/GiveAdminPermission/GiveAdminPermission";

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
      {
        path: "admin",
        element: <AdminMain />,
      },
      {
        path: "create-tag",
        element: <CreateTag />,
      },
      {
        path: "update-tag",
        element: <UpdateTag />,
      },
      {
        path: "delete-tag",
        element: <DeleteTag />,
      },
      {
        path: "create-game",
        element: <CreateGame />,
      },
      {
        path: "ban-user",
        element: <BanUser />,
      },
      {
        path: "admin-permission",
        element: <GiveAdminPermission />,
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
