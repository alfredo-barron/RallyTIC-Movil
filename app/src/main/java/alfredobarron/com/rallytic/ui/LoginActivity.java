package alfredobarron.com.rallytic.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Toast;

import alfredobarron.com.rallytic.R;
import alfredobarron.com.rallytic.listener.OnMakeSnackbar;
import alfredobarron.com.rallytic.models.server.RequestTeam;
import alfredobarron.com.rallytic.resources.ConnectionDetector;
import alfredobarron.com.rallytic.resources.SessionManager;
import alfredobarron.com.rallytic.rest.RestClient;
import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    @Bind(R.id.user_login_edit_text)
    AppCompatEditText mUserEditText;

    @Bind(R.id.password_login_edit_text)
    AppCompatEditText mPasswordEditText;

    String user, password;
    ProgressDialog progressDialog;
    static SessionManager sessionManager;
    ConnectionDetector cd;
    Boolean isInternetPresent;
    private OnMakeSnackbar mOnMakeSnackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(getApplicationContext());
        cd = new ConnectionDetector(this);
        if (getApplication() instanceof OnMakeSnackbar) {
            mOnMakeSnackbar = (OnMakeSnackbar) getApplication();
        }
    }

    public void log_in(View view){
        if(mUserEditText.getText().length() == 0 || mPasswordEditText.getText().length() == 0){
            Toast.makeText(this,"Rellene los campos",Toast.LENGTH_SHORT).show();
        } else {
            user = mUserEditText.getText().toString();
            password = mPasswordEditText.getText().toString();
            isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent = cd.isConnectingToInternet()) {
                login(user, password);
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setTitle("Verificando");
                progressDialog.setMessage("Espere un momento");
                progressDialog.setIndeterminate(false);
                progressDialog.setCancelable(true);
                progressDialog.show();
            } else {
                Toast.makeText(this,"Verifique su conexión",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void login(String user, String password) {
        RestClient.getApiService().login(user,password, new Callback<RequestTeam>() {
            @Override
            public void success(RequestTeam requestTeam, Response response) {
                if (requestTeam.getStatus() == 1) {
                    progressDialog.dismiss();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    sessionManager.createLoginSession(requestTeam.getTeam().getId(),requestTeam.getTeam().getName().toString());
                    finish();
                } else {
                    progressDialog.dismiss();
                    Vibrator vibrar = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibrar.vibrate(500);
                    Toast.makeText(getApplicationContext(),"Equipo o contraseña incorrecta",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(),"Intente de nuevo",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
