package co.chenao.stroopers.actividades;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import co.chenao.stroopers.R;
import co.chenao.stroopers.fragments.AjustesFragment;
import co.chenao.stroopers.clases.PreferenciasJuego;

public class AjustesActivity extends AppCompatActivity {

    RelativeLayout layoutFondo;
    ImageButton btnAyuda;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedorAjustes,new AjustesFragment()).commit();

        layoutFondo=findViewById(R.id.idLayoutFondo);
        btnAyuda=findViewById(R.id.btnAyuda);

        btnAyuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
                createSimpleDialog().show();
            }
        });

    }

    private void asignarValoresPreferencias() {
        layoutFondo.setBackgroundColor(getResources().getColor(PreferenciasJuego.colorTema));
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnIcoAtras:
                finish();
                break;
        }
    }

    public AlertDialog createSimpleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AjustesActivity.this);

        builder.setTitle("Ayuda")
                .setMessage("Desde Aquí podrás configurar las opciones del juego así como el tema de la aplicación.")
                .setPositiveButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

        return builder.create();
    }

    //La actividad es visible, por eso llamamos a los valores de las preferencias
    @Override
    protected void onResume() {
        super.onResume();
        asignarValoresPreferencias();
    }

}
