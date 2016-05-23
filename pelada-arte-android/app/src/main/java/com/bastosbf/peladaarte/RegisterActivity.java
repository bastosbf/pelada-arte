package com.bastosbf.peladaarte;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class RegisterActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private PlayerRegisterTask registerPlayerTask;

    private View progress;
    private View form;

    private EditText txtEmail;
    private EditText txtPassword;
    private EditText txtRePassword;
    private EditText txtName;
    private Button btnRegister;

    private String rootURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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
        txtRePassword = (EditText) findViewById(R.id.txt_re_password);
        txtName = (EditText) findViewById(R.id.txt_name);
        btnRegister = (Button) findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtEmail.setError(null);
                txtRePassword.setError(null);
                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();
                String rePassword = txtRePassword.getText().toString();
                String name = txtName.getText().toString();
                if (TextUtils.isEmpty(email) || !isEmailValid(email)) {
                    txtEmail.setError("E-mail inválido!");
                } else {
                    if (TextUtils.isEmpty(password) || !password.equals(rePassword)) {
                        txtRePassword.setError("Senhas e confirmação de senha estão diferentes ou vazias!");
                    } else {
                        registerUser(email, password, name);
                    }
                }
            }
        });

        progress = findViewById(R.id.progress);
        form = findViewById(R.id.form);
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private void registerUser(String email, String password, String name) {
        showProgress(true);
        registerPlayerTask = new PlayerRegisterTask(email, password, name);
        registerPlayerTask.execute((Void) null);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
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

    public class PlayerRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String email;
        private final String password;
        private final String name;

        PlayerRegisterTask(String email, String password, String name) {
            this.email = email;
            this.password = password;
            this.name = name;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String md5Pass = new String(Hex.encodeHex(DigestUtils.md5(password)));
            final String registerURL = rootURL + "/pelada-arte-server/rest/player/add?email=" + email + "&password=" + md5Pass + "&name=" + name;
            RestTemplate loginRESTTemplate = new RestTemplate();
            loginRESTTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            String result = loginRESTTemplate.getForObject(registerURL, String.class);
            return Boolean.valueOf(result);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            showProgress(false);

            if (success) {
                Toast.makeText(getApplicationContext(), "Usuário cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            } else {
                txtEmail.setError("Usuário inválido ou já cadastrado!");
            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
        }

    }
}
