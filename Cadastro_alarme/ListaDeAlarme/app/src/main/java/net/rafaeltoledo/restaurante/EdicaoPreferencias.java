package net.rafaeltoledo.restaurante;

import android.content.ComponentName;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class EdicaoPreferencias extends PreferenceActivity {
	
	SharedPreferences preferencias = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.preferencias);
	}
	
	@Override
	protected void onResume() {		
		super.onResume();
		
		preferencias = PreferenceManager.getDefaultSharedPreferences(this);
		preferencias.registerOnSharedPreferenceChangeListener(onChange);
	}
	
	@Override
	protected void onPause() {
		preferencias.unregisterOnSharedPreferenceChangeListener(onChange);
		
		super.onPause();
	}
	
	OnSharedPreferenceChangeListener onChange = new SharedPreferences.OnSharedPreferenceChangeListener() {
		
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
				String key) {
			if ("alarme".equals(key)) {
				boolean habilitado = preferencias.getBoolean(key, false);
				int flag = (habilitado ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED);
				ComponentName componente = new ComponentName(EdicaoPreferencias.this, ReceptorBoot.class);
				
				getPackageManager().setComponentEnabledSetting(componente, flag, PackageManager.DONT_KILL_APP);
				
				if (habilitado) {
					ReceptorBoot.configurarAlarme(EdicaoPreferencias.this);
				} else {
					ReceptorBoot.cancelarAlarme(EdicaoPreferencias.this);
				}
			} else if ("horario_alarme".equals(key)) {
				ReceptorBoot.cancelarAlarme(EdicaoPreferencias.this);
				ReceptorBoot.configurarAlarme(EdicaoPreferencias.this);
			}
		}
	};
}
