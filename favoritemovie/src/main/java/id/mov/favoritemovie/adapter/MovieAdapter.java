package id.mov.favoritemovie.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import id.mov.favoritemovie.DetailMovieActivity;
import id.mov.favoritemovie.R;
import id.mov.favoritemovie.model.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private Context mContext;
    private Cursor mCursor;
    private SimpleDateFormat formatToString = new SimpleDateFormat("E, MMM dd, yyyy");
    private SimpleDateFormat formatToDate = new SimpleDateFormat("yyyy-mm-dd");

    public MovieAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemRow = LayoutInflater.from(mContext).inflate(R.layout.fragment_movie, parent, false);
        return new ViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Movie movie = new Movie();
        if (mCursor != null)  {
            movie = getItem(position);
        }

        Picasso.get()
                .load("http://image.tmdb.org/t/p/w92/" + movie.getPosterPath())
                .into(holder.ivPoster);

        holder.tvTitle.setText(movie.getTitle());

        if (movie.getOverview().length() < 25) {
            holder.tvOverview.setText(movie.getOverview());
        } else {
            holder.tvOverview.setText(movie.getOverview().substring(0, 25) + "...");
        }
        try {
            Date date = formatToDate.parse(movie.getReleaseDate());
            holder.tvReleaseDate.setText(formatToString.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final Movie finalMovie = movie;
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(DetailMovieActivity.MOVIE_ITEM, finalMovie);
                Intent intent = new Intent(mContext, DetailMovieActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPoster;
        TextView tvTitle;
        TextView tvOverview;
        TextView tvReleaseDate;
        ConstraintLayout container;

        public ViewHolder(View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.poster);
            tvTitle = itemView.findViewById(R.id.title);
            tvOverview = itemView.findViewById(R.id.overview);
            tvReleaseDate = itemView.findViewById(R.id.release_date);
            container = itemView.findViewById(R.id.container_parent);
        }
    }

    private Movie getItem(int position) {
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("Position invalid");
        }
        return new Movie(mCursor);
    }

    public void removeAllItem() {
        mCursor = null;
        notifyDataSetChanged();
    }
}
