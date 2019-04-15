package co.chenao.stroopers.clases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ConexionSQLiteHelper extends SQLiteOpenHelper {


    public ConexionSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Utilidades.CREAR_TABLA_JUGADOR);
        sqLiteDatabase.execSQL(Utilidades.CREAR_TABLA_PUNTAJES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int versionAntigua, int versionNueva) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+Utilidades.TABLA_JUGADOR);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+Utilidades.TABLA_PUNTAJE);
        onCreate(sqLiteDatabase);
    }
}
