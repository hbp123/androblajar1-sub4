package id.mov.favoritemovie;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.mov.favoritemovie.model.Movie;

public class DetailMovieActivity extends AppCompatActivity {
    public static final String MOVIE_ITEM = "movie";
    private Movie movie;
    @BindView(R.id.image_poster_detail)
    ImageView ivPoster;
    @BindView(R.id.title_detail)
    TextView tvTitle;
    @BindView(R.id.overview_detail)
    TextView tvOverview;
    @BindView(R.id.release_detail)
    TextView tvReleasedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        getBundle();
        ButterKnife.bind(this);

        setValuesMovieItem();
    }

    private void getBundle() {
        Bundle bundle = getIntent().getExtras();
        movie = bundle.getParcelable(MOVIE_ITEM);
        Log.d("Movie Data ", movie.toString());
    }

    private void setValuesMovieItem() {
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
