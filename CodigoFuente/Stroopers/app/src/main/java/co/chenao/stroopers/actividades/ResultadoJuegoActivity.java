package co.chenao.stroopers.actividades;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import co.chenao.stroopers.R;
import co.chenao.stroopers.clases.ConexionSQLiteHelper;
import co.chenao.stroopers.clases.PreferenciasJuego;
import co.chenao.stroopers.clases.Utilidades;

public class ResultadoJuegoActivity extends AppCompatActivity {

    TextView txtResCorrectas,txtResIncorrectas,txtCorrectas,txtIncorrectas,txtResultados,txtPuntaje,txtResPuntaje;
    FloatingActionButton btnInicio;
    RelativeLayout layoutFondo;
    TextView textNickName;
    ImageView imagenAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_juego);

        txtResultados =findViewById(R.id.txtResultados);
        txtCorrectas =findViewById(R.id.txtPalabrasCorrectas);
        txtIncorrectas=findViewById(R.id.txtPalabrasIncorrectas);
        txtResCorrectas =findViewById(R.id.txtResPalabrasCorrectas);
        txtResIncorrectas=findViewById(R.id.txtResPalabrasIncorrectas);
        txtPuntaje =findViewById(R.id.txtPuntajeTitulo);
        txtResPuntaje=findViewById(R.id.txtPuntaje);

        layoutFondo=findViewById(R.id.idLayoutFondo);
        textNickName=findViewById(R.id.textNickName);
        imagenAvatar=findViewById(R.id.avatarImage);

        txtResCorrectas.setText(Utilidades.correctas+"");
        txtResIncorrectas.setText(Utilidades.incorrectas+"");
        txtResPuntaje.setText(Utilidades.puntaje+"");

        asignarValoresPreferencias();
        registrarResultados();

        btnInicio=findViewById(R.id.btnHome);
        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }

    private void registrarResultados() {
            int jugador=PreferenciasJuego.jugadorId;
            int puntos=Utilidades.puntaje;
            String nivel;
            if (Utilidades.nivelJuego==1){
                nivel="Facil";
            }else{
                nivel="Medio";
            }

            int correctas=Utilidades.correctas;
            int incorrectas=Utilidades.incorrectas;
            String modoJuego=PreferenciasJuego.modoJuego;

            ConexionSQLiteHelper conn=new ConexionSQLiteHelper(getApplicationContext(),Utilidades.NOMBRE_BD,null,1);

            SQLiteDatabase db=conn.getWritableDatabase();

            ContentValues values=new ContentValues();
            values.put(Utilidades.CAMPO_ID_JUGADOR,jugador);
            values.put(Utilidades.CAMPO_PUNTOS,puntos);
            values.put(Utilidades.CAMPO_PALABRAS_CORRECTAS,correctas);
            values.put(Utilidades.CAMPO_PALABRAS_INCORRECTAS,incorrectas);
            values.put(Utilidades.CAMPO_NIVEL,nivel);
            values.put(Utilidades.CAMPO_MODO_JUEGO,modoJuego);

            Long idResultante=db.insert(Utilidades.TABLA_PUNTAJE,Utilidades.CAMPO_ID_JUGADOR,values);

            if(idResultante!=-1){
                //Toast.makeText(actividad,"Id Registro: "+idResultante,Toast.LENGTH_SHORT).show();
             //   Toast.makeText(getApplicationContext(),"El puntaje a sido Registrado con Exito! "+idResultante,Toast.LENGTH_SHORT).show();
            }
              db.close();

        }

    //Permite asignar las preferencias y cambiar el modo y color del banner personalizado
    private void asignarValoresPreferencias() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            layoutFondo.setBackground(getResources().getDrawable(PreferenciasJuego.formaBanner));
        }else{
            layoutFondo.setBackgroundDrawable(getResources().getDrawable(PreferenciasJuego.formaBanner));
        }

        Drawable shape = (Drawable) layoutFondo.getBackground();
        shape.setColorFilter(getResources().getColor(PreferenciasJuego.colorTema), android.graphics.PorterDuff.Mode.SRC);


        if (PreferenciasJuego.avatarId==1){
            imagenAvatar.setImageResource(R.drawable.cara_simio_banner);
        }else{
            imagenAvatar.setImageResource(Utilidades.listaAvatars.get(PreferenciasJuego.avatarId-1).getAvatarId());
        }

        textNickName.setText(PreferenciasJuego.nickName);
        txtCorrectas.setTextColor(getResources().getColor(PreferenciasJuego.colorTema));
        txtIncorrectas.setTextColor(getResources().getColor(PreferenciasJuego.colorTema));
        txtPuntaje.setTextColor(getResources().getColor(PreferenciasJuego.colorTema));
    }
}
