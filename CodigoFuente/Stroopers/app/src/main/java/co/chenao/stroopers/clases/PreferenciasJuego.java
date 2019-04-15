package co.chenao.stroopers.clases;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;
import co.chenao.stroopers.R;

public class PreferenciasJuego {

    public static final String MODO_JUEGO="modoJuego";
    public static final String TIEMPO_JUEGO="tiempoJuego";
    public static final String DURACION_PALABRA="duracionPalabra";
    public static final String NUM_INTENTOS="NumeroDeIntentos";
    public static final String COLOR_TEMA="colorTema";
    public static final String FORMA_BANNER="formaBanner";
    public static final String JUGADOR_ID="jugadorId";
    public static final String NICKNAME="nickName";
    public static final String AVATAR_ID="avatarId";

    public static String modoJuego;
    public static long tiempoJuego,duracionPalabra;
    public static int numIntentos;
    public static int colorTema;
    public static int formaBanner;

    public static int jugadorId;
    public static String nickName;
    public static int avatarId;

    public static void asignarPreferenciasJugador(SharedPreferences preferences, Context applicationContext){

        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(JUGADOR_ID,""+jugadorId);
        editor.putString(NICKNAME,""+nickName);
        editor.putString(AVATAR_ID,""+avatarId);
        editor.commit();

        obtenerPreferencias(preferences,applicationContext);
    }

    public static void asignarPreferenciasTema(SharedPreferences preferences, Context applicationContext){

        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(COLOR_TEMA,""+colorTema);
        editor.putString(FORMA_BANNER,""+formaBanner);
        editor.commit();

        obtenerPreferencias(preferences,applicationContext);
    }

    public static void obtenerPreferencias(SharedPreferences preferences, Context applicationContext) {
        String msjError="ok";

        //Cuando no se han asignado preferencias entonces se asigna por defecto los diferentes valores
        modoJuego=preferences.getString(MODO_JUEGO,"TIEMPO");

        String tiempo=preferences.getString(TIEMPO_JUEGO,"1 Minuto");

        if (tiempo.equals("1 Minuto")){
            tiempoJuego=10000;
        }else if (tiempo.equals("2 Minutos")){
            tiempoJuego=20000;
        }else if (tiempo.equals("3 Minutos")){
            tiempoJuego=30000;
        }else if (tiempo.equals("4 Minutos")){
            tiempoJuego=40000;
        }else if (tiempo.equals("5 Minutos")){
            tiempoJuego=50000;
        }

        String duracion=preferences.getString(DURACION_PALABRA,"1/2 Segundos");

        if (duracion.equals("1/2 Segundos")){
            duracionPalabra=500;
        }else if (duracion.equals("1 Segundo")){
            duracionPalabra=1000;
        }else if (duracion.equals("2 Segundos")){
            duracionPalabra=2000;
        }

        try {
            numIntentos=Integer.parseInt(preferences.getString(NUM_INTENTOS,"5"));
        }catch (NumberFormatException e){
            msjError="NUMERO DE INTENTOS";
        }

        try {
            colorTema=Integer.parseInt(preferences.getString(COLOR_TEMA,""+R.color.colorNegro));
        }catch (NumberFormatException e){
            msjError="COLOR TEMA";
        }

        try {
            formaBanner=Integer.parseInt(preferences.getString(FORMA_BANNER,""+R.drawable.banner_redondo));
        }catch (NumberFormatException e){
            msjError="FORMA BANNER";
        }

        if (!msjError.equals("ok")){
            Toast.makeText(applicationContext,"Verifique la configuración en "+msjError+", debe ser un valor en milisegundos",Toast.LENGTH_LONG).show();
        }

        jugadorId=Integer.parseInt(preferences.getString(JUGADOR_ID,"0"));
        nickName=preferences.getString(NICKNAME,"NA");
        avatarId=Integer.parseInt(preferences.getString(AVATAR_ID,"1"));

        String cad="Modo Juego: "+modoJuego+"\n";
        cad+="Tiempo Juego: "+tiempoJuego+"\n";
        cad+="Duracion Palabra: "+duracionPalabra+"\n";
        cad+="Num Intentos: "+numIntentos+"\n";
        cad+="Color Tema: "+colorTema+"\n";
        cad+="Forma Banner: "+formaBanner+"\n";
        cad+="NickName: "+nickName+"\n";
        cad+="AvatarId: "+avatarId+"\n";

       // Toast.makeText(applicationContext,""+cad,Toast.LENGTH_SHORT).show();

    }

}
