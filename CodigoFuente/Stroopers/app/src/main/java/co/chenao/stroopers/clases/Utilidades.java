package co.chenao.stroopers.clases;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import co.chenao.stroopers.R;
import co.chenao.stroopers.clases.vo.AvatarVo;
import co.chenao.stroopers.clases.vo.JugadorVo;

public class Utilidades {

    //permite tener la referencia del avatar seleccionado para el momento del registro en la BD
    public static AvatarVo avatarSeleccion=null;
    public static int avatarIdSeleccion=0;

    public static ArrayList<AvatarVo> listaAvatars=null;
    public static ArrayList<JugadorVo> listaJugadores=null;

    public static int correctas, incorrectas, puntaje,nivelJuego;

    public static final String NOMBRE_BD="stroopers_bd";
    //Constantes campos tabla usuario
    public static final String TABLA_JUGADOR="jugador";
    public static final String CAMPO_ID="id";
    public static final String CAMPO_NOMBRE="nombre";
    public static final String CAMPO_GENERO="genero";
    public static final String CAMPO_AVATAR="avatar";

    //Constantes campos tabla puntaje
    public static final String TABLA_PUNTAJE="puntaje_jugador";
    public static final String CAMPO_ID_JUGADOR="id";
    public static final String CAMPO_PUNTOS="puntos";
    public static final String CAMPO_PALABRAS_CORRECTAS="correctas";
    public static final String CAMPO_PALABRAS_INCORRECTAS="incorrectas";
    public static final String CAMPO_NIVEL="nivel";
    public static final String CAMPO_MODO_JUEGO="modo";


    public static final String CREAR_TABLA_JUGADOR="CREATE TABLE "+TABLA_JUGADOR+" ("+CAMPO_ID+" INTEGER PRIMARY KEY, "+CAMPO_NOMBRE+" TEXT,"+CAMPO_GENERO+" TEXT,"+CAMPO_AVATAR+" INTEGER)";
    public static final String CREAR_TABLA_PUNTAJES="CREATE TABLE "+TABLA_PUNTAJE+" ("+CAMPO_ID_JUGADOR+" INTEGER, "+CAMPO_PUNTOS+" INTEGER,"+CAMPO_PALABRAS_CORRECTAS+
            " INTEGER,"+CAMPO_PALABRAS_INCORRECTAS+" INTEGER,"+CAMPO_NIVEL+" TEXT,"+CAMPO_MODO_JUEGO+" TEXT)";


    public static void obtenerListaAvatars() {

        //se instancian los avatars y se llena la lista
        listaAvatars=new ArrayList<AvatarVo>();

        listaAvatars.add(new AvatarVo(1,R.drawable.avatar1,"Avatar1"));
        listaAvatars.add(new AvatarVo(2,R.drawable.avatar2,"Avatar2"));
        listaAvatars.add(new AvatarVo(3,R.drawable.avatar3,"Avatar3"));
        listaAvatars.add(new AvatarVo(4,R.drawable.avatar4,"Avatar4"));
        listaAvatars.add(new AvatarVo(5,R.drawable.avatar5,"Avatar5"));
        listaAvatars.add(new AvatarVo(6,R.drawable.avatar6,"Avatar6"));
        listaAvatars.add(new AvatarVo(7,R.drawable.avatar7,"Avatar7"));
        listaAvatars.add(new AvatarVo(8,R.drawable.avatar8,"Avatar8"));
        listaAvatars.add(new AvatarVo(9,R.drawable.avatar9,"Avatar9"));
        listaAvatars.add(new AvatarVo(10,R.drawable.avatar10,"Avatar10"));
        listaAvatars.add(new AvatarVo(11,R.drawable.avatar11,"Avatar11"));
        listaAvatars.add(new AvatarVo(12,R.drawable.avatar12,"Avatar12"));
        listaAvatars.add(new AvatarVo(13,R.drawable.avatar13,"Avatar13"));
        listaAvatars.add(new AvatarVo(14,R.drawable.avatar14,"Avatar14"));
        listaAvatars.add(new AvatarVo(15,R.drawable.avatar15,"Avatar15"));
        listaAvatars.add(new AvatarVo(16,R.drawable.avatar16,"Avatar16"));
        listaAvatars.add(new AvatarVo(17,R.drawable.avatar17,"Avatar17"));
        listaAvatars.add(new AvatarVo(18,R.drawable.avatar18,"Avatar18"));
        listaAvatars.add(new AvatarVo(19,R.drawable.avatar19,"Avatar19"));
        listaAvatars.add(new AvatarVo(20,R.drawable.avatar20,"Avatar20"));
        listaAvatars.add(new AvatarVo(21,R.drawable.avatar21,"Avatar21"));
        listaAvatars.add(new AvatarVo(22,R.drawable.avatar22,"Avatar22"));
        listaAvatars.add(new AvatarVo(23,R.drawable.avatar23,"Avatar23"));
        listaAvatars.add(new AvatarVo(24,R.drawable.avatar24,"Avatar24"));
        listaAvatars.add(new AvatarVo(25,R.drawable.avatar25,"Avatar25"));
        listaAvatars.add(new AvatarVo(26,R.drawable.avatar26,"Avatar26"));
        listaAvatars.add(new AvatarVo(27,R.drawable.avatar27,"Avatar27"));

    }

    public static void consultarListaJugadores(Activity actividad) {
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(actividad,NOMBRE_BD,null,1);
        SQLiteDatabase db=conn.getReadableDatabase();

        JugadorVo jugador=null;
        listaJugadores=new ArrayList<JugadorVo>();
        //select * from usuarios
        Cursor cursor=db.rawQuery("SELECT * FROM "+Utilidades.TABLA_JUGADOR,null);

        while (cursor.moveToNext()){
            jugador=new JugadorVo();
            jugador.setId(cursor.getInt(0));
            jugador.setNombre(cursor.getString(1));
            jugador.setGenero(cursor.getString(2));
            jugador.setAvatar(cursor.getInt(3));

            listaJugadores.add(jugador);
        }

        db.close();
    }
}
