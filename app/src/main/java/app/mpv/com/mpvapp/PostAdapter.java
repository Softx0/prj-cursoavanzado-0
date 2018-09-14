package app.mpv.com.mpvapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import static app.mpv.com.mpvapp.R.drawable.loading;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;

    public PostAdapter() {

    }

    public PostAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Post mPost = posts.get(position);

        holder.mTitleProduct.setText(mPost.getTitleImage());
        holder.mPriceProduct.setText(mPost.getPriceImage());
        holder.mAddressProduct.setText(mPost.getAddressImage());

        Glide.with(context)
                .load(mPost.getImageUrl())
                .into(holder.mImageView);
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .fitCenter()
                .placeholder(loading)
                .error(loading)
                .priority(Priority.HIGH);

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTitleProduct;
        public TextView mPriceProduct;
        public ImageView mImageView;
        public TextView mAddressProduct;

        public ViewHolder(View itemView) {
            super(itemView);

            mTitleProduct = itemView.findViewById(R.id.titleProducts);
            mPriceProduct = itemView.findViewById(R.id.price);
            mImageView = itemView.findViewById(R.id.image_user);
            mAddressProduct = itemView.findViewById(R.id.Myaddress);

        }


    }
}
