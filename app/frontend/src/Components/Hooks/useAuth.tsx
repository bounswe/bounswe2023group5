import axios from "axios";
import Cookies from "js-cookie";
import {
  PropsWithChildren,
  ReactNode,
  createContext,
  useContext,
  useEffect,
  useState,
} from "react";
import { me } from "../../Services/me";

type User = any;

interface AuthContextProps {
  user: User | null;
  setUser: React.Dispatch<React.SetStateAction<User | null>>;
}

// Create a context for auth
const AuthContext = createContext<AuthContextProps>({
  user: null,
  setUser: () => {},
});

interface UseAuthProps extends AuthContextProps {
  token?: string;
  setToken: (token: string) => void;
  isLoggedIn: boolean;
  logOut: () => void;
}

// Custom hook to use auth
const useAuth = (): UseAuthProps => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  const { user, setUser } = context;

  const token = Cookies.get("token");

  function setToken(token: string) {
    Cookies.set("token", token);
    axios.defaults.headers.common["Authorization"] = `${token}`;
  }

  function logOut() {
    Cookies.remove("token");
    location.reload();
  }

  return { user, setUser, token, setToken, isLoggedIn: !!user, logOut };
};

// AuthProvider component
const AuthProvider = ({ children }: { children?: ReactNode }) => {
  const [user, setUser] = useState<User | null>(null); // Initialize to fetch from local storage or server if needed

  useEffect(() => {
    const token = Cookies.get("token");
    if (token) {
      axios.defaults.headers.common["Authorization"] = `${token}`;
      me().then((res) => {
        setUser?.(res.data);
      });
    }
  }, []);

  const value = {
    user,
    setUser,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export { useAuth, AuthProvider };
