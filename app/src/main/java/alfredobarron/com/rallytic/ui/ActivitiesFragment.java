package alfredobarron.com.rallytic.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import alfredobarron.com.rallytic.R;
import alfredobarron.com.rallytic.Utils;
import alfredobarron.com.rallytic.listener.OnRecyclerItemClickListener;
import alfredobarron.com.rallytic.models.entity.Task;
import alfredobarron.com.rallytic.models.repository.TaskRepository;
import alfredobarron.com.rallytic.ui.adapter.TaskListAdapter;
import alfredobarron.com.rallytic.ui.widget.DividerItemDecoration;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

import static butterknife.ButterKnife.findById;

public class ActivitiesFragment extends Fragment implements OnRecyclerItemClickListener {

    public ActivitiesFragment() {
    }

    public static ActivitiesFragment newInstance() {
        return new ActivitiesFragment();
    }

    private TaskListAdapter mTaskListAdapter;

    // Realm instance for the UI thread
    private Realm mUIThreadRealm;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mUIThreadRealm = Realm.getDefaultInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activities, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RealmResults<Task> tasks = TaskRepository.findAllByCompleted(mUIThreadRealm, false);
        mTaskListAdapter = new TaskListAdapter(tasks, this);

        RecyclerView recyclerView = findById(view, R.id.activities_list_recycler_view);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(Utils.getDrawableResource(getActivity(), R.drawable.line)));
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mTaskListAdapter);
    }

    @Override
    public void onClickRecyclerItem(View v, int position) {
        Task task = mTaskListAdapter.getItem(position);
        Intent intent = new Intent(getActivity(), RequestActivity.class);
        intent.putExtra("id", task.getId());
        intent.putExtra("question", task.getQuestion());
        intent.putExtra("answer", task.getAnswer());
        intent.putExtra("track", task.getTrack());
        intent.putExtra("intents", task.getIntents());
        intent.putExtra("time", task.getTime());
        intent.putExtra("penalty", task.getPenalty());
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTaskListAdapter.release();
        mUIThreadRealm.close();
    }

}
