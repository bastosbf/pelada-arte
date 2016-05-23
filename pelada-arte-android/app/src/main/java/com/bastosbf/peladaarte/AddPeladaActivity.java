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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class AddPeladaActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private PeladaAddTask peladaAddTask;
    private Player player;
    private ArrayList<Pelada> peladas;

    private EditText txtName;
    private Spinner chDay;
    private EditText txtTime;
    private Button btnAdd;

    private View progress;
    private View form;

    private String rootURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pelada);
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
        peladas = (ArrayList<Pelada>) intent.getSerializableExtra("peladas");

        txtName = (EditText) findViewById(R.id.txt_name);
        chDay = (Spinner) findViewById(R.id.ch_day);
        txtTime = (EditText) findViewById(R.id.txt_time);
        btnAdd = (Button) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtName.setError(null);
                int day = PeladaArteUtils.getDayOfTheWeek(String.valueOf(chDay.getSelectedItem()));
                addPelada(player.getEmail(), txtName.getText().toString(), day, txtTime.getText().toString());
            }
        });

        progress = findViewById(R.id.progress);
        form = findViewById(R.id.form);
    }

    private void addPelada(String owner, String name, Integer day, String time) {
        showProgress(true);
        peladaAddTask = new PeladaAddTask(owner, name, day, time);
        peladaAddTask.execute((Void) null);
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

    public class PeladaAddTask extends AsyncTask<Void, Void, Boolean> {

        private final String owner;
        private final String name;
        private final Integer day;
        private final String time;

        PeladaAddTask(String owner, String name, Integer day, String time) {
            this.owner = owner;
            this.name = name;
            this.day = day;
            this.time = time;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            final String addPeladaURL = rootURL + "/pelada-arte-server/rest/pelada/add?owner=" + owner + "&name=" + name + "&day=" + day + "&time=" + time;
            RestTemplate addPeladaRESTTemplate = new RestTemplate();
            addPeladaRESTTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            String result = addPeladaRESTTemplate.getForObject(addPeladaURL, String.class);

            final String peladasURL = rootURL + "/pelada-arte-server/rest/pelada/list?player=" + owner;
            RestTemplate peladaRESTTemplate = new RestTemplate();
            peladaRESTTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            Pelada[] p = peladaRESTTemplate.getForObject(peladasURL, Pelada[].class);
            List<Pelada> l = Arrays.asList(p);
            peladas.clear();
            peladas.addAll(l);
            return Boolean.valueOf(result);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            showProgress(false);
            if (success) {
                Toast.makeText(getApplicationContext(), "Pelada cadastrada com sucesso!", Toast.LENGTH_LONG).show();
                Intent intent = getIntent();
                intent.putExtra("player", player);
                intent.putExtra("peladas", peladas);
                setResult(PeladaArteUtils.ADD_PELADA_RESULT, intent);
                finish();
            } else {
                txtName.setError("Pelada já cadastrada!");
            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
        }
    }
}
