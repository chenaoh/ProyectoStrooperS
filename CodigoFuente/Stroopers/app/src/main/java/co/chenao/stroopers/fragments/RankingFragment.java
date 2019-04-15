package co.chenao.stroopers.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import co.chenao.stroopers.R;
import co.chenao.stroopers.actividades.AjustesTemaActivity;
import co.chenao.stroopers.adapters.AdaptadorJugador;
import co.chenao.stroopers.adapters.AdaptadorResultados;
import co.chenao.stroopers.clases.ConexionSQLiteHelper;
import co.chenao.stroopers.clases.PreferenciasJuego;
import co.chenao.stroopers.clases.Utilidades;
import co.chenao.stroopers.clases.vo.JugadorVo;
import co.chenao.stroopers.clases.vo.ResultadosVo;
import co.chenao.stroopers.interfaces.IComunicaFragments;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RankingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RankingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RankingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    Activity actividad;
    View vista;
    IComunicaFragments iComunicaFragments;
    RelativeLayout layoutFondo;

    Spinner comboTipoResultados,comboNivel,comboModo;
    RecyclerView recyclerJugadores;
    JugadorVo jugadorSeleccionado;
    private static ArrayList<ResultadosVo> listaResultados;

    ImageButton btnAtras,btnAyuda;
    TextView separador,txtMensaje;

    FloatingActionButton btnBuscar;


    public RankingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RankingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RankingFragment newInstance(String param1, String param2) {
        RankingFragment fragment = new RankingFragment();
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
        // Inflate the layout for this fragment
        vista=inflater.inflate(R.layout.fragment_ranking, container, false);

        layoutFondo=vista.findViewById(R.id.idLayoutFondo);
        layoutFondo.setBackgroundColor(getResources().getColor(PreferenciasJuego.colorTema));

        comboTipoResultados=vista.findViewById(R.id.comboTipoResultados);
        comboNivel=vista.findViewById(R.id.comboNivel);
        txtMensaje=vista.findViewById(R.id.txtSinDatos);

        ArrayList<String> listaNivel=new ArrayList<>();
        listaNivel.add("Todos");
        listaNivel.add("Facil");
        listaNivel.add("Medio");

        comboModo=vista.findViewById(R.id.comboModo);
        ArrayList<String> listaModo=new ArrayList<>();
        listaModo.add("Todos");
        listaModo.add("Intentos");
        listaModo.add("Tiempo");

        ArrayAdapter<CharSequence> adapterTipoRes=ArrayAdapter.createFromResource(actividad,R.array.combo_resultados,android.R.layout.simple_spinner_item);
        comboTipoResultados.setAdapter(adapterTipoRes);

        ArrayAdapter<CharSequence> adapterNivel= new ArrayAdapter(actividad,android.R.layout.simple_spinner_item,listaNivel);
        comboNivel.setAdapter(adapterNivel);

        ArrayAdapter<CharSequence> adapterModo=new ArrayAdapter(actividad,android.R.layout.simple_spinner_item,listaModo);
        comboModo.setAdapter(adapterModo);

        recyclerJugadores =vista.findViewById(R.id.recyclerJugadoresId);
        recyclerJugadores.setLayoutManager(new LinearLayoutManager(this.actividad));
        recyclerJugadores.setHasFixedSize(true);

        separador=vista.findViewById(R.id.separadorId);
        separador.setBackgroundColor(getResources().getColor(PreferenciasJuego.colorTema));
        btnAtras=vista.findViewById(R.id.btnIcoAtras);
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
                iComunicaFragments.mostrarMenu();
            }
        });
        //se hace una consulta inicial
        consultarResultados("select j.id,j.nombre,j.genero,j.avatar,p.puntos,p.nivel,p.modo from "+Utilidades.TABLA_JUGADOR+" j,"+Utilidades.TABLA_PUNTAJE+" p where j.id=p.id and j.id="+PreferenciasJuego.jugadorId);

        btnBuscar=vista.findViewById(R.id.idFabConsultar);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int jugadorId=PreferenciasJuego.jugadorId;
                String tipoConsulta=comboTipoResultados.getSelectedItem().toString();
                String nivelJuego=comboNivel.getSelectedItem().toString();
                String modo=comboModo.getSelectedItem().toString();
                String consulta="";
                if(!tipoConsulta.equals("Individual")){
                    if(nivelJuego.equalsIgnoreCase("Todos") && modo.equalsIgnoreCase("Todos")){
                        consulta="select j.id,j.nombre,j.genero,j.avatar,p.puntos,p.nivel,p.modo from "+Utilidades.TABLA_JUGADOR+" j,"+Utilidades.TABLA_PUNTAJE+" p where j.id=p.id";
                    }else if(!nivelJuego.equalsIgnoreCase("Todos") && modo.equalsIgnoreCase("Todos")){
                        consulta="select j.id,j.nombre,j.genero,j.avatar,p.puntos,p.nivel,p.modo from "+Utilidades.TABLA_JUGADOR+" j,"+Utilidades.TABLA_PUNTAJE+" p where j.id=p.id and p.nivel='"+nivelJuego+"'";
                    }else if(nivelJuego.equalsIgnoreCase("Todos") && !modo.equalsIgnoreCase("Todos")){
                        consulta="select j.id,j.nombre,j.genero,j.avatar,p.puntos,p.nivel,p.modo from "+Utilidades.TABLA_JUGADOR+" j,"+Utilidades.TABLA_PUNTAJE+" p where j.id=p.id and p.modo='"+modo.toUpperCase().trim()+"'";
                    }else if(!nivelJuego.equalsIgnoreCase("Todos") && !modo.equalsIgnoreCase("Todos")){
                        consulta="select j.id,j.nombre,j.genero,j.avatar,p.puntos,p.nivel,p.modo from "+Utilidades.TABLA_JUGADOR+" j,"+Utilidades.TABLA_PUNTAJE+" p where j.id=p.id and p.nivel='"+nivelJuego+"' and p.modo='"+modo.toUpperCase().trim()+"'";
                    }
                }else{
                    if(nivelJuego.equalsIgnoreCase("Todos") && modo.equalsIgnoreCase("Todos")){
                        consulta="select j.id,j.nombre,j.genero,j.avatar,p.puntos,p.nivel,p.modo from "+Utilidades.TABLA_JUGADOR+" j,"+Utilidades.TABLA_PUNTAJE+" p where j.id=p.id and j.id="+jugadorId;
                    }else if(!nivelJuego.equalsIgnoreCase("Todos") && modo.equalsIgnoreCase("Todos")){
                        consulta="select j.id,j.nombre,j.genero,j.avatar,p.puntos,p.nivel,p.modo from "+Utilidades.TABLA_JUGADOR+" j,"+Utilidades.TABLA_PUNTAJE+" p where j.id=p.id and j.id="+jugadorId+" and p.nivel='"+nivelJuego+"'";
                    }else if(nivelJuego.equalsIgnoreCase("Todos") && !modo.equalsIgnoreCase("Todos")){
                        consulta="select j.id,j.nombre,j.genero,j.avatar,p.puntos,p.nivel,p.modo from "+Utilidades.TABLA_JUGADOR+" j,"+Utilidades.TABLA_PUNTAJE+" p where j.id=p.id and j.id="+jugadorId+" and p.modo='"+modo.toUpperCase().trim()+"'";
                    }else if(!nivelJuego.equalsIgnoreCase("Todos") && !modo.equalsIgnoreCase("Todos")){
                        consulta="select j.id,j.nombre,j.genero,j.avatar,p.puntos,p.nivel,p.modo from "+Utilidades.TABLA_JUGADOR+" j,"+Utilidades.TABLA_PUNTAJE+" p where j.id=p.id and j.id="+jugadorId+" and p.nivel='"+nivelJuego+"' and p.modo='"+modo.toUpperCase().trim()+"'";
                    }
                }

                consultarResultados(consulta);
            }
        });



        return vista;
    }

    public AlertDialog createSimpleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(actividad);

        builder.setTitle("Ayuda")
                .setMessage("Defina los parámetros de consulta y presione el botón para conocer el Ranking de Stroopers")
                .setPositiveButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

        return builder.create();
    }

    private void consultarResultados(String consulta) {
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(actividad,Utilidades.NOMBRE_BD,null,1);
        SQLiteDatabase db=conn.getReadableDatabase();

        ResultadosVo resultados=null;
        listaResultados=new ArrayList<ResultadosVo>();
        //select * from usuarios
        Cursor cursor=db.rawQuery(consulta,null);

        while (cursor.moveToNext()){
            resultados=new ResultadosVo();
            resultados.setId(cursor.getInt(0));
            resultados.setNombre(cursor.getString(1));
            resultados.setGenero(cursor.getString(2));
            resultados.setAvatar(cursor.getInt(3));
            resultados.setPuntos(cursor.getInt(4));
            resultados.setNivel(cursor.getString(5));
            resultados.setModo(cursor.getString(6));

            listaResultados.add(resultados);
        }

        if (listaResultados.size()>0){
            txtMensaje.setText("Se encontraron "+listaResultados.size()+" resultados");
        }else{
            txtMensaje.setText("No hay datos asociados!");
        }

        db.close();
        llenarAdaptadorJugadores();
    }


    private void consultarResultadosIndividuales(int jugadorId, String nivelJuego, String modo) {

        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(actividad,Utilidades.NOMBRE_BD,null,1);
        SQLiteDatabase db=conn.getReadableDatabase();

        ResultadosVo resultados=null;
        listaResultados=new ArrayList<ResultadosVo>();
        //select * from usuarios
        String consula="select j.id,j.nombre,j.telefono,j.avatar,p.puntos,p.nivel,p.modo from "+Utilidades.TABLA_JUGADOR+" j,"+Utilidades.TABLA_PUNTAJE+" p where j.id=p.id and j.id="+jugadorId+" and p.nivel='"+nivelJuego+"' and p.modo='"+modo+"'";
        Cursor cursor=db.rawQuery(consula,null);

        while (cursor.moveToNext()){
            resultados=new ResultadosVo();
            resultados.setId(cursor.getInt(0));
            resultados.setNombre(cursor.getString(1));
            resultados.setGenero(cursor.getString(2));
            resultados.setAvatar(cursor.getInt(3));
            resultados.setPuntos(cursor.getInt(4));
            resultados.setNivel(cursor.getString(5));
            resultados.setModo(cursor.getString(6));

            listaResultados.add(resultados);

        }

        db.close();

    }

    private void consultarResultadosGenerales(String nivelJuego, String modo) {
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(actividad,Utilidades.NOMBRE_BD,null,1);
        SQLiteDatabase db=conn.getReadableDatabase();

        ResultadosVo resultados=null;
        listaResultados=new ArrayList<ResultadosVo>();
        //select * from usuarios
        String consulta="select j.id,j.nombre,j.telefono,j.avatar,p.puntos,p.nivel,p.modo from "+Utilidades.TABLA_JUGADOR+" j,"+Utilidades.TABLA_PUNTAJE+" p where j.id=p.id ";
        Cursor cursor=db.rawQuery(consulta,null);

        while (cursor.moveToNext()){
            resultados=new ResultadosVo();
            resultados.setId(cursor.getInt(0));
            resultados.setNombre(cursor.getString(1));
            resultados.setGenero(cursor.getString(2));
            resultados.setAvatar(cursor.getInt(3));
            resultados.setPuntos(cursor.getInt(4));
            resultados.setNivel(cursor.getString(5));
            resultados.setModo(cursor.getString(6));

            listaResultados.add(resultados);
        }

        db.close();
    }


    private void llenarAdaptadorJugadores() {

        //se asigna la lista de jugadores por defecto
        final AdaptadorResultados miAdaptadorJugadores=new AdaptadorResultados(listaResultados);
        miAdaptadorJugadores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //    Toast.makeText(actividad,"Avatar "+Utilidades.listaJugadores.get(recyclerJugadores.getChildAdapterPosition(view)).getNombre(),Toast.LENGTH_SHORT).show();
                jugadorSeleccionado=Utilidades.listaJugadores.get(recyclerJugadores.getChildAdapterPosition(view));
            }

        });

        recyclerJugadores.setAdapter(miAdaptadorJugadores);
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
