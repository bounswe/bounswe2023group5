import React from "react";
import ReactDOM from "react-dom/client";
import { QueryCache, QueryClient, QueryClientProvider } from "react-query";

import App from "./App.tsx";
import { AuthProvider } from "./Components/Hooks/useAuth.tsx";
import { message } from "antd";
import { handleError } from "./Library/utils/handleError.ts";
import axios from "axios";

const queryClient = new QueryClient({
  queryCache: new QueryCache({
    onError(error) {
      handleError(error);
    },
  }),
});

axios.defaults.headers.post["Content-Type"] = "application/json";
axios.defaults.headers.common["Content-Type"] = "application/json";
axios.defaults.headers.get["Content-Type"] = "application/json";

ReactDOM.createRoot(document.getElementById("root")!).render(
  <React.StrictMode>
    <QueryClientProvider client={queryClient}>
      <AuthProvider>
        <App />
      </AuthProvider>
    </QueryClientProvider>
  </React.StrictMode>
);
