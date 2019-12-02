package net.rafaeltoledo.restaurante;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ReceptorBoot extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		configurarAlarme(context);		
	}
	
	public static void configurarAlarme(Context contexto) {
		AlarmManager gerenciador = (AlarmManager) contexto.getSystemService(Context.ALARM_SERVICE);
		Calendar cal = Calendar.getInstance();
		SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(contexto);
		String horario = preferencias.getString("horario_alarme", "12:00");
		
		cal.set(Calendar.HOUR_OF_DAY, PreferenciaTempo.obterHora(horario));
		cal.set(Calendar.MINUTE, PreferenciaTempo.obterMinuto(horario));
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		if (cal.getTimeInMillis() < System.currentTimeMillis()) {
			cal.add(Calendar.DAY_OF_YEAR, 1);
		}
		
		gerenciador.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
				AlarmManager.INTERVAL_DAY, obterIntentPendente(contexto));
	}
	
	public static void cancelarAlarme(Context contexto) {
		AlarmManager gerenciador = (AlarmManager) contexto.getSystemService(Context.ALARM_SERVICE);
		gerenciador.cancel(obterIntentPendente(contexto));
	}
	
	private static PendingIntent obterIntentPendente(Context contexto) {
		Intent i = new Intent(contexto, ReceptorAlarme.class);
		return PendingIntent.getBroadcast(contexto, 0, i, 0);
	}
}
