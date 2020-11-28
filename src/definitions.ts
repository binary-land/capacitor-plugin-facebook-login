declare module '@capacitor/core' {
  interface PluginRegistry {
    FacebookLogin: FacebookLoginPlugin;
  }
}

export interface FacebookLoginPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
