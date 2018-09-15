package app.mpv.com.mpvapp.app.mpv.com.mpvapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import app.mpv.com.mpvapp.R;
import app.mpv.com.mpvapp.app.mpv.com.mpvapp.model.Post;

import static app.mpv.com.mpvapp.R.drawable.loading;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;
    Button mBtnShareTo;

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
        holder.mPriceProduct.setText("RD$ " + mPost.getPriceImage());
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

        mBtnShareTo.setOnClickListener((v) -> {
            holder.mImageView.buildDrawingCache();
            Bitmap bitmap = holder.mImageView.getDrawingCache();

            try {
                File file = new File(context.getCacheDir(), bitmap + ".png");
                FileOutputStream fOut = null;
                fOut = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                fOut.flush();
                fOut.close();

                file.setReadable(true, false);
                final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                //intent.putExtra(Intent.EXTRA_TEXT, ""+holder.mTitleProduct.getText());
                // intent.putExtra(Intent.EXTRA_TEXT, ""+holder.mPriceProduct.getText());
                intent.setType("image/");
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

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
            mBtnShareTo = itemView.findViewById(R.id.btn_share_to);

        }


    }
}
