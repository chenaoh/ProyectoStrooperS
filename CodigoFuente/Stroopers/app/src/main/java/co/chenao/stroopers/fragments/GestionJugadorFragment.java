package co.chenao.stroopers.fragments;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;

import co.chenao.stroopers.MainActivity;
import co.chenao.stroopers.R;
import co.chenao.stroopers.adapters.AdaptadorAvatar;
import co.chenao.stroopers.adapters.AdaptadorJugador;
import co.chenao.stroopers.clases.ConexionSQLiteHelper;
import co.chenao.stroopers.clases.PreferenciasJuego;
import co.chenao.stroopers.clases.Utilidades;
import co.chenao.stroopers.clases.vo.AvatarVo;
import co.chenao.stroopers.clases.vo.JugadorVo;
import co.chenao.stroopers.interfaces.IComunicaFragments;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GestionJugadorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GestionJugadorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GestionJugadorFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    int eventoEliminar=0;
    Activity actividad;
    View vista;
    IComunicaFragments iComunicaFragments;
    RelativeLayout layoutFondo;

    RecyclerView recyclerAvatars,recyclerJugadores;

    ImageButton btnAtras,btnAyuda;
    TextView barraSeleccion,separador;
    FloatingActionsMenu grupoBotones;
    FloatingActionButton fabActualizar,fabEliminar,fabConfirmar;
    EditText campoNick;
    RadioButton radioM,radioF;

    JugadorVo jugadorSeleccionado;

    public GestionJugadorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GestionJugadorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GestionJugadorFragment newInstance(String param1, String param2) {
        GestionJugadorFragment fragment = new GestionJugadorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vista=inflater.inflate(R.layout.fragment_gestion_jugador, container, false);

        recyclerAvatars =vista.findViewById(R.id.recyclerAvatarsId);
        recyclerJugadores =vista.findViewById(R.id.recyclerJugadoresId);

        layoutFondo=vista.findViewById(R.id.idLayoutFondo);
        layoutFondo.setBackgroundColor(getResources().getColor(PreferenciasJuego.colorTema));
        separador=vista.findViewById(R.id.separadorId);
        separador.setBackgroundColor(getResources().getColor(PreferenciasJuego.colorTema));

        btnAtras=vista.findViewById(R.id.btnIcoAtras);
        grupoBotones=vista.findViewById(R.id.grupoFab);
        fabActualizar=vista.findViewById(R.id.idFabActualizar);
        fabActualizar.setColorNormal(getResources().getColor(PreferenciasJuego.colorTema));
        fabActualizar.setColorPressed(getResources().getColor(PreferenciasJuego.colorTema));
        fabEliminar=vista.findViewById(R.id.idFabEliminar);
        fabEliminar.setColorNormal(getResources().getColor(PreferenciasJuego.colorTema));
        fabEliminar.setColorPressed(getResources().getColor(PreferenciasJuego.colorTema));
        fabConfirmar=vista.findViewById(R.id.idFabConfirmar);
        fabConfirmar.setColorNormal(getResources().getColor(PreferenciasJuego.colorTema));
        fabConfirmar.setColorPressed(getResources().getColor(PreferenciasJuego.colorTema));

        campoNick=vista.findViewById(R.id.campoNickName);
        radioM=vista.findViewById(R.id.radioM);
        radioF=vista.findViewById(R.id.radioF);
        barraSeleccion=vista.findViewById(R.id.barraSeleccionId);


        recyclerJugadores.setLayoutManager(new LinearLayoutManager(this.actividad));
        recyclerJugadores.setHasFixedSize(true);

        recyclerAvatars.setLayoutManager(new GridLayoutManager(this.actividad,3));
        recyclerAvatars.setHasFixedSize(true);


        btnAyuda=vista.findViewById(R.id.btnAyuda);
        btnAyuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
                createSimpleDialog().show();
            }
        });

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (eventoEliminar==0){
                    iComunicaFragments.mostrarMenu();
                }else{
                    Toast.makeText(actividad,"Seleccione un Jugador",Toast.LENGTH_SHORT).show();
                }

            }
        });

        fabActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                actualizarJugador();
                llenarAdaptadorJugadores();
                grupoBotones.collapse();
            }
        });

        fabEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (campoNick.getText().toString()!=null && !campoNick.getText().toString().trim().equals("")){
                    dialogoEliminar().show();
                    grupoBotones.collapse();
                }else{
                    Toast.makeText(actividad,"Debe seleccionar un Jugador para poder eliminarlo",Toast.LENGTH_SHORT).show();
                }
            }
        });

        fabConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (campoNick.getText().toString()!=null && !campoNick.getText().toString().trim().equals("")){
                    PreferenciasJuego.nickName="{ "+campoNick.getText().toString()+" }";
                    PreferenciasJuego.avatarId=Utilidades.avatarSeleccion.getId();
                    SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(actividad);
                    PreferenciasJuego.asignarPreferenciasJugador(preferences,actividad);
                    grupoBotones.collapse();
                    eventoEliminar=0;
                    iComunicaFragments.mostrarMenu();
                }else{
                    Toast.makeText(actividad,"Verifique los datos para realizar la selección",Toast.LENGTH_SHORT).show();
                }

            }
        });

        llenarAdaptadorJugadores();
        llenarAdaptadorAvatars(0);


        return vista;

    }


    public AlertDialog createSimpleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(actividad);

        builder.setTitle("Ayuda")
                .setMessage("Elija el jugador de la lista de Jugadores disponible, si lo desea puede modificar su información (Nick, Genero, Avatar) y posteriormente" +
                        "desde el menú de botones flotantes podrá actualizarlo, eliminarlo o seleccionarlo para Jugar.")
                .setPositiveButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

        return builder.create();
    }

    private void llenarAdaptadorJugadores() {

        //se asigna la lista de jugadores por defecto
        final AdaptadorJugador miAdaptadorJugadores=new AdaptadorJugador(Utilidades.listaJugadores);
        miAdaptadorJugadores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                grupoBotones.collapse();
                jugadorSeleccionado=Utilidades.listaJugadores.get(recyclerJugadores.getChildAdapterPosition(view));
                campoNick.setText(jugadorSeleccionado.getNombre());
                if(jugadorSeleccionado.getGenero().equals("M")){
                    radioM.setChecked(true);
                }else{
                    radioF.setChecked(true);
                }

                PreferenciasJuego.jugadorId=jugadorSeleccionado.getId();
                Utilidades.avatarSeleccion=Utilidades.listaAvatars.get(jugadorSeleccionado.getAvatar()-1);

                llenarAdaptadorAvatars(jugadorSeleccionado.getAvatar());

            }

        });

        recyclerJugadores.setAdapter(miAdaptadorJugadores);
    }

    private void llenarAdaptadorAvatars(int avatarId) {
      // Toast.makeText(actividad,"reinicia adapter ",Toast.LENGTH_SHORT).show();
        Utilidades.avatarIdSeleccion=avatarId;
        //se asigna la lista de avatars por defecto
        final AdaptadorAvatar miAdaptadorAvatars=new AdaptadorAvatar(Utilidades.listaAvatars);
        recyclerAvatars.scrollToPosition(avatarId-1);//asigno la posicion del elemento seleccionado -1 ya que en la bd guarda el elemento real pero en la lista inicia en 0


        recyclerAvatars.setAdapter(miAdaptadorAvatars);
    }

    private void actualizarJugador() {

        String genero="";

        if (radioM.isChecked()==true){
            genero="M";
        }else if(radioF.isChecked()==true){
            genero="F";
        }else{
            genero="No seleccionado";
        }

        if (!genero.equals("No seleccionado") && campoNick.getText().toString()!=null && !campoNick.getText().toString().trim().equals("")){
            String nickName=campoNick.getText().toString();
            int avatarId=Utilidades.avatarSeleccion.getId();

            ConexionSQLiteHelper conn=new ConexionSQLiteHelper(actividad,Utilidades.NOMBRE_BD,null,1);

            SQLiteDatabase db=conn.getWritableDatabase();

            ContentValues values=new ContentValues();
            values.put(Utilidades.CAMPO_NOMBRE,nickName);
            values.put(Utilidades.CAMPO_GENERO,genero);
            values.put(Utilidades.CAMPO_AVATAR,avatarId);

            int idResultante=db.update(Utilidades.TABLA_JUGADOR, values, Utilidades.CAMPO_ID+"="+jugadorSeleccionado.getId(), null);

            if(idResultante!=-1){
                Toast.makeText(actividad,"El Jugador a sido Actualizado con Exito!",Toast.LENGTH_SHORT).show();
                recyclerJugadores.scrollToPosition(jugadorSeleccionado.getId()-1);//asigno la posicion del elemento seleccionado -1 ya que en la bd guarda el elemento real pero en la lista inicia en 0
                Utilidades.consultarListaJugadores(actividad);
            }else
                Toast.makeText(actividad,"No se pudo Actualizado el Jugador! ",Toast.LENGTH_SHORT).show();

            db.close();

        }else{
            Toast.makeText(actividad,"Verifique los datos para realizar la actualización",Toast.LENGTH_SHORT).show();
        }
    }
// Antes de validar la eliminación, verificar porqué despues de seleccionado el elemento ya no deja seleccionar otro con el cambio de barra
    private void eliminarJugador() {

        if (campoNick.getText().toString()!=null && !campoNick.getText().toString().trim().equals("")){
            ConexionSQLiteHelper conn=new ConexionSQLiteHelper(actividad,Utilidades.NOMBRE_BD,null,1);

            SQLiteDatabase db=conn.getWritableDatabase();


            int idResultante= db.delete(Utilidades.TABLA_JUGADOR, Utilidades.CAMPO_ID+"="+jugadorSeleccionado.getId(), null);

            if(idResultante!=-1){
                //Toast.makeText(actividad,"Id Registro: "+idResultante,Toast.LENGTH_SHORT).show();
                Toast.makeText(actividad,"El Jugador a sido Eliminado con Exito!",Toast.LENGTH_SHORT).show();
                campoNick.setText("");
                radioF.setChecked(false);
                radioM.setChecked(false);
                recyclerJugadores.scrollToPosition(jugadorSeleccionado.getId());
                recyclerAvatars.scrollToPosition(0);
                Utilidades.consultarListaJugadores(actividad);

                PreferenciasJuego.nickName="NA";
                PreferenciasJuego.avatarId=1;
                SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(actividad);
                PreferenciasJuego.asignarPreferenciasJugador(preferences,actividad);

                //se eliminan los puntajes
                int idResultante2= db.delete(Utilidades.TABLA_PUNTAJE, Utilidades.CAMPO_ID_JUGADOR+"="+jugadorSeleccionado.getId(), null);
                if(idResultante2==-1){
                    Toast.makeText(actividad,"No se pudieron eliminar los puntajes!",Toast.LENGTH_SHORT).show();
                }
                eventoEliminar=1;

            }else
                Toast.makeText(actividad,"No se pudo Eliminar el Jugador! ",Toast.LENGTH_SHORT).show();


            db.close();
        }else{
            Toast.makeText(actividad,"Debe seleccionar un Jugador para poder eliminarlo",Toast.LENGTH_SHORT).show();
        }
    }


    public AlertDialog dialogoEliminar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(actividad);

        builder.setTitle("Advertencia!!!")
                .setMessage("¿Está seguro que desea eliminar a "+jugadorSeleccionado.getNombre().toUpperCase()+" y toda su información?")
                .setNegativeButton("CANCELAR",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                .setPositiveButton("ACEPTAR",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                eliminarJugador();
                                llenarAdaptadorJugadores();
                            }
                        });

        return builder.create();
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.actividad= (Activity) context;
            iComunicaFragments= (IComunicaFragments) this.actividad;
        }
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
