import { WebPlugin } from '@capacitor/core';

import { AuthData, FacebookLoginPlugin } from './definitions';

declare const FB: any;

export class FacebookLoginWeb extends WebPlugin implements FacebookLoginPlugin {
  constructor() {
    super({
      name: 'FacebookLogin',
      platforms: ['web'],
    });
  }

  logIn(options: { permissions: string[] }): Promise<AuthData> {
    return new Promise((resolve, reject) => {
      try {
        FB.login(
          (response: any) => {
            if (response.status === 'connected') {
              const { userID, accessToken } = response.authResponse;
              resolve({
                id: userID,
                access_token: accessToken,
              });
            } else {
              resolve();
            }
          },
          {
            scope: options?.permissions?.join(','),
          },
        );
      } catch (error) {
        reject(error);
      }
    });
  }

  logOut(): Promise<void> {
    return new Promise((resolve, reject) => {
      try {
        FB.logout(() => {
          resolve();
        });
      } catch (error) {
        reject(error);
      }
    });
  }

  getCurrentUser(): Promise<AuthData> {
    return new Promise((resolve, reject) => {
      try {
        FB.getLoginStatus((response: any) => {
          if (response.status === 'connected') {
            const { userID, accessToken } = response.authResponse;
            resolve({
              id: userID,
              access_token: accessToken,
            });
          } else {
            reject();
          }
        });
      } catch (error) {
        reject(error);
      }
    });
  }
}

const FacebookLogin = new FacebookLoginWeb();

export { FacebookLogin };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(FacebookLogin);
