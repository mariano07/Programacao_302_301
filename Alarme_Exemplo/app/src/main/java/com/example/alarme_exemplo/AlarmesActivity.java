package com.example.alarme_exemplo;

import java.util.Calendar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AlarmesActivity extends Activity implements OnClickListener {

    private Button button1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(this);
    }

    public void onClick(View arg0) {

        // primeiro cria a intenção
        Intent it = new Intent("EXECUTAR_ALARME");
        PendingIntent p = PendingIntent.getBroadcast(AlarmesActivity.this, 0, it, 0);

        // precisamos pegar agora + 10segundos
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.SECOND, 10); // +10 segundos

        // agendar o alarme
        AlarmManager alarme = (AlarmManager) getSystemService(ALARM_SERVICE);
        long time = c.getTimeInMillis();
        alarme.set(AlarmManager.RTC_WAKEUP, time, p);

        // debug:
        Log.i("Alarme", "Alarme agendado!");
    }


    /*protected void onDestroy() {
       super.onDestroy();
       Log.i("Alarme", "Alarme finalizado!");
       // primeiro cria a intenção
       Intent it = new Intent("EXECUTAR_ALARME");
       PendingIntent p = PendingIntent.getBroadcast(AlarmesActivity.this, 0, it, 0);
       // cancela o amarme
       AlarmManager alarme = (AlarmManager) getSystemService(ALARM_SERVICE);
       alarme.cancel(p);
   }*/
}