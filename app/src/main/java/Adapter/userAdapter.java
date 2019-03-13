package Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ayo.retrofitpostbody.R;

import java.util.List;

import model.Post;

public class userAdapter extends RecyclerView.Adapter<userAdapter.RetroViewHolder> {

    private Context context;

    private List<Post> postList;

    public userAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public userAdapter.RetroViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View v =LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user, viewGroup, false);
        return new RetroViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull userAdapter.RetroViewHolder retroViewHolder, int position) {

        Post list = postList.get(position);
        retroViewHolder.username.setText(list.getName());
        retroViewHolder.password.setText(list.getPassword());
        retroViewHolder.about.setText(list.getAbout());

    }

    @Override
    public int getItemCount() {
        if(postList==  null){
            return 0;
        }else
        return postList.size();
    }

    public void setResult(List<Post> list){
        postList = list;
        notifyDataSetChanged();
    }

    public class RetroViewHolder extends RecyclerView.ViewHolder{
        public TextView username, password, about;
        public RetroViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.useName);
            password = itemView.findViewById(R.id.usePassword);
            about = itemView.findViewById(R.id.useAbout);
        }
    }
}
