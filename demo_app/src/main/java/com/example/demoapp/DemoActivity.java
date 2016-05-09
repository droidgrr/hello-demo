package com.example.demoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.text.InputType;
import android.app.AlertDialog;
import android.content.Context;
import android.widget.EditText;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Button;
import android.widget.LinearLayout;

import com.freshdesk.hotline.Hotline;
import com.freshdesk.hotline.HotlineConfig;

public class DemoActivity extends AppCompatActivity {

    private Hotline hotline;
    private View configureAppView, showSupportView, showFAQView;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        configureAppView = (Button) findViewById(R.id.configure_app);
        showSupportView = (Button) findViewById(R.id.launch_support);
        showFAQView = (Button) findViewById(R.id.launch_faq);
        
        showFAQView.setOnClickListener(viewClickListener);
        showSupportView.setOnClickListener(viewClickListener);
        configureAppView.setOnClickListener(viewClickListener);
        
        if(!getStringFromPrefs(DemoActivity.this, "id").isEmpty() && !getStringFromPrefs(DemoActivity.this, "key").isEmpty()) {
            String id = getStringFromPrefs(DemoActivity.this, "id");
            String key = getStringFromPrefs(DemoActivity.this, "key");
            HotlineConfig hotlineConfig = new HotlineConfig(id, key);
            getHotlineInstance().init(hotlineConfig);
        }

    }

    private Hotline getHotlineInstance() {
        if (hotline == null) {
            hotline = Hotline.getInstance(getApplicationContext());
        }
        return hotline;
    }

    View.OnClickListener viewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.configure_app:
                    showAppInfoDialog();
                    break;

                case R.id.launch_faq:
                    Hotline.showFAQs(DemoActivity.this);
                    break;

                case R.id.launch_support:
                    Hotline.showConversations(DemoActivity.this);
                    break;
            }
        }
    };

    private void showAppInfoDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("App Config");
        LinearLayout layout = new LinearLayout(this);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText idText = new EditText(this);
        idText.setPadding(16, 16, 16, 16);
        idText.setHint("id");
        idText.setText(getStringFromPrefs(DemoActivity.this, "id"));
        idText.setInputType(InputType.TYPE_CLASS_TEXT);
        layout.addView(idText);

        final EditText keyText = new EditText(this);
        keyText.setPadding(16, 16, 16, 16);
        keyText.setHint("key");
        keyText.setText(getStringFromPrefs(DemoActivity.this, "key"));
        keyText.setInputType(InputType.TYPE_CLASS_TEXT);
        layout.addView(keyText);

        dialogBuilder.setView(layout);

        dialogBuilder.setPositiveButton("Configure", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String id = idText.getText().toString();
                String key = keyText.getText().toString();
                
                if (id != null && !id.isEmpty() && key != null && !key.isEmpty()) {
                    setStringToPrefs(DemoActivity.this, "id", id);
                    setStringToPrefs(DemoActivity.this, "key", key);
                    HotlineConfig hotlineConfig = new HotlineConfig(id, key);
                    getHotlineInstance().init(hotlineConfig);
                }
            }
        });

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled
            }
        });
        dialogBuilder.show();
    }

    private static SharedPreferences getPrefs(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context can't be null");
        }
        return context.getSharedPreferences("prefs", 0);
    }

    private static Editor getPrefsEditor(Context context) {
        SharedPreferences prefs = getPrefs(context);
        return prefs.edit();
    }

    public static void setStringToPrefs(Context context, String key, String value) {
        getPrefsEditor(context).putString(key, value).commit();
    }

    public static String getStringFromPrefs(Context context, String key) {
        return getPrefs(context).getString(key, "");
    }
}
