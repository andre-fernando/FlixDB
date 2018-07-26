package comandre_fernando.httpsgithub.flixdb.components.adapters;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import comandre_fernando.httpsgithub.flixdb.R;
import comandre_fernando.httpsgithub.flixdb.components.constants.BaseUrl;
import comandre_fernando.httpsgithub.flixdb.components.constants.Query;
import comandre_fernando.httpsgithub.flixdb.components.constants.UrlPath;
import comandre_fernando.httpsgithub.flixdb.data_objects.TrailerObject;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private final Context context;
    private final ArrayList<TrailerObject> list;

    public TrailerAdapter(Context c, ArrayList<TrailerObject> l){
        context=c;
        list = new ArrayList<>();
        list.addAll(l);
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inf = LayoutInflater.from(parent.getContext());
        View rv_view = inf.inflate(R.layout.trailer_recyclerview_layout,parent,false);
        return new TrailerViewHolder(rv_view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        Uri ThumbnailUri = Uri.parse(BaseUrl.YOUTUBE_THUMBNAIL)
                .buildUpon()
                .appendPath(list.get(position).getKey())
                .appendPath(UrlPath.YOUTUBE_IMAGE_QUALITY)
                .build();

        Glide.with(context)
                .load(ThumbnailUri)
                .apply(new RequestOptions()
                    .centerCrop())
                .into(holder.iv_Trailer_Thumbnail);

        holder.tv_Trailer_Name.setText(list.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder {
        @SuppressWarnings("CanBeFinal")
        CardView cv_Trailer;
        @SuppressWarnings("CanBeFinal")
        ImageView iv_Trailer_Thumbnail;
        @SuppressWarnings("CanBeFinal")
        TextView tv_Trailer_Name;

        TrailerViewHolder(View itemView) {
            super(itemView);
            cv_Trailer = itemView.findViewById(R.id.card_trailer);
            iv_Trailer_Thumbnail= itemView.findViewById(R.id.iv_trailer_thumbnail);
            tv_Trailer_Name = itemView.findViewById(R.id.tv_trailer_name);

            cv_Trailer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri TrailerUri = Uri.parse(BaseUrl.YOUTUBE_VIDEO)
                            .buildUpon()
                            .appendQueryParameter(Query.YOUTUBE_VIDEO, list.get(getAdapterPosition()).getKey())
                            .build();
                    Intent Watch_Trailer = new Intent(Intent.ACTION_VIEW,TrailerUri);
                    if (Watch_Trailer.resolveActivity(context.getPackageManager())!=null){
                        context.startActivity(Watch_Trailer);
                    } else {
                        Toast.makeText(context, "No App to run this link!!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
