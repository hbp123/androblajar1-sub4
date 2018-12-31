package id.mov.katalogfilm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.mov.katalogfilm.db.FavoriteHelper;
import id.mov.katalogfilm.model.Movie;

public class DetailMovieActivity extends AppCompatActivity {
    public static final String MOVIE_ITEM = "movie";
    private Movie movie;
    private FavoriteHelper favoriteHelper;
    @BindView(R.id.image_poster_detail)
    ImageView ivPoster;
    @BindView(R.id.title_detail)
    TextView tvTitle;
    @BindView(R.id.overview_detail)
    TextView tvOverview;
    @BindView(R.id.release_detail)
    TextView tvReleasedDate;
    @BindView(R.id.ibFav)
    ImageButton ibFav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        favoriteHelper = new FavoriteHelper(getApplicationContext());
        favoriteHelper.open();

        getBundle();
        ButterKnife.bind(this);

        setValuesMovieItem();
        onStarClick();
    }

    private void onStarClick() {
        ibFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (favoriteHelper.checkid(String.valueOf(movie.getId()))) {
                    favoriteHelper.delete(movie.getId());
                    ibFav.setImageResource(R.drawable.ic_star_border);
                } else {
                    favoriteHelper.insert(movie);
                    ibFav.setImageResource(R.drawable.ic_star);
                }
            }
        });
    }

    private void getBundle() {
        Bundle bundle = getIntent().getExtras();
        movie = bundle.getParcelable(MOVIE_ITEM);
        Log.d("Movie Data ", movie.toString());
    }

    private void setValuesMovieItem() {
        if (favoriteHelper.checkid(String.valueOf(movie.getId()))) {
            ibFav.setImageResource(R.drawable.ic_star);
        } else {
            ibFav.setImageResource(R.drawable.ic_star_border);
        }
        Picasso.get()
                .load("http://image.tmdb.org/t/p/w185/" + movie.getPosterPath())
                .into(ivPoster);
        tvTitle.setText(movie.getTitle() + "(" + movie.getOriginalLanguage() + ")");
        try {
            SimpleDateFormat formatToString = new SimpleDateFormat("E, MMM dd, yyyy");
            SimpleDateFormat formatToDate = new SimpleDateFormat("yyyy-mm-dd");
            Date date = formatToDate.parse(movie.getReleaseDate());
            tvReleasedDate.setText(formatToString.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tvOverview.setText(movie.getOverview());
    }
}
