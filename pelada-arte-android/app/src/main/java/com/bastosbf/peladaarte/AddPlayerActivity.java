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

import com.bastosbf.peladaarte.model.Pelada;
import com.bastosbf.peladaarte.model.Player;
import com.bastosbf.peladaarte.utils.PeladaArteUtils;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class AddPlayerActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private ArrayList<Player> players;
    private PlayerAddTask playerAddTask;
    private Player player;
    private Pelada pelada;
    private EditText txtEmail;
    private Button btnAdd;
    private View progress;
    private View form;

    private String rootURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);
        try {
            InputStream is = getBaseContext().getAssets().open("peladaarte.properties");
            Properties properties = new Properties();
            properties.load(is);
            rootURL = properties.getProperty("root.url");
        } catch (IOException e) {
            rootURL = "http://peladaarte.bastosbf.com";
        }

        Intent intent = getIntent();
        player = (Player) intent.getSerializableExtra("player");
        pelada = (Pelada) intent.getSerializableExtra("pelada");
        players = (ArrayList<Player>) intent.getSerializableExtra("players");

        txtEmail = (EditText) findViewById(R.id.txt_email);
        btnAdd = (Button) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtEmail.setError(null);
                String email = txtEmail.getText().toString();
                if (TextUtils.isEmpty(email) || !isEmailValid(email)) {
                    txtEmail.setError("E-mail inválido!");
                } else {
                    addPlayer(player.getEmail(), pelada.getId(), email);
                }
            }
        });
        progress = findViewById(R.id.progress);
        form = findViewById(R.id.form);
    }

    private void addPlayer(String owner, Integer pelada, String player) {
        showProgress(true);
        playerAddTask = new PlayerAddTask(owner, pelada, player);
        playerAddTask.execute((Void) null);
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
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

    public class PlayerAddTask extends AsyncTask<Void, Void, Boolean> {

        private final String owner;
        private final Integer pelada;
        private final String player;

        PlayerAddTask(String owner, Integer pelada, String player) {
            this.owner = owner;
            this.pelada = pelada;
            this.player = player;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            final String registerURL = rootURL + "/pelada-arte-server/rest/pelada/add-player?owner=" + owner + "&pelada=" + pelada + "&player=" + player;
            RestTemplate addPlayerRESTTemplate = new RestTemplate();
            addPlayerRESTTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            String result = addPlayerRESTTemplate.getForObject(registerURL, String.class);

            final String listPlayersURL = rootURL + "/pelada-arte-server/rest/pelada/list-players?pelada=" + pelada;
            RestTemplate loginRESTTemplate = new RestTemplate();
            loginRESTTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            Player[] p = loginRESTTemplate.getForObject(listPlayersURL, Player[].class);
            List<Player> l = Arrays.asList(p);
            players.clear();
            players.addAll(l);
            return Boolean.valueOf(result);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            showProgress(false);

            if (success) {
                Toast.makeText(getApplicationContext(), "Jogador cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                Intent intent = getIntent();
                intent.putExtra("pelada", pelada);
                intent.putExtra("player", player);
                intent.putExtra("players", players);
                setResult(PeladaArteUtils.ADD_PLAYER_RESULT, intent);
                finish();
            } else {
                txtEmail.setError("Jodador não existe ou já cadastrado!");
                txtEmail.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
        }
    }
}
