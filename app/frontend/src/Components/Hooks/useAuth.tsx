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
import { useQuery } from "react-query";
import { getProfile } from "../../Services/profile";
import { useNavigate } from "react-router-dom";

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
  profile: any;
  isLoading: boolean;
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
    location.replace("/");
  }

  const { data: profile, isLoading } = useQuery(
    ["profile", user?.id],
    () => getProfile(user?.id),
    { enabled: !!user }
  );

  return {
    user,
    setUser,
    token,
    setToken,
    isLoggedIn: !!user,
    logOut,
    profile,
    isLoading: isLoading,
  };
};

// AuthProvider component
const AuthProvider = ({ children }: { children?: ReactNode }) => {
  const [user, setUser] = useState<User | null>(null); // Initialize to fetch from local storage or server if needed
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = Cookies.get("token");
    if (token) {
      axios.defaults.headers.common["Authorization"] = `${token}`;
      me().then((res) => {
        setUser?.(res.data);
        setLoading(false);
      });
    } else {
      setLoading(false);
    }
  }, []);

  const value = {
    user,
    setUser,
  };

  return (
    <AuthContext.Provider value={value}>
      {!loading && children}
    </AuthContext.Provider>
  );
};

export { useAuth, AuthProvider };
