package co.chenao.stroopers.actividades;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import co.chenao.stroopers.MainActivity;
import co.chenao.stroopers.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor=preferences.edit();

                int bandera=Integer.parseInt(preferences.getString("bandera","0"));

                if (bandera==1){
                    Intent intent=new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    editor.putString("bandera","1");
                    editor.commit();

                    Intent intent=new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);

                    Intent intent2=new Intent(SplashActivity.this, ContenedorInstruccionesActivity.class);
                    startActivity(intent2);
                }

                finish();
            }
        },3000);

    }
}
