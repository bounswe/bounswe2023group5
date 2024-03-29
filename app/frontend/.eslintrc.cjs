module.exports = {
  root: true,
  env: { browser: true, es2020: true },
  extends: [
    "eslint:recommended",
    "plugin:@typescript-eslint/recommended",
    "plugin:react-hooks/recommended",
  ],
  ignorePatterns: ["dist", ".eslintrc.cjs"],
  parser: "@typescript-eslint/parser",
  plugins: ["react-refresh"],
  rules: {
    "react-refresh/only-export-components": [
      "warn",
      { allowConstantExport: true },
    ],
    "@typescript-eslint/no-unused-vars": "warn", // or 'off' to completely ignore unused variables
    "@typescript-eslint/no-explicit-any": "warn", // Changes error to a warning for 'no-explicit-any'
    "no-useless-catch": "warn", // Changes error to a warning for 'no-useless-catch'
    "@typescript-eslint/no-non-null-asserted-optional-chain": "warn",
  },
};
