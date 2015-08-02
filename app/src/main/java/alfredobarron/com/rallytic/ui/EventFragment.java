package alfredobarron.com.rallytic.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.Iterator;

import alfredobarron.com.rallytic.R;
import alfredobarron.com.rallytic.Utils;
import alfredobarron.com.rallytic.listener.OnMakeSnackbar;
import alfredobarron.com.rallytic.models.entity.Rallies;
import alfredobarron.com.rallytic.models.entity.Task;
import alfredobarron.com.rallytic.models.repository.RalliesRepository;
import alfredobarron.com.rallytic.models.repository.TaskRepository;
import alfredobarron.com.rallytic.models.server.Activities;
import alfredobarron.com.rallytic.models.server.Rally;
import alfredobarron.com.rallytic.resources.ConnectionDetector;
import alfredobarron.com.rallytic.resources.SessionManager;
import alfredobarron.com.rallytic.rest.RestClient;
import alfredobarron.com.rallytic.ui.adapter.RallyListAdapter;
import alfredobarron.com.rallytic.ui.widget.BottomButton;
import alfredobarron.com.rallytic.ui.widget.DividerItemDecoration;
import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static butterknife.ButterKnife.findById;

public class EventFragment extends Fragment {

    private static final int REQUEST_CREATE_TASK_ACTIVITY = 1000;

    public EventFragment() {
    }

    public static EventFragment newInstance() {
        return new EventFragment();
    }

    @Bind(R.id.event_count)
    AppCompatTextView mEventCountTextView;

    @Bind(R.id.event_rally_name)
    AppCompatTextView mEventNameTextView;

    @Bind(R.id.event_rally_description)
    AppCompatTextView mEventDescriptionTextView;

    private Realm mUIThreadRealm;

    private OnMakeSnackbar mOnMakeSnackbar;

    ProgressDialog progressDialog;

    static SessionManager sessionManager;

    ConnectionDetector cd;

    Boolean isInternetPresent;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof OnMakeSnackbar) {
            mOnMakeSnackbar = (OnMakeSnackbar) activity;
        }

        sessionManager = new SessionManager(getActivity());
        cd = new ConnectionDetector(getActivity());

        mUIThreadRealm = Realm.getDefaultInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Rallies mRally = RalliesRepository.findFirst(mUIThreadRealm);
        String name, description;
        if (mRally == null) {
            name = "Sin eventos";
            description = "";
        } else {
            name = mRally.getName();
            description = mRally.getDescription();
        }
        mEventNameTextView.setText(name);
        mEventDescriptionTextView.setText(description);

        long taskCount = TaskRepository.countByCompleted(mUIThreadRealm, false);
        mEventCountTextView.setText(String.valueOf(taskCount));

        AppCompatTextView homeDayOfWeekTextView = findById(view, R.id.event_dayOfWeek);
        homeDayOfWeekTextView.setText(Utils.getDayOfWeekString());

        AppCompatTextView homeDateTextView = findById(view, R.id.event_date);
        homeDateTextView.setText(Utils.getDateString().toUpperCase());

        BottomButton btnEvento = findById(view, R.id.evento_disponible);
        btnEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEvent();

                long taskCount = TaskRepository.countByCompleted(mUIThreadRealm, false);
                mEventCountTextView.setText(String.valueOf(taskCount));
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CREATE_TASK_ACTIVITY:
                if (resultCode == Activity.RESULT_OK) {

                }
                break;
        }
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

    public void checkEvent(){
        if (isInternetPresent = cd.isConnectingToInternet()) {
            rally(sessionManager.obtenerId());
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Verificando");
            progressDialog.setMessage("Buscando eventos disponibles");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        } else {
            showSnackbarWhenDismiss("Verifique su conexión");
        }
    }

    private void rally(int id) {
        RestClient.getApiService().getEvent(id, new Callback<Rally>() {
            @Override
            public void success(Rally rally, Response response) {
                if (rally.getStatus() == 200) {
                    progressDialog.dismiss();
                    progressDialog.show();
                    progressDialog.setTitle("Descargando");
                    progressDialog.setMessage("Espere un momento");
                    progressDialog.setIndeterminate(false);
                    progressDialog.setCancelable(true);
                    TaskRepository.truncate(mUIThreadRealm);
                    for (int i = 0; i < rally.getActivities().size(); i++) {
                        TaskRepository.create(mUIThreadRealm, rally.getActivities().get(i).getId(), rally.getActivities().get(i).getName(),
                                rally.getActivities().get(i).getTime(), rally.getActivities().get(i).getIntents(), rally.getActivities().get(i).getStation(),
                                rally.getActivities().get(i).getLat(), rally.getActivities().get(i).getLng(), rally.getActivities().get(i).getQuestionId(),
                                rally.getActivities().get(i).getQuestion(), rally.getActivities().get(i).getAnswer(), rally.getActivities().get(i).getTrack(),
                                rally.getActivities().get(i).getPenalty(), i);
                    }
                    RalliesRepository.truncate(mUIThreadRealm);
                    RalliesRepository.create(mUIThreadRealm, rally.getEvent().getId(), rally.getEvent().getName(), rally.getEvent().getDescription());
                    progressDialog.dismiss();
                    showSnackbarWhenDismiss("Tiene une evento disponible!");
                } else {
                    progressDialog.dismiss();
                    showSnackbarWhenDismiss("Intente un poco más tarde");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();
                showSnackbarWhenDismiss("Intenta de nuevo");
            }
        });
    }

    private void showSnackbarWhenDismiss(String text) {
        if (mOnMakeSnackbar != null) {
            Snackbar snackbar = mOnMakeSnackbar.onMakeSnackbar(text, Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }

}
