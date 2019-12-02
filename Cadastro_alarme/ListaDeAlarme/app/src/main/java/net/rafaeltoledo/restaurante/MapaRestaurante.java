package net.rafaeltoledo.restaurante;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class MapaRestaurante extends MapActivity {
	
	public static final String LATITUDE_EXTRA = "net.rafaeltoledo.LATITUDE_EXTRA";
	public static final String LONGITUDE_EXTRA = "net.rafaeltoledo.LONGITUDE_EXTRA";
	public static final String NOME_EXTRA = "net.rafaeltoledo.NOME_EXTRA";
	private MapView mapa = null;
	
	@Override
	public void onCreate(Bundle icicle) {		
		super.onCreate(icicle);
		setContentView(R.layout.mapa);
		
		double latitude = getIntent().getDoubleExtra(LATITUDE_EXTRA, 0);
		double longitude = getIntent().getDoubleExtra(LONGITUDE_EXTRA, 0);
		
		mapa = (MapView) findViewById(R.id.mapa);
		mapa.getController().setZoom(17);
		GeoPoint status = new GeoPoint((int) (latitude * 1000000.0), (int) (longitude * 1000000.0));
		mapa.getController().setCenter(status);
		mapa.setBuiltInZoomControls(true);
		
		Drawable marcador = getResources().getDrawable(R.drawable.marcador);
		marcador.setBounds(0, 0, marcador.getIntrinsicWidth(), marcador.getIntrinsicHeight());
		mapa.getOverlays().add(new SobreposicaoRestaurante(marcador, status, getIntent().getStringExtra(NOME_EXTRA)));
	}

	@Override
	protected boolean isRouteDisplayed() {		
		return false;
	}
	
	private class SobreposicaoRestaurante extends ItemizedOverlay<OverlayItem> {
		private OverlayItem item = null;
		
		public SobreposicaoRestaurante(Drawable marcador, GeoPoint ponto, String nome) {
			super(marcador);
			
			boundCenterBottom(marcador);
			item = new OverlayItem(ponto, nome, nome);
			populate();
		}

		@Override
		protected OverlayItem createItem(int arg0) {			
			return item;
		}

		@Override
		public int size() {
			return 1;
		}
		
		@Override
		public boolean onTap(int index) {
			Toast.makeText(MapaRestaurante.this, item.getSnippet(), Toast.LENGTH_SHORT).show();
			return true;
		}
	}
}
