import axios from "axios";
import Cookies from "js-cookie";
import {
  PropsWithChildren,
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
  }

  useEffect(() => {
    if (token) {
      axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;

      me().then((res) => {
        setUser?.(res.data);
      });
    }
  }, [token]);

  return { user, setUser, token, setToken, isLoggedIn: !!user };
};

// AuthProvider component
const AuthProvider = ({ children }: PropsWithChildren<{}>) => {
  const [user, setUser] = useState<User | null>(null); // Initialize to fetch from local storage or server if needed

  const value = {
    user,
    setUser,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export { useAuth, AuthProvider };
