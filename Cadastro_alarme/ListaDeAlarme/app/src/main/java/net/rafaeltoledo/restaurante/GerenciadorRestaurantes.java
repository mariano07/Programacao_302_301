package net.rafaeltoledo.restaurante;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GerenciadorRestaurantes extends SQLiteOpenHelper {
	
	private static final String NOME_BANCO = "restaurantes.db";
	private static final int VERSAO_SCHEMA = 3;

	public GerenciadorRestaurantes(Context context) {
		super(context, NOME_BANCO, null, VERSAO_SCHEMA);		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE restaurantes (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
				" nome TEXT, endereco TEXT, tipo TEXT, anotacoes TEXT, twitter TEXT," +
				" latitude REAL, longitude REAL);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion < 2) {
			db.execSQL("ALTER TABLE restaurantes ADD COLUMN twitter TEXT");
		}
		
		if (oldVersion < 3) {
			db.execSQL("ALTER TABLE restaurantes ADD COLUMN latitude REAL");
			db.execSQL("ALTER TABLE restaurantes ADD COLUMN longitude REAL");
		}
	}
	
	public void inserir(String nome, String endereco, String tipo, String anotacoes, String twitter) {
		ContentValues valores = new ContentValues();

		valores.put("nome", nome);
		valores.put("endereco", endereco);
		valores.put("tipo", tipo);
		valores.put("anotacoes", anotacoes);
		valores.put("twitter", twitter);

		getWritableDatabase().insert("restaurantes", "nome", valores);
	}
	
	public void atualizar(String id, String nome, String endereco, String tipo, String anotacoes, String twitter) {
		ContentValues valores = new ContentValues();
		String[] argumentos = {id};
		
		valores.put("nome", nome);
		valores.put("endereco", endereco);
		valores.put("tipo", tipo);
		valores.put("anotacoes", anotacoes);
		valores.put("twitter", twitter);
		
		getWritableDatabase().update("restaurantes", valores, "_id=?", argumentos);
	}
	
	public void atualizarLocalizacao(String id, double latitude, double longitude) {
		ContentValues cv = new ContentValues();
		String[] args = {id};
		
		cv.put("latitude", latitude);
		cv.put("longitude", longitude);
		
		getWritableDatabase().update("restaurantes", cv, "_ID = ?", args);
	}
	
	public Cursor obterTodos(String ordenacao) {
		return getReadableDatabase().rawQuery("select _id, nome, endereco, tipo, " +
				"anotacoes, twitter, latitude, longitude FROM restaurantes ORDER BY " + 
				ordenacao, null);
	}
	
	public String obterNome(Cursor c) {
		return c.getString(1);
	}

	public String obterEndereco(Cursor c) {
		return c.getString(2);
	}

	public String obterTipo(Cursor c) {
		return c.getString(3);
	}

	public String obterAnotacoes(Cursor c) {
		return c.getString(4);
	}
	
	public String obterTwitter(Cursor c) {
		return c.getString(5);
	}
	
	public double obterLatitude(Cursor c) {
		return c.getDouble(6);
	}
	
	public double obterLongitude(Cursor c) {
		return c.getDouble(7);
	}
	
	public Cursor obterPorId(String id) {
		String[] argumentos = {id};
		
		return getReadableDatabase().rawQuery(
				"SELECT _id, nome, endereco, tipo, anotacoes, twitter, latitude," +
				" longitude FROM restaurantes WHERE _id = ?", argumentos);
	}
}
