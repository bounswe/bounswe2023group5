import { createBrowserRouter } from "react-router-dom";
import HelloWorld from "./Pages/HelloWorld/HelloWorld";
import MainLayout from "./Layout/MainLayout/MainLayout";
import ChangePassword from "./Pages/ChangePassword/ChangePassword";

const router = createBrowserRouter([
  {
    path: "/",
    element: <MainLayout />,
    children: [
      {
        path: "hello",
        element: <HelloWorld />,
      },
    ],
  },
  {
    path: "change",
    element: <ChangePassword />,
  },
]);

export { router };
