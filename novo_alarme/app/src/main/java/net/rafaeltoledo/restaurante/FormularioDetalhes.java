package net.rafaeltoledo.restaurante;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class FormularioDetalhes extends Activity {
	
	EditText nome = null;
	EditText endereco = null;
	EditText anotacoes = null;
	EditText twitter = null;
	RadioGroup tipos = null;
	TextView localizacao = null;
	GerenciadorRestaurantes gerenciador = null;
	String idRestaurante = null;
	LocationManager locationManager = null;
	double latitude = 0.0;
	double longitude = 0.0;

	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_detalhes);
		
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		gerenciador = new GerenciadorRestaurantes(this);

		nome = (EditText) findViewById(R.id.nome);
		endereco = (EditText) findViewById(R.id.end);
		anotacoes = (EditText) findViewById(R.id.anotacoes);
		twitter = (EditText) findViewById(R.id.twitter);
		tipos = (RadioGroup) findViewById(R.id.tipos);
		localizacao = (TextView) findViewById(R.id.localizacao);
		
		idRestaurante = getIntent().getStringExtra(ListaRestaurantes._ID);		
		
		if (idRestaurante != null) {
			carregar();
		}
	}
	
	@Override
	public void onPause() {
		try {
			salvar();
		} catch (NullPointerException ex) {
			Toast.makeText(FormularioDetalhes.this, "Descartado.", Toast.LENGTH_SHORT);
		}		
		
		super.onPause();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		locationManager.removeUpdates(onLocationChange);
		
		gerenciador.close();
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (idRestaurante == null) {
			menu.findItem(R.id.localizacao).setEnabled(false);
			menu.findItem(R.id.mapa).setEnabled(false);
		}
		
		return super.onPrepareOptionsMenu(menu);
	}
	
	private void salvar() {
		String tipo = null;

		switch (tipos.getCheckedRadioButtonId()) {
		case R.id.rodizio:
			tipo = "rodizio";
			break;
		case R.id.fast_food:
			tipo = "fast_food";
			break;
		case R.id.a_domicilio:
			tipo = "a_domicilio";
			break;
		}
		
		if (tipo != null && endereco.getText().toString() != null &&
				nome.getText().toString() != null) {
			
			if (idRestaurante == null) {
				gerenciador.inserir(nome.getText().toString(), 
						endereco.getText().toString(), 
						tipo, anotacoes.getText().toString(),
						twitter.getText().toString());
			} else {
				gerenciador.atualizar(idRestaurante, 
						nome.getText().toString(), 
						endereco.getText().toString(), 
						tipo, anotacoes.getText().toString(),
						twitter.getText().toString());
			}
		}
			
		finish();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.opcao_detalhes, menu);
		
		return super.onCreateOptionsMenu(menu);
	};
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.twitter) {
			if (redeDisponivel()) {
				Toast.makeText(this, "Conexão com a Internet indisponível", Toast.LENGTH_LONG).show();

			} else {
				Toast.makeText(this, "Conexão com a Internet indisponível", Toast.LENGTH_LONG).show();
			}
			
			return true;
		} else if (item.getItemId() == R.id.localizacao) {
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 
					0, 0, onLocationChange);
		} else if (item.getItemId() == R.id.mapa) {
			Intent i = new Intent(this, MapaRestaurante.class);
			
			i.putExtra(MapaRestaurante.LATITUDE_EXTRA, latitude);
			i.putExtra(MapaRestaurante.LONGITUDE_EXTRA, longitude);
			i.putExtra(MapaRestaurante.NOME_EXTRA, nome.getText().toString());
			
			startActivity(i);
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private boolean redeDisponivel() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		
		return (info != null);
	}
	
	private void carregar() {
		Cursor c = gerenciador.obterPorId(idRestaurante);
		
		c.moveToFirst();
		nome.setText(gerenciador.obterNome(c));
		endereco.setText(gerenciador.obterEndereco(c));
		anotacoes.setText(gerenciador.obterAnotacoes(c));
		twitter.setText(gerenciador.obterTwitter(c));
		
		if (gerenciador.obterTipo(c).equals("rodizio")) {
			tipos.check(R.id.rodizio);
		} else if (gerenciador.obterTipo(c).equals("fast_food")) {
			tipos.check(R.id.fast_food);
		} else {
			tipos.check(R.id.a_domicilio);
		}
		
		latitude = gerenciador.obterLatitude(c);
		longitude = gerenciador.obterLongitude(c);
		
		localizacao.setText(String.valueOf(gerenciador.obterLatitude(c)) +
				", " + String.valueOf(gerenciador.obterLongitude(c)));
		
		c.close();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {		
		super.onSaveInstanceState(outState);
		
		outState.putString("nome", nome.getText().toString());
		outState.putString("endereco", endereco.getText().toString());
		outState.putString("anotacoes", anotacoes.getText().toString());
		outState.putInt("tipo", tipos.getCheckedRadioButtonId());
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {		
		super.onRestoreInstanceState(savedInstanceState);
		
		nome.setText(savedInstanceState.getString("nome"));
		endereco.setText(savedInstanceState.getString("endereco"));
		anotacoes.setText(savedInstanceState.getString("anotacoes"));
		tipos.check(savedInstanceState.getInt("tipo"));
	}
	
	LocationListener onLocationChange = new LocationListener() {

		public void onLocationChanged(Location location) {			
			gerenciador.atualizarLocalizacao(idRestaurante, location.getLatitude(), 
					location.getLongitude());
			localizacao.setText(String.valueOf(location.getLatitude()) + ", " +
					String.valueOf(location.getLongitude()));
			locationManager.removeUpdates(onLocationChange);
			
			Toast.makeText(FormularioDetalhes.this, "Localização salva", Toast.LENGTH_LONG);
		}

		public void onProviderDisabled(String provider) {
			// Requerido pela interface. Não utilizado			
		}

		public void onProviderEnabled(String provider) {
			// Requerido pela interface. Não utilizado			
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// Requerido pela interface. Não utilizado			
		}		
	};
}
