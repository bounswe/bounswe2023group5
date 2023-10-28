import { ConfigProvider, ThemeConfig } from "antd";
import { PropsWithChildren } from "react";

function getThemeColor(name: string) {
  if (document.body) {
    return getComputedStyle(document.body).getPropertyValue(`--${name}`);
  }
}

function AntdConfigProvider({ children }: PropsWithChildren<{}>) {
  const theme: ThemeConfig = {
    token: {
      colorBgBase: getThemeColor("color-background"),
      colorTextBase: getThemeColor("color-text"),
      colorPrimary: getThemeColor("color-primary"),
      colorBgContainer: getThemeColor("color-container"),
    },
  };
  console.log(theme);
  return <ConfigProvider theme={theme}>{children}</ConfigProvider>;
}

export default AntdConfigProvider;
