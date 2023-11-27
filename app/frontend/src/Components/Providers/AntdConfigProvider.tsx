import { ConfigProvider, ThemeConfig } from "antd";
import { ReactNode } from "react";

export function getThemeColor(name: string) {
  if (document.body) {
    return getComputedStyle(document.body).getPropertyValue(`--${name}`);
  }
}

function AntdConfigProvider({ children }: { children: ReactNode }) {
  const theme: ThemeConfig = {
    token: {
      colorBgBase: getThemeColor("color-background"),
      colorTextBase: getThemeColor("color-text"),
      colorPrimary: getThemeColor("color-primary"),
      colorBgContainer: "#fefdf5",
      colorBorder: "#ddd68e",
    },
  };
  return <ConfigProvider theme={theme}>{children}</ConfigProvider>;
}

export default AntdConfigProvider;
