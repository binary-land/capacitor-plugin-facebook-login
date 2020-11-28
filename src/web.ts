import { WebPlugin } from '@capacitor/core';
import { FacebookLoginPlugin } from './definitions';

export class FacebookLoginWeb extends WebPlugin implements FacebookLoginPlugin {
  constructor() {
    super({
      name: 'FacebookLogin',
      platforms: ['web'],
    });
  }

  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}

const FacebookLogin = new FacebookLoginWeb();

export { FacebookLogin };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(FacebookLogin);
