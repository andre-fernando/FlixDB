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

import comandre_fernando.httpsgithub.flixdb.components.EndlessRecyclerViewScrollListener;
import comandre_fernando.httpsgithub.flixdb.data_objects.MoviesObject;
import comandre_fernando.httpsgithub.flixdb.R;

/** This tbe custom recycler view adapter I created based on your tutorials. */

public class MovieListAdapter extends RecyclerView.Adapter <MovieListAdapter.ViewHolder> {

    @SuppressWarnings("CanBeFinal")
    private ArrayList<MoviesObject> MoviesList;
    @SuppressWarnings("CanBeFinal")
    private Context Rv_Context;

    //The Constructor
    public MovieListAdapter(Context context, ArrayList<MoviesObject> mv){
        Rv_Context=context;
        MoviesList= new ArrayList<>();
        MoviesList.addAll(mv);
    }

    // A method to get the context.
    private Context getContext(){ return Rv_Context;}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inf = LayoutInflater.from(parent.getContext());
        View rv_view = inf.inflate(R.layout.main_recyclerview_layout,parent,false);
        return new ViewHolder(rv_view);
    }

    /**
     * This method facilitates the reset and change in data in the recycler view.
     * @param updatedlist The ArrayList with the new data
     * @param Endless An object of the Endless Scroll listener, used to reset it's state.
     */
    public void RefreshData(ArrayList<MoviesObject> updatedlist, EndlessRecyclerViewScrollListener Endless){
        MoviesList.clear();
        MoviesList.addAll(updatedlist);
        notifyDataSetChanged();
        Endless.resetState(); // This method  is to reset the scroll listener
    }

    /**
     *  This expands the data in the Recycler View by adding an ArrayList to
     *  the existing data
     * @param ListToAdd Arraylist of data that addsto the existing data.
     */
    public void AddDataToList(ArrayList<MoviesObject> ListToAdd){
        MoviesList.addAll(ListToAdd);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MoviesObject mv_parcel = MoviesList.get(position);
        holder.tv_movietitle.setText(mv_parcel.getTitle());
        Glide.with(getContext())
                .load(mv_parcel.getPosterImageUri())
                .apply(new RequestOptions()
                        .placeholder(R.mipmap.imageplaceholder)
                        .dontTransform())
                .into(holder.iv_poster);
    }

    @Override
    public int getItemCount() {
        return MoviesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @SuppressWarnings("CanBeFinal")
        ImageView iv_poster;
        @SuppressWarnings("CanBeFinal")
        TextView tv_movietitle;

        ViewHolder(View itemView) {

            super(itemView);
            iv_poster = itemView.findViewById(R.id.rv_posterimage);
            tv_movietitle = itemView.findViewById(R.id.rv_movietitle);

        }
    }

}
