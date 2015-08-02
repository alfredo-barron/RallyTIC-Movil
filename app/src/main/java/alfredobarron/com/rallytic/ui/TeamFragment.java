package alfredobarron.com.rallytic.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import alfredobarron.com.rallytic.R;
import alfredobarron.com.rallytic.models.entity.Rallies;
import alfredobarron.com.rallytic.models.repository.RalliesRepository;
import alfredobarron.com.rallytic.models.repository.TaskRepository;
import alfredobarron.com.rallytic.resources.SessionManager;
import butterknife.ButterKnife;
import io.realm.Realm;

import static butterknife.ButterKnife.findById;

public class TeamFragment extends Fragment {

    public TeamFragment() {
    }

    public static TeamFragment newInstance() {
        return new TeamFragment();
    }

    // Realm instance for the UI thread
    private Realm mUIThreadRealm;
    static SessionManager sessionManager;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mUIThreadRealm = Realm.getDefaultInstance();
        sessionManager = new SessionManager(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Count of uncompleted tasks
        AppCompatTextView uncompletedTaskCount = findById(view, R.id.profile_uncompleted_count_text);
        long uncompletedCount = TaskRepository.countByCompleted(mUIThreadRealm, false);
        uncompletedTaskCount.setText(String.valueOf(uncompletedCount));

        // Count of completed tasks
        AppCompatTextView completedTaskCount = findById(view, R.id.profile_completed_count_text);
        long completedCount = TaskRepository.countByCompleted(mUIThreadRealm, true);
        completedTaskCount.setText(String.valueOf(completedCount));

        // Count of all tasks
        AppCompatTextView allTaskCount = findById(view, R.id.profile_all_count_text);
        final Rallies mRally = RalliesRepository.findFirst(mUIThreadRealm);
        String score;
        if (mRally == null){
            score = "0";
        } else {
            score = String.valueOf(mRally.getScore());
        }
        allTaskCount.setText(score);

        // Description about state of task
        AppCompatTextView state = findById(view, R.id.profile_state_text);
        String stateStr;
        if (uncompletedCount == 0) {
            stateStr = getString(R.string.good_job);
        } else if(completedCount == 0){
            stateStr = getString(R.string.you_can_do_it);
        } else {
            stateStr = getString(R.string.keep_it_up);
        }
        state.setText(stateStr);

        AppCompatTextView title = findById(view, R.id.team_name_1);
        title.setText(sessionManager.obtenerUser());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUIThreadRealm.close();
    }

}
