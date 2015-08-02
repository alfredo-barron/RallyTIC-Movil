package alfredobarron.com.rallytic.ui.adapter;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import alfredobarron.com.rallytic.R;
import alfredobarron.com.rallytic.models.entity.Rallies;
import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmResults;

/**
 * Created by alfredobarron on 27/07/15.
 */
public class RallyListAdapter extends RecyclerView.Adapter<RallyListAdapter.ViewHolder> {

    private RealmResults<Rallies> mRallies;

    public RallyListAdapter(RealmResults<Rallies> rallies) {
        super();
        mRallies = rallies;
    }

    public void setData(RealmResults<Rallies> rallies) {
        this.mRallies = rallies;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rally_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Rallies rally = mRallies.get(position);
        holder.rallyNameText.setText(rally.getName());
        holder.rallyDescriptionText.setText(rally.getDescription());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.event_rally_name)
        AppCompatTextView rallyNameText;

        @Bind(R.id.event_rally_description)
        AppCompatTextView rallyDescriptionText;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
