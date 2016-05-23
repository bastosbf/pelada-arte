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
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bastosbf.peladaarte.model.Pelada;
import com.bastosbf.peladaarte.model.Player;
import com.bastosbf.peladaarte.model.Rate;
import com.bastosbf.peladaarte.utils.PeladaArteUtils;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class PeladaActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private PlayersRateTask playersRateTask;
    private ViewPreviousStatisticsTask viewPreviousStatisticsTask;
    private ViewGlobalStatisticsTask viewGlobalStatisticsTask;
    private Pelada pelada;
    private Player player;
    private ArrayList<Player> players;
    private Map<Player, Float> sumOfRates = new HashMap<>();
    private Map<Player, Integer> numberOfRates = new HashMap<>();
    private Button btnAddPlayer;
    private Button btnViewPelada;
    private Button btnViewPreviousStatistics;
    private Button btnViewGlobalStatistics;
    private TextView lblPelada;
    private TextView lblDayTime;
    private View progress;
    private View form;
    private String rootURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelada);
        try {
            InputStream is = getBaseContext().getAssets().open("peladaarte.properties");
            Properties properties = new Properties();
            properties.load(is);
            rootURL = properties.getProperty("root.url");
        } catch (IOException e) {
            rootURL = "http://peladaarte.bastosbf.com";
        }

        Intent intent = getIntent();
        configure(intent, Type.MAIN);

    }

    private void configure(Intent intent, Type type) {
        pelada = (Pelada) intent.getSerializableExtra("pelada");
        player = (Player) intent.getSerializableExtra("player");
        players = (ArrayList<Player>) intent.getSerializableExtra("players");


        btnAddPlayer = (Button) findViewById(R.id.btn_add_player);
        btnAddPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PeladaActivity.this, AddPlayerActivity.class);
                intent.putExtra("player", player);
                intent.putExtra("players", players);
                intent.putExtra("pelada", pelada);

                startActivityForResult(intent, PeladaArteUtils.ADD_PLAYER_RESULT);
            }
        });

        btnViewPelada = (Button) findViewById(R.id.btn_view_pelada);
        btnViewPelada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPelada();
            }
        });

        btnViewPreviousStatistics = (Button) findViewById(R.id.btn_view_previous_statistics);
        btnViewPreviousStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPreviousStatistics(pelada.getId());
            }
        });
        btnViewGlobalStatistics = (Button) findViewById(R.id.btn_view_global_statistics);
        btnViewGlobalStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewGlobalStatistics(pelada.getId());
            }
        });

        lblPelada = (TextView) findViewById(R.id.lbl_pelada);
        lblDayTime = (TextView) findViewById(R.id.lbl_day_time);

        LinearLayout ratingContainer = (LinearLayout) findViewById(R.id.rating_container);
        ratingContainer.removeAllViews();

        if (type == Type.MAIN) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            if (day == pelada.getDay()) {
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                calendar.setTime(PeladaArteUtils.getDate(pelada.getTime()));
                int peladaHour = calendar.get(Calendar.HOUR_OF_DAY);
                int peladaMinute = calendar.get(Calendar.MINUTE);

                if (hour > peladaHour || (hour == peladaHour && minute >= peladaMinute)) {
                    int count = 100;
                    for (Player p : players) {
                        if (!p.getEmail().equals(player.getEmail())) {
                            TextView lblRateName = new TextView(this);
                            lblRateName.setId(count++);
                            lblRateName.setText(p.getName());
                            lblRateName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

                            ratingContainer.addView(lblRateName);

                            TextView lblRateEmail = new TextView(this);
                            lblRateEmail.setId(count++);
                            lblRateEmail.setText(p.getEmail());

                            ratingContainer.addView(lblRateEmail);

                            RatingBar rating = new RatingBar(this);
                            ViewGroup.LayoutParams layout = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            rating.setId(count++);
                            rating.setNumStars(5);
                            rating.setMax(5);

                            rating.setLayoutParams(layout);
                            rating.setStepSize(1);

                            ratingContainer.addView(rating);
                        }
                    }
                    Button btnRate = new Button(this);
                    ViewGroup.LayoutParams layout = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    btnRate.setId(count++);
                    btnRate.setText("Enviar");
                    btnRate.setLayoutParams(layout);
                    btnRate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Map<String, Float> rates = new HashMap<String, Float>();
                            int start = 100;
                            TextView lblRateName = (TextView) findViewById(start++);
                            while (!(lblRateName instanceof Button)) {
                                TextView lblRateEmail = (TextView) findViewById(start++);
                                RatingBar rating = (RatingBar) findViewById(start++);
                                rates.put(String.valueOf(lblRateEmail.getText()), rating.getRating());
                                lblRateName = (TextView) findViewById(start++);
                            }
                            rate(player.getEmail(), pelada.getId(), rates);
                        }
                    });
                    ratingContainer.addView(btnRate);
                } else {
                    int id = 100;
                    TextView lblMsg = new TextView(this);
                    lblMsg.setId(id++);
                    lblMsg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    lblMsg.setGravity(Gravity.CENTER);
                    lblMsg.setText("Não fure a pelada hoje!");

                    ratingContainer.addView(lblMsg);
                    TextView lblSubMsg = new TextView(this);
                    lblSubMsg.setId(id++);
                    lblSubMsg.setGravity(Gravity.CENTER);
                    lblSubMsg.setText("Você poderá qualificar seus amigos assim que ela iniciar!");
                    ratingContainer.addView(lblSubMsg);

                }
            } else {
                int id = 100;
                TextView lblMsg = new TextView(this);
                lblMsg.setId(id++);
                lblMsg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                lblMsg.setGravity(Gravity.CENTER);
                lblMsg.setText("Você não tem pelada hoje!");

                ratingContainer.addView(lblMsg);
            }
        } else if (type == Type.PREVIOUS || type == Type.GLOBAL) {
            int count = 100;
            for (Player p : players) {
                float r = 0;
                Float sum = sumOfRates.get(p);
                Integer number = numberOfRates.get(p);
                if (sum != null && number != null) {
                    r = sum / number;
                }

                TextView lblRateName = new TextView(this);
                lblRateName.setId(count++);
                lblRateName.setText(p.getName());
                lblRateName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

                ratingContainer.addView(lblRateName);

                TextView lblRateEmail = new TextView(this);
                lblRateEmail.setId(count++);
                lblRateEmail.setText(p.getEmail());

                ratingContainer.addView(lblRateEmail);

                RatingBar rating = new RatingBar(this);
                ViewGroup.LayoutParams layout = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                rating.setId(count++);
                rating.setLayoutParams(layout);
                rating.setStepSize(0.1f);
                rating.setNumStars(5);
                rating.setIsIndicator(true);
                rating.setRating(r);

                ratingContainer.addView(rating);
            }
        }

        lblPelada.setText(pelada.getName());
        lblDayTime.setText(PeladaArteUtils.getDayOfTheWeek(pelada.getDay()) + " - " + pelada.getTime());
        if (player.getEmail().equals(pelada.getOwner().getEmail())) {
            btnAddPlayer.setEnabled(true);
        }

        progress = findViewById(R.id.progress);
        form = findViewById(R.id.form);
    }

    private void viewPelada() {
        configure(getIntent(), Type.MAIN);
    }

    private void viewPreviousStatistics(int pelada) {
        showProgress(true);
        viewPreviousStatisticsTask = new ViewPreviousStatisticsTask(pelada);
        viewPreviousStatisticsTask.execute((Void) null);
    }

    private void viewGlobalStatistics(int pelada) {
        showProgress(true);
        viewGlobalStatisticsTask = new ViewGlobalStatisticsTask(pelada);
        viewGlobalStatisticsTask.execute((Void) null);
    }

    private void rate(String from, int pelada, Map<String, Float> rates) {
        showProgress(true);
        playersRateTask = new PlayersRateTask(from, pelada, rates);
        playersRateTask.execute((Void) null);
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
        if (resultCode == PeladaArteUtils.ADD_PLAYER_RESULT) {
            configure(data, Type.MAIN);
        }
    }

    public enum Type {
        MAIN, PREVIOUS, GLOBAL;
    }

    public class PlayersRateTask extends AsyncTask<Void, Void, Boolean> {

        private final String from;
        private final int pelada;
        private final Map<String, Float> rates;

        PlayersRateTask(String from, int pelada, Map<String, Float> rates) {
            this.from = from;
            this.pelada = pelada;
            this.rates = rates;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Iterator<String> iterator = rates.keySet().iterator();
            boolean result = false;
            while (iterator.hasNext()) {
                String to = iterator.next();
                float rate = rates.get(to);
                if (rate > 0) {
                    final String ratePlayerURL = rootURL + "/pelada-arte-server/rest/player/rate?from=" + from + "&to=" + to + "&pelada=" + pelada + "&rate=" + rate;
                    RestTemplate ratePlayerRESTTemplate = new RestTemplate();
                    ratePlayerRESTTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                    String r = ratePlayerRESTTemplate.getForObject(ratePlayerURL, String.class);
                    result = result || Boolean.valueOf(r);
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            showProgress(false);

            if (success) {
                Toast.makeText(getApplicationContext(), "Pontuações enviadas com sucess!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Você já enviou a pontuação para todos os jogadores selecionados!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
        }

    }

    public class ViewPreviousStatisticsTask extends AsyncTask<Void, Void, Boolean> {

        private final int pelada;
        private List<Rate> rates;

        ViewPreviousStatisticsTask(int pelada) {
            this.pelada = pelada;
            rates = new ArrayList<>();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            final String ratesURL = rootURL + "/pelada-arte-server/rest/rate/list-previous?pelada=" + pelada;
            RestTemplate ratesRESTTemplate = new RestTemplate();
            ratesRESTTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            Rate[] p = ratesRESTTemplate.getForObject(ratesURL, Rate[].class);
            List<Rate> l = Arrays.asList(p);
            rates.addAll(l);
            return !rates.isEmpty();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            showProgress(false);

            if (success) {
                sumOfRates.clear();
                numberOfRates.clear();
                for (Rate rate : rates) {
                    Player player = rate.getRate_to();
                    float r = rate.getRate();
                    Float sum = sumOfRates.get(player);
                    if (sum == null) {
                        sum = 0f;
                    }
                    sum += r;
                    Integer number = numberOfRates.get(player);
                    if (number == null) {
                        number = 0;
                    }
                    number++;
                    sumOfRates.put(player, sum);
                    numberOfRates.put(player, number);
                }
                configure(getIntent(), Type.PREVIOUS);
            } else {
                Toast.makeText(getApplicationContext(), "Ainda não existe nenhuma classificação para essa pelada!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
        }

    }

    public class ViewGlobalStatisticsTask extends AsyncTask<Void, Void, Boolean> {

        private final int pelada;
        private List<Rate> rates;

        ViewGlobalStatisticsTask(int pelada) {
            this.pelada = pelada;
            rates = new ArrayList<>();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            final String ratesURL = rootURL + "/pelada-arte-server/rest/rate/list-global?pelada=" + pelada;
            RestTemplate ratesRESTTemplate = new RestTemplate();
            ratesRESTTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            Rate[] p = ratesRESTTemplate.getForObject(ratesURL, Rate[].class);
            List<Rate> l = Arrays.asList(p);
            rates.addAll(l);
            return !rates.isEmpty();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            showProgress(false);

            if (success) {
                sumOfRates.clear();
                numberOfRates.clear();
                for (Rate rate : rates) {
                    Player player = rate.getRate_to();
                    float r = rate.getRate();
                    Float sum = sumOfRates.get(player);
                    if (sum == null) {
                        sum = 0f;
                    }
                    sum += r;
                    Integer number = numberOfRates.get(player);
                    if (number == null) {
                        number = 0;
                    }
                    number++;
                    sumOfRates.put(player, sum);
                    numberOfRates.put(player, number);
                }
                configure(getIntent(), Type.GLOBAL);
            } else {
                Toast.makeText(getApplicationContext(), "Ainda não existe nenhuma classificação para essa pelada!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
        }

    }
}
