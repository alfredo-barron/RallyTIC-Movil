package alfredobarron.com.rallytic.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import alfredobarron.com.rallytic.R;
import alfredobarron.com.rallytic.Utils;
import alfredobarron.com.rallytic.listener.OnCanSetLayoutParamsListener;
import alfredobarron.com.rallytic.models.entity.Rallies;
import alfredobarron.com.rallytic.models.entity.Task;
import alfredobarron.com.rallytic.models.repository.RalliesRepository;
import alfredobarron.com.rallytic.models.repository.TaskRepository;
import alfredobarron.com.rallytic.ui.widget.BottomButton;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static butterknife.ButterKnife.findById;

public class RequestActivity extends AppCompatActivity {

    private int id_activity;

    private String question;

    private String track;

    private int time, intents;

    private String answer;

    private String request;

    @Bind(R.id.question_text)
    AppCompatTextView mQuestionTextView;

    @Bind(R.id.answer_edit_text)
    AppCompatEditText mAnswerEditText;

    @Bind(R.id.count_intents_text)
    AppCompatTextView mIntentsTextView;

    @Bind(R.id.count_time_text)
    AppCompatTextView mTimeTextView;

    @Bind(R.id.track_fab)
    FloatingActionButton mFloatingActionButton;

    @Bind(R.id.verificar_button)
    BottomButton mVerificar;

    // Realm instance for the UI thread
    private Realm mUIThreadRealm;

    private CountDownTimer timer;

    int score = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        ButterKnife.bind(this);
        Toolbar toolbar = findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            id_activity = extras.getInt("id");
            question = extras.getString("question");
            answer = extras.getString("answer");
            track = extras.getString("track");
            intents = extras.getInt("intents");
            time = extras.getInt("time");
        }

        mQuestionTextView.setText(question);
        mIntentsTextView.setText(String.valueOf(intents));
        mTimeTextView.setText(String.valueOf(time) + ":00");

        mUIThreadRealm = Realm.getDefaultInstance();
        setupFloatingActionButton();

        int segundos = time * 60;
        //showTime(segundos);

        mVerificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAnswerEditText.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Ingrese la clave!", Toast.LENGTH_SHORT).show();
                } else {
                    request = mAnswerEditText.getText().toString();
                    if (request.equalsIgnoreCase(answer)) {
                        //Toast.makeText(getApplicationContext(),"Respuesta correcta", Toast.LENGTH_SHORT).show();
                        final Task task = TaskRepository.find(mUIThreadRealm, id_activity);
                        TaskRepository.updateByCompleted(mUIThreadRealm, task, true);
                        final Rallies mRally = RalliesRepository.findFirst(mUIThreadRealm);
                        int total = score + mRally.getScore();
                        RalliesRepository.updateByScore(mUIThreadRealm,mRally,total);
                        nextActivity();
                    } else {
                        Vibrator vibrar = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        vibrar.vibrate(500);
                        Toast.makeText(getApplicationContext(),"Respuesta incorrecta", Toast.LENGTH_SHORT).show();
                        if (intents > 0) {
                            intents--;
                            score--;
                            mIntentsTextView.setText(String.valueOf(intents));
                        }
                    }
                }
            }
        });
    }

    public void showTime(int countdownMillis){
        if(timer != null) { timer.cancel(); }
        timer = new CountDownTimer(countdownMillis, 1000) {
            public void onTick(long millisUntilFinished) {
                mTimeTextView.setText(String.valueOf(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                Toast.makeText(getApplicationContext(), "Tiempo agotado!", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    private void setupFloatingActionButton() {
        Utils.setFabLayoutParams(mFloatingActionButton, new OnCanSetLayoutParamsListener() {
            @Override
            public void onCanSetLayoutParams() {
                // setMargins to fix floating action button's layout bug
                CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mFloatingActionButton
                        .getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                mFloatingActionButton.setLayoutParams(params);
            }
        });
    }

    @OnClick(R.id.track_fab)
    public void onClickFAB() {
        Vibrator vibrar = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrar.vibrate(500);
        trackAnswer();
        score = score - 2;
    }

    public void trackAnswer() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Pista");
        alertDialog.setIcon(R.drawable.ic_accessibility_black_36dp);
        alertDialog.setMessage(track);
        alertDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.show();
    }

    public void nextActivity() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Actividad desbloqueada");
        alertDialog.setIcon(R.drawable.ic_directions_walk_black_48dp);
        alertDialog.setMessage("Respuesta correcta, ¿Desea continuar" +
                               " a la siguiente estación? \n\n" +
                               "                 Puntuaje: " + score);
        alertDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(getApplicationContext(), MapActivity.class));
            }
        });
        alertDialog.show();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUIThreadRealm.close();
    }

}
