import { RouterProvider } from "react-router-dom";
import { router } from "./router";
import "./globals.scss";
import { QueryClient, QueryClientProvider } from "react-query";
import AntdConfigProvider from "./Components/Providers/AntdConfigProvider";

const queryClient = new QueryClient();

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <AntdConfigProvider>
        <RouterProvider router={router} />
      </AntdConfigProvider>
    </QueryClientProvider>
  );
}

export default App;
