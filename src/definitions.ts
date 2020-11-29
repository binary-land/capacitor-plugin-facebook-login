declare module '@capacitor/core' {
  interface PluginRegistry {
    FacebookLogin: FacebookLoginPlugin;
  }
}

export interface AuthData {
  id: string;
  access_token: string;
}
export interface FacebookLoginPlugin {
  logIn(options?: { permissions: string[] }): Promise<AuthData>;
  logOut(): Promise<void>;
  getCurrentUser(): Promise<AuthData>;
}
