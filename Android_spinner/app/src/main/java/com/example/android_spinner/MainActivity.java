package com.example.android_spinner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Object AdapterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Spinner Elemento
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        //Spinner Click Lista
        spinner.setOnItemClickListener((android.widget.AdapterView.OnItemClickListener) this);

        //Spinner Drop Elementos
        List<String> categorias = new ArrayList<String>();
        categorias.add("Automovel");
        categorias.add("Serviços Empresariais");
        categorias.add("Computadores");
        categorias.add("Cursos Tecnicos");
        categorias.add("Viagem");
        categorias.add("Turismo");

        //Criando as Seleções
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,categorias);

        //Criando Down Layout Style
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Pegando as informações
        spinner.setAdapter(dataAdapter);

        public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
        //Item Selecionado
        String item = parent.getItemAtPosition(position).toString();

        // Listando itens
        Toast.makeText(parent.getContext(), "Selecionado: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0){
        // TODO Auto-generated method stub
    }


    }
}
