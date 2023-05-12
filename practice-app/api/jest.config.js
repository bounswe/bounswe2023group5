const config = {
    verbose: true,
    clearMocks: true,
    resetMocks: true,
    restoreMocks: true,
    forceExit: true,
    testEnvironment: "node",
    setupFilesAfterEnv: ['<rootDir>/setup-jest.js'],
};
export default config;