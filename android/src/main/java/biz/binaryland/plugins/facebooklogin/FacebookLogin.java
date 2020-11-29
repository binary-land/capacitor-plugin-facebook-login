package biz.binaryland.plugins.facebooklogin;

import android.content.Intent;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

import org.json.JSONException;

import java.util.Collection;

@NativePlugin(
    requestCodes = {FacebookLogin.REQUEST_FACEBOOK_LOGIN}
)
public class FacebookLogin extends Plugin {
  protected static final int REQUEST_FACEBOOK_LOGIN = 0xface;

  private CallbackManager callbackManager;

  @Override
  public void load() {
    callbackManager = CallbackManager.Factory.create();

    LoginManager.getInstance().registerCallback(callbackManager,
        new FacebookCallback<LoginResult>() {
          @Override
          public void onSuccess(LoginResult loginResult) {
            PluginCall savedCall = getSavedCall();

            if (savedCall == null) {
              return;
            }

            AccessToken accessToken = loginResult.getAccessToken();

            JSObject ret = new JSObject();
            ret.put("id", accessToken.getUserId());
            ret.put("access_token", accessToken.getToken());
            savedCall.success(ret);

            saveCall(null);
          }

          @Override
          public void onCancel() {
            PluginCall savedCall = getSavedCall();

            if (savedCall == null) {
              return;
            }

            savedCall.reject(null);

            saveCall(null);
          }

          @Override
          public void onError(FacebookException exception) {
            PluginCall savedCall = getSavedCall();

            if (savedCall == null) {
              return;
            }

            savedCall.reject(exception.toString());

            saveCall(null);
          }
        });
  }

  @Override
  protected void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
    callbackManager.onActivityResult(requestCode, resultCode, data);
  }

  @PluginMethod
  public void logIn(PluginCall call) {
    PluginCall savedCall = getSavedCall();

    if (savedCall != null) {
      return;
    }

    Collection<String> permissions = null;

    try {
      permissions = call.getArray("permissions").toList();
    } catch (JSONException e) {
      Log.e(getLogTag(), "Invalid Parameter", e);
    }

    LoginManager.getInstance().logInWithReadPermissions(getActivity(), permissions);

    saveCall(call);
  }

  @PluginMethod
  public void logOut(PluginCall call) {
    LoginManager.getInstance().logOut();

    call.success();
  }

  @PluginMethod
  public void getCurrentUser(PluginCall call) {
    AccessToken accessToken = AccessToken.getCurrentAccessToken();

    JSObject ret = new JSObject();
    if (accessToken != null) {
      ret.put("id", accessToken.getUserId());
      ret.put("access_token", accessToken.getToken());
    }
    call.success(ret);
  }
}
