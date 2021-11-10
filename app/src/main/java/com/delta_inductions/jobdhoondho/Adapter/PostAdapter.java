package com.delta_inductions.jobdhoondho.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.delta_inductions.jobdhoondho.Model.Post;
import com.delta_inductions.jobdhoondho.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class PostAdapter extends FirestoreRecyclerAdapter<Post, PostAdapter.PostHolder> {
    private OnItemClicklistener onitemclicklistner;

    public PostAdapter(@NonNull FirestoreRecyclerOptions<Post> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull PostHolder holder, int position, @NonNull Post model) {
        holder.posttext.setText("Position :"+model.getPosition()+"\n\n"+"Salary :"+model.getSalary()+"\n\n"+"Job Location :"+model.getLocation()+"\n\n"+"Job duration :"+model.getWork_period()+"\n\n"+"Job description :"+ model.getDescription());

        if(model.isApplydisplay())
        {
            holder.apply.setVisibility(View.VISIBLE);
            holder.postby.setText("Post By-"+model.getEmployername());
        }
        else
        {
            holder.apply.setVisibility(View.GONE);
        }
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_cardview,parent,false);
        return (new PostHolder(V));
    }

    public class PostHolder extends RecyclerView.ViewHolder{
        private  TextView postby;
        private TextView posttext;
        private Button  apply;
        public PostHolder(@NonNull View itemView) {
            super(itemView);
            posttext = itemView.findViewById(R.id.postjobdetails);
            postby = itemView.findViewById(R.id.postby);
            apply = itemView.findViewById(R.id.btn_apply);
            apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAbsoluteAdapterPosition();
                    if(getAbsoluteAdapterPosition()!=RecyclerView.NO_POSITION&&onitemclicklistner!=null)
                    {
                        onitemclicklistner.OnItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });
        }
    }
    public interface OnItemClicklistener
    {
        void OnItemClick(DocumentSnapshot documentSnapshot,int position);
    }
public void setOnitemclicklistner(OnItemClicklistener onitemclicklistner)
{
    this.onitemclicklistner = onitemclicklistner;
}
}
