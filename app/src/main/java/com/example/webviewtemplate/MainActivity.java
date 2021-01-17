package com.example.webviewtemplate;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private WebView webView;

    String url="https://jivoclub.ru/";


    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);*/

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        //actionBar.setTitle(Html.fromHtml("<font color=\"Black\">" + getString(R.string.app_name) + "</font>"));
        actionBar.hide();

        webView = findViewById(R.id.web);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptEnabled(true);
        //webView.addJavascriptInterface(new WebAppInterface(this), "Android");
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setGeolocationEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                // callback.invoke(String origin, boolean allow, boolean remember);
                callback.invoke(origin, true, false);
            }
        });
        webView.setWebViewClient(new WebViewClient());
        //webView.getSettings().setSupportZoom(true);
        //webView.getSettings().setDisplayZoomControls(false);


        if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        webView.getSettings().getLoadsImagesAutomatically();


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                /*if (url == null || url.startsWith("http://") || url.startsWith("https://") || url.startsWith("tel://") || url.startsWith("telegram:"))
                    return false;
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    view.getContext().startActivity(intent);
                    return true;
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Приложение не найдено", Toast.LENGTH_SHORT).show();
                    return true;
                }
                /*if (url.contains(url)) {
                    return false;
                }
                // все остальные ссылки будут спрашивать какой браузер открывать
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;*/

                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.cancel();
            }

        });
        webView.loadUrl(url);
    }



    private class MyWebViewClient extends WebViewClient {
        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }
        // Для старых устройств
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String  url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()) {
            webView.goBack();
        } else {
            //super.onBackPressed();
            new AlertDialog.Builder(this)
                    .setTitle("Выйти из приложения")
                    .setMessage("Вы действительно хотите выйти?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            //MainActivity.super.onBackPressed();
                            finishAffinity();
                        }
                    }).create().show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.oby) {
            webView.reload();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


