package comandre_fernando.httpsgithub.flixdb.components.adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import comandre_fernando.httpsgithub.flixdb.data_objects.MoviesObject;
import comandre_fernando.httpsgithub.flixdb.R;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.FavouritesViewHolder> {

    @SuppressWarnings("CanBeFinal")
    private ArrayList<MoviesObject> FavMovies;
    @SuppressWarnings("CanBeFinal")
    private Context context;

    public FavouritesAdapter(Context c, ArrayList<MoviesObject> favlist){
        context=c;
        FavMovies = new ArrayList<>();
        FavMovies.addAll(favlist);
    }

    /**
     * This method refreshes the recycler view and is called
     * with the change in LiveData.
     */
    /*public void RefreshData(){
        FavMovies.clear();
        FavMovies.addAll(MainActivity.FavouriteMovieList);
        notifyDataSetChanged();
    }*/

    @NonNull
    @Override
    public FavouritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inf = LayoutInflater.from(parent.getContext());
        View rv_view = inf.inflate(R.layout.favourites_recyclerview_layout,parent,false);
        return new FavouritesAdapter.FavouritesViewHolder(rv_view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouritesViewHolder holder, int position) {
        MoviesObject mv_parcel = FavMovies.get(position);
        holder.tv_movie_title.setText(mv_parcel.getTitle());
        Glide.with(context)
                .load(mv_parcel.getPosterImageUri())
                .apply(new RequestOptions()
                        .placeholder(R.mipmap.imageplaceholder)
                        .dontTransform())
                .into(holder.iv_poster_image);

    }

    @Override
    public int getItemCount() {
        return FavMovies.size();
    }

    class FavouritesViewHolder extends RecyclerView.ViewHolder {
        @SuppressWarnings("CanBeFinal")
        ImageView iv_poster_image;
        @SuppressWarnings("CanBeFinal")
        TextView tv_movie_title;

        FavouritesViewHolder(View itemView) {
            super(itemView);
            iv_poster_image = itemView.findViewById(R.id.rv_fav_posterimage);
            tv_movie_title = itemView.findViewById(R.id.rv_fav_movietitle);
        }
    }
}
