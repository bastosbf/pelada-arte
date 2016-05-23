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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.bastosbf.peladaarte.model.Pelada;
import com.bastosbf.peladaarte.model.Player;
import com.bastosbf.peladaarte.utils.PeladaArteUtils;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private PlayersListTask playersListTask;
    private Player player;
    private ArrayList<Pelada> peladas;

    private Button btnAddPelada;
    private ListView lstPeladas;
    private View progress;
    private View form;

    private String rootURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            InputStream is = getBaseContext().getAssets().open("peladaarte.properties");
            Properties properties = new Properties();
            properties.load(is);
            rootURL = properties.getProperty("root.url");
        } catch (IOException e) {
            rootURL = "http://peladaarte.bastosbf.com";
        }
        Intent intent = getIntent();
        configure(intent);
    }

    private void configure(Intent intent) {
        lstPeladas = (ListView) findViewById(R.id.lst_peladas);
        player = (Player) intent.getSerializableExtra("player");
        peladas = (ArrayList<Pelada>) intent.getSerializableExtra("peladas");

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (Pelada pelada : peladas) {
            Map<String, Object> element = new HashMap<>();
            element.put("pelada", pelada);
            element.put("name", pelada.getName());
            element.put("info", PeladaArteUtils.getDayOfTheWeek(pelada.getDay()) + " - " + pelada.getTime());

            list.add(element);
        }

        BaseAdapter adapter = new SimpleAdapter(this, list,
                android.R.layout.simple_list_item_2,
                new String[]{"name", "info"},
                new int[]{android.R.id.text1, android.R.id.text2}) {
        };

        lstPeladas.setAdapter(adapter);
        lstPeladas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> item = (Map<String, Object>) lstPeladas.getItemAtPosition(position);
                Pelada pelada = (Pelada) item.get("pelada");
                view(pelada);
            }
        });


        btnAddPelada = (Button) findViewById(R.id.btn_add_pelada);
        btnAddPelada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddPeladaActivity.class);
                intent.putExtra("player", player);
                intent.putExtra("peladas", peladas);

                startActivityForResult(intent, PeladaArteUtils.ADD_PELADA_RESULT);
            }
        });

        progress = findViewById(R.id.progress);
        form = findViewById(R.id.form);
    }

    private void view(Pelada pelada) {
        showProgress(true);
        playersListTask = new PlayersListTask(pelada);
        playersListTask.execute((Void) null);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == PeladaArteUtils.ADD_PELADA_RESULT) {
            configure(data);
        }
    }

    public class PlayersListTask extends AsyncTask<Void, Void, Boolean> {

        private final Pelada pelada;
        private ArrayList<Player> players;

        PlayersListTask(Pelada pelada) {
            this.pelada = pelada;
            players = new ArrayList<>();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            final String listPlayersURL = rootURL + "/pelada-arte-server/rest/pelada/list-players?pelada=" + pelada.getId();
            RestTemplate loginRESTTemplate = new RestTemplate();
            loginRESTTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            Player[] p = loginRESTTemplate.getForObject(listPlayersURL, Player[].class);
            List<Player> l = Arrays.asList(p);
            players.addAll(l);
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            showProgress(false);

            if (success) {
                Intent intent = new Intent(MainActivity.this, PeladaActivity.class);
                intent.putExtra("pelada", pelada);
                intent.putExtra("player", player);
                intent.putExtra("peladas", peladas);
                intent.putExtra("players", players);
                startActivity(intent);
            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
        }

    }

}
