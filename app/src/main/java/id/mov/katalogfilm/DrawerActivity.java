package id.mov.katalogfilm;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.mov.katalogfilm.adapter.MovieAdapter;
import id.mov.katalogfilm.model.Movie;
import id.mov.katalogfilm.remote.MovieService;
import id.mov.katalogfilm.remote.RetrofitClient;
import id.mov.katalogfilm.response.MovieResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.ed_search)
    EditText edSearch;
    @BindView(R.id.btn_search)
    Button btnSearch;
    @BindView(R.id.listMovies)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    List<Movie> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        clickSearch();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_now_playing) {
            // Handle the camera action
            this.setTitle(this.getResources().getString(R.string.NOW_PLAYING));
            btnSearch.setVisibility(INVISIBLE);
            edSearch.setVisibility(INVISIBLE);
            callNowPlayingMovies();
        } else if (id == R.id.nav_upcoming) {
            this.setTitle(this.getResources().getString(R.string.UPCOMING));
            btnSearch.setVisibility(INVISIBLE);
            edSearch.setVisibility(INVISIBLE);
            callUpcomingMovies();
        } else if (id == R.id.nav_search)   {
            recyclerView.setAdapter(null);
            this.setTitle(this.getResources().getString(R.string.SEARCH));
            btnSearch.setVisibility(VISIBLE);
            edSearch.setVisibility(VISIBLE);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void clickSearch() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edSearch.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Silahkan masukan kata kunci !", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Silahkan tunggu . . .", Toast.LENGTH_LONG).show();
                    recyclerView.setAdapter(null);
                    Retrofit retrofit = RetrofitClient.getClient();
                    MovieService movieService = retrofit.create(MovieService.class);

                    Call<MovieResponse> call = movieService.searchMovie(
                            "0553ea4b7169cee1993e121828c11830",
                            "en-US",
                            edSearch.getText().toString()
                    );
                    call.enqueue(new Callback<MovieResponse>() {
                        @Override
                        public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                            list = response.body().getResults();
                            MovieAdapter movieAdapter = new MovieAdapter(DrawerActivity.this, list);
                            recyclerView.setAdapter(movieAdapter);
                        }

                        @Override
                        public void onFailure(Call<MovieResponse> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    private void callNowPlayingMovies() {
        recyclerView.setAdapter(null);
        Retrofit retrofit = RetrofitClient.getClient();
        MovieService movieService = retrofit.create(MovieService.class);

        Call<MovieResponse> call = movieService.nowPlayingMovies(
                "0553ea4b7169cee1993e121828c11830",
                "en-US"
        );
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                list = response.body().getResults();
                MovieAdapter movieAdapter = new MovieAdapter(DrawerActivity.this, list);
                recyclerView.setAdapter(movieAdapter);
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void callUpcomingMovies() {
        recyclerView.setAdapter(null);
        Retrofit retrofit = RetrofitClient.getClient();
        MovieService movieService = retrofit.create(MovieService.class);

        Call<MovieResponse> call = movieService.upcomingMovies(
                "0553ea4b7169cee1993e121828c11830",
                "en-US"
        );
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                list = response.body().getResults();
                MovieAdapter movieAdapter = new MovieAdapter(DrawerActivity.this, list);
                recyclerView.setAdapter(movieAdapter);
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
