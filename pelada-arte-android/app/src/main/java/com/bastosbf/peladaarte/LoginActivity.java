package com.bastosbf.peladaarte;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.bastosbf.peladaarte.model.Pelada;
import com.bastosbf.peladaarte.model.Player;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private PlayerLoginTask playerLoginTask;

    private EditText txtEmail;
    private EditText txtPassword;
    private CheckBox cbSave;
    private Button btnSignIn;
    private TextView lnkRegister;
    private View progress;
    private View form;

    private String rootURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        try {
            InputStream is = getBaseContext().getAssets().open("peladaarte.properties");
            Properties properties = new Properties();
            properties.load(is);
            rootURL = properties.getProperty("root.url");
        } catch (IOException e) {
            rootURL = "http://peladaarte.bastosbf.com";
        }
        txtEmail = (EditText) findViewById(R.id.txt_email);
        txtPassword = (EditText) findViewById(R.id.txt_password);
        cbSave = (CheckBox) findViewById(R.id.cb_save);
        btnSignIn = (Button) findViewById(R.id.btn_sign_in);
        btnSignIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                txtEmail.setError(null);
                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();
                if (TextUtils.isEmpty(email) || !isEmailValid(email)) {
                    txtEmail.setError("E-mail inválido!");
                } else {
                    login(email, password);
                }
            }
        });

        lnkRegister = (TextView) findViewById(R.id.lnk_register);
        lnkRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("pelada-arte-user", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        String password = sharedPreferences.getString("password", "");

        txtEmail.setText(email);
        txtPassword.setText(password);

        if (!email.isEmpty() && !password.isEmpty()) {
            cbSave.setChecked(true);
        }

        progress = findViewById(R.id.progress);
        form = findViewById(R.id.form);
    }

    private void register() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private void login(String email, String password) {
        showProgress(true);
        playerLoginTask = new PlayerLoginTask(email, password);
        playerLoginTask.execute((Void) null);
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean saveLoginData(String email, String password) {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("pelada-arte-user", Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.putString("email", email);
        sharedPreferencesEditor.putString("password", password);
        return sharedPreferencesEditor.commit();
    }

    private boolean deleteLoginData() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("pelada-arte-user", Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.putString("email", "");
        sharedPreferencesEditor.putString("password", "");
        return sharedPreferencesEditor.commit();
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            form.setVisibility(show ? View.GONE : View.VISIBLE);
            form.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    form.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progress.setVisibility(show ? View.VISIBLE : View.GONE);
            progress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progress.setVisibility(show ? View.VISIBLE : View.GONE);
            form.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public class PlayerLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String email;
        private final String password;
        private Player player;
        private ArrayList<Pelada> peladas;

        PlayerLoginTask(String email, String password) {
            this.email = email;
            this.password = password;
            peladas = new ArrayList<>();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String md5Pass = new String(Hex.encodeHex(DigestUtils.md5(password)));
            final String loginURL = rootURL + "/pelada-arte-server/rest/player/login?email=" + email + "&password=" + md5Pass;
            RestTemplate loginRESTTemplate = new RestTemplate();
            loginRESTTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            player = loginRESTTemplate.getForObject(loginURL, Player.class);
            if (player != null) {
                final String peladasURL = rootURL + "/pelada-arte-server/rest/pelada/list?player=" + player.getEmail();
                RestTemplate peladaRESTTemplate = new RestTemplate();
                peladaRESTTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Pelada[] p = peladaRESTTemplate.getForObject(peladasURL, Pelada[].class);
                List<Pelada> l = Arrays.asList(p);
                peladas.addAll(l);
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            showProgress(false);

            if (success) {
                if (cbSave.isChecked()) {
                    saveLoginData(email, password);
                } else {
                    deleteLoginData();
                }
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                i.putExtra("player", player);
                i.putExtra("peladas", peladas);
                startActivity(i);
            } else {
                txtEmail.setError(getString(R.string.error_incorrect_password));
                txtEmail.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
        }

    }
}

