package alfredobarron.com.rallytic.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import alfredobarron.com.rallytic.R;
import alfredobarron.com.rallytic.models.entity.Task;
import alfredobarron.com.rallytic.models.repository.TaskRepository;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

import static butterknife.ButterKnife.findById;

public class MapActivity extends AppCompatActivity implements
		GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener,
		LocationListener {

	private SupportMapFragment mapFragment;
	private GoogleMap map;
	private GoogleApiClient mGoogleApiClient;
	private LocationRequest mLocationRequest;
	private long UPDATE_INTERVAL = 60000;  /* 60 secs */
	private long FASTEST_INTERVAL = 5000; /* 5 secs */
	private Realm mUIThreadRealm;
	NumberFormat formatter;
	/*
	 * Define a request code to send to Google Play services This code is
	 * returned in Activity.onActivityResult
	 */
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_demo_activity);
		ButterKnife.bind(this);
		Toolbar toolbar = findById(this, R.id.toolbar);
		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayShowTitleEnabled(true);
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setTitle("Mapa");
		}
		setUpMapIfNeeded();
		mUIThreadRealm = Realm.getDefaultInstance();
		formatter = new DecimalFormat("#0.0000000");
	}

	protected void setUpMapIfNeeded(){
		if(mapFragment == null){
			mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
			if (mapFragment != null) {
				mapFragment.getMapAsync(new OnMapReadyCallback() {
					@Override
					public void onMapReady(GoogleMap map) {
						loadMap(map);
					}
				});
			} else {
				Toast.makeText(this, "Error - Mapa no encontrado", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public void addMarkes () {

		RealmResults<Task> tasks = TaskRepository.findAll(mUIThreadRealm);
		ArrayList<LatLng> list = new ArrayList<>();
		for (int i = 0; i < tasks.size(); i++){
			map.addMarker(new MarkerOptions().
					position(new LatLng(Double.parseDouble(tasks.get(i).getLat()), Double.parseDouble(tasks.get(i).getLng())))
					.title(tasks.get(i).getStation()));
			list.add(new LatLng(Double.parseDouble(tasks.get(i).getLat()), Double.parseDouble(tasks.get(i).getLng())));
		}

		PolylineOptions options = new PolylineOptions().width(10).color(Color.BLUE).geodesic(true);
		for (int z = 0; z < list.size(); z++) {
			LatLng point = list.get(z);
			options.add(point);
		}
		map.addPolyline(options);
	}

	protected void loadMap(GoogleMap googleMap) {
		map = googleMap;
		if (map != null) {
			// Map is ready
			Toast.makeText(this, "Cargando el mapa...", Toast.LENGTH_SHORT).show();
			map.setMyLocationEnabled(true);

			// Now that map has loaded, let's get our location!
			mGoogleApiClient = new GoogleApiClient.Builder(this)
					.addApi(LocationServices.API)
					.addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this).build();

			connectClient();
		} else {
			Toast.makeText(this, "Error - Mapa no se pudo cargar", Toast.LENGTH_SHORT).show();
		}
	}

	protected void connectClient() {
		// Connect the client.
		if (isGooglePlayServicesAvailable() && mGoogleApiClient != null) {
			mGoogleApiClient.connect();
		}
	}

	/*
     * Called when the Activity becomes visible.
    */
	@Override
	protected void onStart() {
		super.onStart();
		connectClient();
	}

	/*
     * Called when the Activity is no longer visible.
     */
	@Override
	protected void onStop() {
		// Disconnecting the client invalidates it.
		if (mGoogleApiClient != null) {
			mGoogleApiClient.disconnect();
		}
		super.onStop();
	}

	/*
	 * Handle results returned to the FragmentActivity by Google Play services
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Decide what to do based on the original request code
		switch (requestCode) {

			case CONNECTION_FAILURE_RESOLUTION_REQUEST:
			/*
			 * If the result code is Activity.RESULT_OK, try to connect again
			 */
				switch (resultCode) {
					case Activity.RESULT_OK:
						mGoogleApiClient.connect();
						break;
				}

		}
	}

	private boolean isGooglePlayServicesAvailable() {
		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {
			// In debug mode, log the status
			Log.d("Location Updates", "Google Play services is available.");
			return true;
		} else {
			// Get the error dialog from Google Play services
			Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this,
					CONNECTION_FAILURE_RESOLUTION_REQUEST);

			// If Google Play services can provide an error dialog
			if (errorDialog != null) {
				// Create a new DialogFragment for the error dialog
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();
				errorFragment.setDialog(errorDialog);
				errorFragment.show(getSupportFragmentManager(), "Location Updates");
			}

			return false;
		}
	}

	/*
	 * Called by Location Services when the request to connect the client
	 * finishes successfully. At this point, you can request the current
	 * location or start periodic updates
	 */
	@Override
	public void onConnected(Bundle dataBundle) {
		// Display the connection status
		Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
		if (location != null) {
			addMarkes();
			Toast.makeText(this, "Se encontro tú ubicación GPS", Toast.LENGTH_SHORT).show();
			LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
			map.animateCamera(cameraUpdate);
			startLocationUpdates();
		} else {
			Toast.makeText(this, "Ubicación no encontrada, favor de habitilar el GPS", Toast.LENGTH_SHORT).show();
			GPSTracker gpsTracker = new GPSTracker(this);
			gpsTracker.showSettingsAlert();
		}
	}

	protected void startLocationUpdates() {
		mLocationRequest = new LocationRequest();
		mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
		mLocationRequest.setInterval(UPDATE_INTERVAL);
		mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
		LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
				mLocationRequest, this);
	}

	public void onLocationChanged(Location location) {
		// Report to the UI that the location was updated
		String msg = "Ubicación actualizada: " +
				Double.toString(location.getLatitude()) + "," +
				Double.toString(location.getLongitude());
		Log.d("Ubicación: Lat: ", location.getLatitude() + " Lng: " + location.getLongitude());
		Task t = TaskRepository.find(mUIThreadRealm, 2);
		Log.d("Query: Lat: ", t.getLat() + " Lng: " + t.getLng());
		Task task = TaskRepository.checkin(mUIThreadRealm, Double.toString(location.getLatitude()), Double.toString(location.getLongitude()));
		if (task != null) {
			checkIn();
		} else {
			Toast.makeText(this, "Acercate más", Toast.LENGTH_SHORT).show();
		}
	}

	/*
     * Called by Location Services if the connection to the location client
     * drops because of an error.
     */
	@Override
	public void onConnectionSuspended(int i) {
		if (i == CAUSE_SERVICE_DISCONNECTED) {
			Toast.makeText(this, "Desconectado. Favor, vuelva a conectar.", Toast.LENGTH_SHORT).show();
		} else if (i == CAUSE_NETWORK_LOST) {
			Toast.makeText(this, "Red perdida. Favor, vuelva a conectar.", Toast.LENGTH_SHORT).show();
		}
	}

	/*
	 * Called by Location Services if the attempt to Location Services fails.
	 */
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
		if (connectionResult.hasResolution()) {
			try {
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(this,
						CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
			} catch (IntentSender.SendIntentException e) {
				// Log the error
				e.printStackTrace();
			}
		} else {
			Toast.makeText(getApplicationContext(),
					"Apenado. Los servicios de localización no están disponibles para usted", Toast.LENGTH_LONG).show();
		}
	}

	public void checkIn() {
		final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("Pregunta desbloqueada");
		alertDialog.setMessage("Ve a activades a desbloquear el siguiente lugar!");
		alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {

			}
		});
		alertDialog.show();
	}


	// Define a DialogFragment that displays the error dialog
	public static class ErrorDialogFragment extends DialogFragment {

		// Global field to contain the error dialog
		private Dialog mDialog;

		// Default constructor. Sets the dialog field to null
		public ErrorDialogFragment() {
			super();
			mDialog = null;
		}

		// Set the dialog to display
		public void setDialog(Dialog dialog) {
			mDialog = dialog;
		}

		// Return a Dialog to the DialogFragment.
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return mDialog;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mUIThreadRealm.close();
	}
}