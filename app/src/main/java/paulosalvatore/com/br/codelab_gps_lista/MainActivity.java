package paulosalvatore.com.br.codelab_gps_lista;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

	private static final int PERMISSION_REQUEST_LOCATIION = 1;
	private GoogleMap mMap;
	private Location ultimaLocalizacao;
	private DatabaseManager baseDados;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		DatabaseHelper helper = new DatabaseHelper(this.getApplicationContext());
		DatabaseManager.initializeInstance(helper);
		baseDados = DatabaseManager.getInstance();

		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this,
					new String[]{
							Manifest.permission.ACCESS_FINE_LOCATION,
							Manifest.permission.ACCESS_COARSE_LOCATION
					},
					PERMISSION_REQUEST_LOCATIION);
		} else {
			inicializarLocalizacao();
		}
	}

	@SuppressLint("MissingPermission")
	private void inicializarLocalizacao() {
		// Inicia a busca pela posição do usuário
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		try {
			Criteria locationCriteria = new Criteria();
			locationCriteria.setAccuracy(Criteria.ACCURACY_LOW); // Define a precisão da posição

			String provider = locationManager.getBestProvider(locationCriteria, true);

			locationManager.requestLocationUpdates(
					provider,
					1L,
					2F,
					this
			);

			ultimaLocalizacao = locationManager.getLastKnownLocation(provider);

			inicializarMapa();
		} catch (Exception ex) {
		}
	}

	private void inicializarMapa() {
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.mvMapa);
		mapFragment.getMapAsync(this);
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;

		if (ultimaLocalizacao == null) {
			return;
		}

		@SuppressLint("MissingPermission") LatLng posicao =
				new LatLng(
						ultimaLocalizacao.getLatitude(),
						ultimaLocalizacao.getLongitude()
				);
		mMap.addMarker(new MarkerOptions().position(posicao).title("Minha posição"));
		mMap.moveCamera(CameraUpdateFactory.newLatLng(posicao));
	}

	@Override
	public void onLocationChanged(Location location) {
		ultimaLocalizacao = location;

		Posicao posicao =
				new Posicao(
						location.getLatitude(),
						location.getLatitude(),
						Calendar.getInstance().getTime().toString()
				);

		baseDados.inserirPosicao(posicao);

		// Atualiza a posição exibida pelo mapa quando a localização do usuário é encontrada
		mMap.animateCamera(
				CameraUpdateFactory.newLatLngZoom(
						new LatLng(
								ultimaLocalizacao.getLatitude(),
								ultimaLocalizacao.getLongitude()
						),
						14
				)
		);
	}

	@Override
	public void onStatusChanged(String s, int i, Bundle bundle) {
		Toast.makeText(this, "Iniciando busca de posição", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderEnabled(String s) {
		Toast.makeText(this, "Buscando sinal de GPS", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderDisabled(String s) {
		Toast.makeText(this, "GPS desabilitado", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		switch (requestCode) {
			case PERMISSION_REQUEST_LOCATIION: {
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					Log.d("Permissão", "Permissão concedida.");
					inicializarLocalizacao();
				} else {
					Log.d("Permissão", "Permissão negada.");
				}
			}
		}
	}

	public void abrirAtividadeLista(View view) {
		Intent i = new Intent(this, ListaActivity.class);
		startActivity(i);
	}
}
