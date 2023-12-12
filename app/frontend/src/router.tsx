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
import Groups from "./Pages/Groups/Groups";
import Group from "./Pages/Group/Group";
import CreateGroup from "./Pages/CreateGroup/CreateGroup";
import Achievement from "./Components/Achievement/Achievement/Achievement";
import Achievements from "./Pages/Achievement/Achievements";
import Profile from "./Pages/Profile/Profile";
import CreateAchievement from "./Pages/Admin/Achievement/CreateAchievement/CreateAchievement";
import DeleteAchievement from "./Pages/Admin/Achievement/DeleteAchievement/DeleteAchievement";
import Notifications from "./Pages/Notifications/Notifications";
import ReviewApplication from "./Pages/ReviewApplication/ReviewApplication";


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
        path: "game/detail/:gameId",
        element: <GameDetails />,
      },
      {
        path: "group",
        children: [
          {
            path: "create",
            element: <CreateGroup />,
          },
          {
            path: "detail/:groupId",
            element: <Group />,
          },
          {
            path: "review-application/:groupId",
            element: <ReviewApplication />,
          },
        ],
      },
      {
        path: "forum",
        children: [
          {
            path: "form",
            element: <ForumPostForm />,
          },
          {
            path: "detail/:forumId/:postId",
            element: <ForumPost />,
          },
        ],
      },
      {
        path: "achievements",
        element: <Achievements />,
      },
      {
        path: "notifications",
        element: <Notifications />,
      },
      {
        path: "games",
        element: <Games />,
      },
      {
        path: "groups",
        element: <Groups />,
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
      {
        path: "profile",
        element: <Profile />,
      },
      {
        path: "create-achievement",
        element: <CreateAchievement />,

      },
      {
        path: "delete-achievement",
        element: <DeleteAchievement />,
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
  {
    path: "profile",
    element: <Profile />,
  },
]);

export { router };
