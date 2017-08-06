package app.mannit.nitin.com.popularmoviesapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

import app.mannit.nitin.com.popularmoviesapp.R;
import app.mannit.nitin.com.popularmoviesapp.activities.DetailsActivity;
import app.mannit.nitin.com.popularmoviesapp.models.Result;
import app.mannit.nitin.com.popularmoviesapp.util.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nitingeetasagardasari on 8/5/17 for the project PopularMoviesApp.
 */

public class MainMovieAdapter extends RecyclerView.Adapter<MainMovieAdapter.PosterViewHolder> {

    private ArrayList<Result> mMoviesList = new ArrayList<>();
    private Context mContext;

    public MainMovieAdapter(ArrayList<Result> moviesList, Context context) {
        mMoviesList = moviesList;
        mContext = context;
    }

    public MainMovieAdapter(Context context) {
        mContext = context;
    }

    @Override
    public PosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.poster_card, parent, false);
        return new PosterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PosterViewHolder holder, int position) {
        String moviePoster = Constants.IMAGE_URL + mMoviesList.get(position).getPosterPath();
        holder.setMoviePoster(moviePoster);
    }

    @Override
    public int getItemCount() {
        return mMoviesList.size() > 0 ? mMoviesList.size() : 0;
    }

    public void addList(ArrayList<Result> moviesList) {
        mMoviesList.addAll(moviesList);
        notifyDataSetChanged();
    }

    public void clear() {
        int size = this.mMoviesList.size();
        this.mMoviesList.clear();
        notifyItemRangeRemoved(0, size);
    }

    class PosterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.poster)
        ImageView moviePoster;

        PosterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setMoviePoster(String image) {
            Picasso.with(mContext)
                    .load(image)
                    .placeholder(R.drawable.poster)
                    .error(R.drawable.poster)
                    .fit()
                    .into(moviePoster);
        }

        @OnClick(R.id.poster)
        void toDetails() {
            Intent intent = new Intent(mContext, DetailsActivity.class);
            intent.putExtra(Constants.MOVIE_DATA, Parcels.wrap(mMoviesList.get(getAdapterPosition())));
            mContext.startActivity(intent);
        }
    }
}
