package id.mov.katalogfilm.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import id.mov.katalogfilm.DetailMovieActivity;
import id.mov.katalogfilm.R;
import id.mov.katalogfilm.model.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private Context mContext;
    private List<Movie> moviesList;
    private SimpleDateFormat formatToString = new SimpleDateFormat("E, MMM dd, yyyy");
    private SimpleDateFormat formatToDate = new SimpleDateFormat("yyyy-mm-dd");

    public MovieAdapter(Context context, List<Movie> list) {
        mContext = context;
        moviesList = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemRow = LayoutInflater.from(mContext).inflate(R.layout.fragment_movie, parent, false);
        return new ViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Picasso.get()
                .load("http://image.tmdb.org/t/p/w92/" + moviesList.get(position).getPosterPath())
                .into(holder.ivPoster);

        holder.tvTitle.setText(moviesList.get(position).getTitle());
        if (moviesList.get(position).getOverview().length() < 25) {
            holder.tvOverview.setText(moviesList.get(position).getOverview());
        } else {
            holder.tvOverview.setText(moviesList.get(position).getOverview().substring(0, 25) + "...");
        }
        try {
            Date date = formatToDate.parse(moviesList.get(position).getReleaseDate());
            holder.tvReleaseDate.setText(formatToString.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("ID", String.valueOf(moviesList.get(position).getId()));
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(DetailMovieActivity.MOVIE_ITEM, moviesList.get(position));
                Intent intent = new Intent(mContext, DetailMovieActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
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

}
