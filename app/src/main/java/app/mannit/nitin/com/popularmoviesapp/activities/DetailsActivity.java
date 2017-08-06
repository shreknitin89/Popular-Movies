package app.mannit.nitin.com.popularmoviesapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import app.mannit.nitin.com.popularmoviesapp.R;
import app.mannit.nitin.com.popularmoviesapp.models.Result;
import app.mannit.nitin.com.popularmoviesapp.util.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends PopularMoviesActivity {

    @BindView(R.id.movie_title)
    TextView title;
    @BindView(R.id.poster_thumbnail)
    ImageView poster;
    @BindView(R.id.release)
    TextView releasedYear;
    @BindView(R.id.duration)
    TextView movieDuration;
    @BindView(R.id.rating)
    TextView userRating;
    @BindView(R.id.description)
    TextView synopsis;

    private Result mMovieData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mUnBinder = ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            mMovieData = Parcels.unwrap(intent.getParcelableExtra(Constants.MOVIE_DATA));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMovieData != null) {
            title.setText(mMovieData.getOriginalTitle());
            releasedYear.setText(mMovieData.getReleaseDate());
            userRating.setText(String.valueOf(mMovieData.getVoteAverage()));
            synopsis.setText(mMovieData.getOverview());
            Picasso.with(this)
                    .load(Constants.IMAGE_URL + mMovieData.getPosterPath())
                    .placeholder(R.drawable.poster)
                    .error(R.drawable.poster)
                    .fit()
                    .into(poster);
        }
    }
}
