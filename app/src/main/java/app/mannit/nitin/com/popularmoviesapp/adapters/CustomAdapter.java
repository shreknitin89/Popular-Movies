package app.mannit.nitin.com.popularmoviesapp.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.mannit.nitin.com.popularmoviesapp.R;
import app.mannit.nitin.com.popularmoviesapp.models.Result;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nitingeetasagardasari on 9/16/17 for the project PopularMoviesApp.
 */

public class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TRAILER = 1;
    public static final int REVIEW = 2;
    public static final int HEADER = 3;

    private Context mContext;
    private ArrayList<Result> mTrailers;
    private ArrayList<Result> mReviews;

    public CustomAdapter(Context context, ArrayList<Result> trailers, ArrayList<Result> reviews) {
        mContext = context;
        mTrailers = trailers;
        mReviews = reviews;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        switch (viewType) {
            case HEADER:
                View header = inflater.inflate(R.layout.header_layout, parent, false);
                return new HeaderViewHolder(header);
            case TRAILER:
                View trailer = inflater.inflate(R.layout.trailer_content, parent, false);
                return new TrailerViewHolder(trailer);
            case REVIEW:
            default:
                View review = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                return new ReviewViewHolder(review);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case HEADER:
                if (position == 0)
                    ((HeaderViewHolder) holder).headline.setText(mContext.getResources().getString(R.string.trailers));
                else
                    ((HeaderViewHolder) holder).headline.setText(mContext.getResources().getString(R.string.reviews));
                break;
            case TRAILER:
                ((TrailerViewHolder) holder).playButton.setColorFilter(ContextCompat.getColor(mContext, android.R.color.black));
                ((TrailerViewHolder) holder).headline.setText(String.format(mContext.getString(R.string.trailer_heading), position));
                break;
            case REVIEW:
            default:
                int tempPosition = position - 1 - mTrailers.size();
                ((ReviewViewHolder) holder).review.setText(mReviews.get(tempPosition).getContent());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mTrailers.size() + mReviews.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == mTrailers.size())
            return HEADER;
        else if (position < mTrailers.size())
            return TRAILER;
        else return REVIEW;
    }


    class HeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView headline;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.trailer_heading)
        TextView headline;
        @BindView(R.id.play_button)
        ImageView playButton;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.trailer_container)
        void openTrailer() {
            String id = mTrailers.get(getAdapterPosition() - 1).getId();
            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + id));
            try {
                mContext.startActivity(appIntent);
            } catch (ActivityNotFoundException ex) {
                mContext.startActivity(webIntent);
            }
        }
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {

        @BindView(android.R.id.text1)
        TextView review;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
