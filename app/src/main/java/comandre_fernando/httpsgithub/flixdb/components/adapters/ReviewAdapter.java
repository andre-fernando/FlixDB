package comandre_fernando.httpsgithub.flixdb.components.adapters;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import comandre_fernando.httpsgithub.flixdb.data_objects.ReviewObject;
import comandre_fernando.httpsgithub.flixdb.R;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    @SuppressWarnings("CanBeFinal")
    private ArrayList<ReviewObject> ReviewList;

    public ReviewAdapter(ArrayList<ReviewObject> r){
        ReviewList = new ArrayList<>();
        ReviewList.addAll(r);
    }

    /**
     *  This expands the data in the Recycler View by adding an ArrayList to
     *  the existing data
     * @param ListToAdd Arraylist of data that addsto the existing data.
     */
    public void AddDataToList(ArrayList<ReviewObject> ListToAdd){
        ReviewList.addAll(ListToAdd);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inf = LayoutInflater.from(parent.getContext());
        View view = inf.inflate(R.layout.review_recyclerview_layout,parent,false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        holder.tv_author.setText(ReviewList.get(position).getAuthor());
        holder.tv_content.setText(ReviewList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return ReviewList.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        @SuppressWarnings("CanBeFinal")
        private TextView tv_author;
        @SuppressWarnings("CanBeFinal")
        private TextView tv_content;

        ReviewViewHolder(View itemView) {
            super(itemView);
            tv_author = itemView.findViewById(R.id.tv_review_author);
            tv_content = itemView.findViewById(R.id.tv_review_content);


        }
    }
}
