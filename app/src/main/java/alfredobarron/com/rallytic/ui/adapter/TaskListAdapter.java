package alfredobarron.com.rallytic.ui.adapter;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.security.acl.Group;

import alfredobarron.com.rallytic.R;
import alfredobarron.com.rallytic.listener.OnRecyclerItemClickListener;
import alfredobarron.com.rallytic.models.entity.Task;
import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmResults;

/**
 * Created by alfredobarron on 26/07/15.
 */
public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder>{

    private RealmResults<Task> mTasks;

    private static OnRecyclerItemClickListener sOnRecyclerItemClickListener;

    public TaskListAdapter(RealmResults<Task> tasks,
                           OnRecyclerItemClickListener onRecyclerItemClickListener) {
        super();
        mTasks = tasks;
        sOnRecyclerItemClickListener = onRecyclerItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Task task = mTasks.get(position);
        holder.groupNameText.setText(task.getName());
        holder.groupDescriptionText.setText(task.getStation());
    }

    public Task getItem(int position) {
        return mTasks.get(position);
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    public void release() {
        sOnRecyclerItemClickListener = null;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.group_name)
        AppCompatTextView groupNameText;

        @Bind(R.id.group_description)
        AppCompatTextView groupDescriptionText;

        public ViewHolder(final View v) {
            super(v);
            v.setOnClickListener(this);
            ButterKnife.bind(this, v);
        }

        @Override
        public void onClick(View view) {
            int position = this.getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                sOnRecyclerItemClickListener.onClickRecyclerItem(view, position);
            }
        }
    }

}
