package net.rafaeltoledo.restaurante;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ListaRestaurantes extends ListActivity {

	Cursor listaRestaurantes = null;
	AdaptadorRestaurante adaptador = null;
	public final static String _ID = "net.rafaeltoledo.restaurante._ID";	
	GerenciadorRestaurantes gerenciador;
	SharedPreferences prefs = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		gerenciador = new GerenciadorRestaurantes(this);
		inicializarLista();
		prefs.registerOnSharedPreferenceChangeListener(prefListener);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		gerenciador.close();
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Intent i = new Intent(ListaRestaurantes.this, FormularioDetalhes.class);
		i.putExtra(_ID, String.valueOf(id));
		startActivity(i);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.opcao, menu);
		
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.adicionar) {
			startActivity(new Intent(ListaRestaurantes.this, FormularioDetalhes.class));			
			return true;
		} else if (item.getItemId() == R.id.prefs) {
			startActivity(new Intent(this, EdicaoPreferencias.class));
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private void inicializarLista() {
		if (listaRestaurantes != null) {
			stopManagingCursor(listaRestaurantes);
			listaRestaurantes.close();
		}
		
		listaRestaurantes = gerenciador.obterTodos(prefs.getString("listagem", "nome"));
		startManagingCursor(listaRestaurantes);
		adaptador = new AdaptadorRestaurante(listaRestaurantes);
		setListAdapter(adaptador);
	}
	
	private OnSharedPreferenceChangeListener prefListener = new OnSharedPreferenceChangeListener() {
		
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
				String key) {			
			if (key.equals("listagem")) {
				inicializarLista();
			}
		}
	};

	class AdaptadorRestaurante extends CursorAdapter {
		AdaptadorRestaurante(Cursor c) {
			super(ListaRestaurantes.this, c);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			ArmazenadorRestaurante armazenador = (ArmazenadorRestaurante) view.getTag();
			armazenador.popularFormulario(cursor, gerenciador);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			LayoutInflater inflater = getLayoutInflater();
			View linha = inflater.inflate(R.layout.linha, parent, false);
			ArmazenadorRestaurante armazenador = new ArmazenadorRestaurante(linha);
			linha.setTag(armazenador);
			return linha;
		}
	}

	static class ArmazenadorRestaurante {
		private TextView nome = null;
		private TextView endereco = null;
		private ImageView icone = null;

		ArmazenadorRestaurante(View linha) {
			nome = (TextView) linha.findViewById(R.id.titulo);
			endereco = (TextView) linha.findViewById(R.id.endereco);
			icone = (ImageView) linha.findViewById(R.id.icone);
		}

		void popularFormulario(Cursor c, GerenciadorRestaurantes gerenciador) {
			nome.setText(gerenciador.obterNome(c));
			endereco.setText(gerenciador.obterEndereco(c));

			if (gerenciador.obterTipo(c).equals("rodizio")) {
				icone.setImageResource(R.drawable.rodizio);
			} else if (gerenciador.obterTipo(c).equals("fast_food")) {
				icone.setImageResource(R.drawable.fast_food);
			} else {
				icone.setImageResource(R.drawable.entrega);
			}
		}
	}
}