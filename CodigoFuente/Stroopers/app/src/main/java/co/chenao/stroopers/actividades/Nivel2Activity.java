package co.chenao.stroopers.actividades;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import co.chenao.stroopers.R;
import co.chenao.stroopers.clases.PreferenciasJuego;
import co.chenao.stroopers.clases.Utilidades;

public class Nivel2Activity extends AppCompatActivity {

    List<String> listaPalabras = new ArrayList<>();
    List<Integer> listaColores = new ArrayList<>();
    List<Integer> listaColoresTmp = new ArrayList<>();

    LinearLayout barraSuperior;
    TextView txtCantidad, txtCorrectas, txtRestante, txtPalabra,txtCambia,txtPuntaje;
    ProgressBar pTiempo;
    FloatingActionButton btnPause;
    Button btnColor1, btnColor2, btnColor3, btnColor4;
    boolean bandera = true;
    boolean bandera1 = true;
    int finalizaJuego=0;//al momento de finalizar
    int valorBoton;//para definir el valor obtenido
    int colorR;//define el rango de 1 a 4 para asignacion de color
    int palabraR;//define el rango de 1 a 4 para asignacion de palabra del color
    int pausar;//variable para definir la cantidad de veces que queremos pausar
    String modo;//modo de juego
    long tiempo;//tiempo de juego
    long tiempoPalabra;//tiempo de la palabra
    int cantidad, intentos;
    int [] milisegundos = {0,(int)PreferenciasJuego.tiempoJuego};//define el tiempo de juego dependiendo de lo parametrizado

   // private Handler miHandler2=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nivel2);

        SharedPreferences preferences= android.preference.PreferenceManager.getDefaultSharedPreferences(this);
        PreferenciasJuego.obtenerPreferencias(preferences,getApplicationContext());

        txtCantidad = findViewById(R.id.txtCantidad);
        txtCorrectas = findViewById(R.id.txtCorrectas);
        txtRestante = findViewById(R.id.txtRestante);
        txtPalabra = findViewById(R.id.txtPalabra);
        txtCambia = findViewById(R.id.txtCambia);
        txtPuntaje=findViewById(R.id.txtPuntaje);
        pTiempo= findViewById(R.id.pTiempo);
        btnColor1 = findViewById(R.id.btnColor1);
        btnColor2 = findViewById(R.id.btnColor2);
        btnColor3 = findViewById(R.id.btnColor3);
        btnColor4 = findViewById(R.id.btnColor4);
        btnPause = findViewById(R.id.btnPause);

        barraSuperior=findViewById(R.id.barraSuperiorId);
        barraSuperior.setBackgroundColor(getResources().getColor(PreferenciasJuego.colorTema));

        inicializaListas();
        definirBotonesAleatorios();
        inicializaValores();
        asignaValores();
        iniciarJuego();

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pausar();
            }
        });
    }


    private void inicializaListas() {
        listaPalabras = new ArrayList<>();
        listaColores = new ArrayList<>();
        listaPalabras.add("AMARILLO");
        listaColores.add(getResources().getColor(R.color.colorAmarillo));
        listaPalabras.add("AZUL");
        listaColores.add(getResources().getColor(R.color.colorAzul));
        listaPalabras.add("ROJO");
        listaColores.add(getResources().getColor(R.color.colorRojo));
        listaPalabras.add("VERDE");
        listaColores.add(getResources().getColor(R.color.colorVerde));
    }

    //Método para hacer aleatorio la palabra, colores y botones
    private void definirBotonesAleatorios() {
        listaColoresTmp = listaColores;
        Collections.shuffle(listaColoresTmp);
        palabraR = (int) (Math.random() * 4);
        colorR = (int) (Math.random() * 4);

        txtPalabra.setText(listaPalabras.get(palabraR));
        txtPalabra.setTextColor(listaColores.get(colorR));

        btnColor1.setBackgroundColor(listaColoresTmp.get(0));
        btnColor2.setBackgroundColor(listaColoresTmp.get(1));
        btnColor3.setBackgroundColor(listaColoresTmp.get(2));
        btnColor4.setBackgroundColor(listaColoresTmp.get(3));

    }

    //Método para ingresar valores predeterminados
    private void inicializaValores() {
        modo=PreferenciasJuego.modoJuego;
        tiempoPalabra=PreferenciasJuego.duracionPalabra;

        Utilidades.correctas =0;
        Utilidades.incorrectas=0;
        Utilidades.puntaje=0;

        bandera=true;
        bandera1=true;
       // ab=0;

        if (modo.equals("INTENTOS")) {
            intentos = PreferenciasJuego.numIntentos;
            pTiempo.setMax(intentos);//definir tiempo parametrizado
            pTiempo.setProgress(intentos);
        }else {
            intentos=0;
            tiempo=PreferenciasJuego.tiempoJuego;
            //  Toast.makeText(getApplicationContext(),"Tiempo que no deberia mostrarse: "+tiempo,Toast.LENGTH_LONG).show();
            milisegundos[1]= (int) tiempo;
            pTiempo.setMax((int) tiempo);
            pTiempo.setProgress((int) tiempo);
        }


    }

    //Método para ingresar a los TextView la información cantidad de palabras, palabras correctas, intentos restantes
    private void asignaValores() {
        txtCantidad.setText(Integer.toString(cantidad));
        txtCorrectas.setText(Integer.toString(Utilidades.correctas));
        if (modo.equals("INTENTOS")){
            txtCambia.setText("Intentos Faltantes");
            txtRestante.setText(Integer.toString(intentos));
            txtPuntaje.setText(Utilidades.puntaje+"");
        }else {
            txtCambia.setText("Tiempo Faltante");
            txtRestante.setText(Integer.toString((int) tiempo));
            txtPuntaje.setText(Utilidades.puntaje+"");
        }
        //Toast.makeText(getApplicationContext(),"milisegundos[0]="+milisegundos[0]+" con milisegundos[1]="+milisegundos[1],Toast.LENGTH_LONG).show();
    }

    //Método para iniciar el juego
    private void iniciarJuego() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (bandera){
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (bandera1) {
                                if (milisegundos[0] == tiempoPalabra) {
                                    milisegundos[0] = 0;
                                    cantidad++;
                                    intentos--;
                                    Utilidades.incorrectas++;
                                    definirBotonesAleatorios();
                                    asignaValores();
                                    terminarJuego();
                                }
                                milisegundos[0]++;
                                milisegundos[1]--;
                                pTiempo.setProgress(intentos);
                                if (modo.equals("INTENTOS")) {
                                    if (milisegundos[1] == 0) {
                                        milisegundos[1] = 30000;
                                    }
                                }else {
                                    txtRestante.setText(Integer.toString(milisegundos[1]));
                                    pTiempo.setProgress(milisegundos[1]);
                                }

                                terminarJuego();
                            }
                        }
                    });
                }
            }
        });
        thread.start();
    }

    //Método para finalizar el juego
    private void terminarJuego() {
        //
        if (finalizaJuego==0 && ((modo.equals("INTENTOS") && intentos==0) || (modo.equals("TIEMPO") && milisegundos[1]==0))){
            btnColor1.setEnabled(false);
            btnColor2.setEnabled(false);
            btnColor3.setEnabled(false);
            btnColor4.setEnabled(false);
            finalizaJuego=1;
            bandera=false;
            bandera1=false;
            Intent intent = new Intent(Nivel2Activity.this,ResultadoJuegoActivity.class);
            startActivity(intent);
            finish();

        }
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnColor1:
                valorBoton=1;
                validar();
                break;

            case R.id.btnColor2:
                valorBoton=2;
                validar();
                break;

            case R.id.btnColor3:
                valorBoton=3;
                validar();
                break;

            case R.id.btnColor4:
                valorBoton=4;
                validar();
                break;
        }
    }



    //Método para validar la jugada
    private void validar() {
        switch (valorBoton){
            case 1:
                if (colorR==0){
                    Utilidades.correctas++;
                    Utilidades.puntaje+=10;
                }else {
                    Utilidades.incorrectas++;
                    intentos--;
                }
                break;

            case 2:
                if (colorR==1){
                    Utilidades.correctas++;
                    Utilidades.puntaje+=10;
                }else {
                    Utilidades.incorrectas++;
                    intentos--;
                }
                break;


            case 3:
                if (colorR==2){
                    Utilidades.correctas++;
                    Utilidades.puntaje+=10;
                }else {
                    Utilidades.incorrectas++;
                    intentos--;
                }
                break;

            case 4:
                if (colorR==3){
                    Utilidades.correctas++;
                    Utilidades.puntaje+=10;
                }else {
                    Utilidades.incorrectas++;
                    intentos--;
                }
                break;
        }
        cantidad++;
        terminarJuego();
        definirBotonesAleatorios();
        asignaValores();
        milisegundos[0]=0;

    }


    //Método para pausar el juego
    private void pausar() {
        pausar++;
        if (pausar<=2) {
            bandera1=false;
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.item_pause);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
            Button btnContinuar = dialog.findViewById(R.id.btnContinuar);
            btnContinuar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    milisegundos[0]=0;
                    bandera1=true;
                    definirBotonesAleatorios();
                    dialog.cancel();
                }
            });
            dialog.show();
        }else {
            Toast.makeText(this, "No puedes utilizar más pausas, el límite son dos", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bandera=false;
        bandera1=false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        bandera1=false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        bandera1=false;
    }

}
