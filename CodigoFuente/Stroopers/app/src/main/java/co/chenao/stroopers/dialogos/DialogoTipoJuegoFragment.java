package co.chenao.stroopers.dialogos;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import co.chenao.stroopers.MainActivity;
import co.chenao.stroopers.R;
import co.chenao.stroopers.actividades.AjustesActivity;
import co.chenao.stroopers.actividades.Nivel1Activity;
import co.chenao.stroopers.actividades.Nivel2Activity;
import co.chenao.stroopers.clases.PreferenciasJuego;
import co.chenao.stroopers.clases.Utilidades;
import co.chenao.stroopers.interfaces.IComunicaFragments;


public class DialogoTipoJuegoFragment extends DialogFragment {

    Activity actividad;
    IComunicaFragments iComunicaFragments;

    ImageButton btnSalir;
    LinearLayout barraSuperior;
    CardView cardFacil,cardMedio;

    public DialogoTipoJuegoFragment() {
        // Required empty public constructor
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return crearDialogoGestionUsuario();
    }

    private AlertDialog crearDialogoGestionUsuario() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_dialogo_tipo_juego, null);
        builder.setView(v);

        barraSuperior=v.findViewById(R.id.barraSuperiorId);
        barraSuperior.setBackgroundColor(getResources().getColor(PreferenciasJuego.colorTema));

        btnSalir=v.findViewById(R.id.btnSalir);

        cardFacil=v.findViewById(R.id.cardFacil);
        cardMedio=v.findViewById(R.id.cardMedio);

        eventosBotones();

        return builder.create();
    }

    private void eventosBotones() {

        cardFacil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogoInstruccionesNivelFacil().show();
            }
        });

        cardMedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogoInstruccionesNivelMedio().show();
            }
        });

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public AlertDialog dialogoInstruccionesNivelFacil() {
        AlertDialog.Builder builder = new AlertDialog.Builder(actividad);

        builder.setTitle("NIVEL FACIL")
                .setMessage("Presione los botones dependiendo de la palabra y el color presentado, por cada acierto se obtendrá un puntaje")
                .setNegativeButton("CANCELAR",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                .setPositiveButton("JUGAR",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Utilidades.nivelJuego=1;
                                Intent intent=new Intent(actividad, Nivel1Activity.class);
                                startActivity(intent);
                                dismiss();
                            }
                        });

        return builder.create();
    }

    public AlertDialog dialogoInstruccionesNivelMedio() {
        AlertDialog.Builder builder = new AlertDialog.Builder(actividad);

        builder.setTitle("NIVEL MEDIO")
                .setMessage("Se presentarán 4 botones de colores, se da puntaje al presionar el botón que coincida con el color de la palabra. ")
                .setNegativeButton("CANCELAR",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                .setPositiveButton("JUGAR",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Utilidades.nivelJuego=2;
                                Intent intent=new Intent(actividad, Nivel2Activity.class);
                                startActivity(intent);
                                dismiss();
                            }
                        });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.actividad= (Activity) context;
            iComunicaFragments= (IComunicaFragments) this.actividad;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

}
