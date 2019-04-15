package co.chenao.stroopers.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import co.chenao.stroopers.R;
import co.chenao.stroopers.clases.Utilidades;
import co.chenao.stroopers.clases.vo.JugadorVo;
import co.chenao.stroopers.clases.vo.ResultadosVo;

public class AdaptadorResultados extends RecyclerView.Adapter<AdaptadorResultados.ViewHolderJugador> implements View.OnClickListener {

    private View.OnClickListener listener;
    List<ResultadosVo> listaJugador;
    View vista;

    public AdaptadorResultados(List<ResultadosVo> listaJugador) {
        this.listaJugador = listaJugador;
    }

    @NonNull
    @Override
    public ViewHolderJugador onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        vista=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_resultados,viewGroup,false);
        vista.setOnClickListener(this);
        return new ViewHolderJugador(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderJugador viewHolderJugador, int i) {


        //se resta uno ya que buscamos la lista de elementos que inicia en la pos 0
        viewHolderJugador.imgAvatar.setImageResource(Utilidades.listaAvatars.get(listaJugador.get(i).getAvatar()-1).getAvatarId());
        viewHolderJugador.txtNombre.setText(listaJugador.get(i).getNombre());
        if (listaJugador.get(i).getGenero().equals("M")){
            viewHolderJugador.txtGenero.setText("Masculino");
        }else{
            viewHolderJugador.txtGenero.setText("Femenino");
        }
        viewHolderJugador.txtNivel.setText(listaJugador.get(i).getNivel());
        viewHolderJugador.txtPuntos.setText(listaJugador.get(i).getPuntos()+"");
        viewHolderJugador.txtModo.setText(listaJugador.get(i).getModo());
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return listaJugador.size();
    }

    @Override
    public void onClick(View view) {
        if(listener!=null){
            listener.onClick(view);
        }
    }

    public class ViewHolderJugador extends RecyclerView.ViewHolder {

        ImageView imgAvatar;
        TextView txtNombre;
        TextView txtGenero;
        TextView txtNivel;
        TextView txtModo;
        TextView txtPuntos;

        public ViewHolderJugador(@NonNull View itemView) {
            super(itemView);
            imgAvatar=itemView.findViewById(R.id.idAvatar);
            txtNombre=itemView.findViewById(R.id.idNombre);
            txtGenero=itemView.findViewById(R.id.idGenero);
            txtNivel=itemView.findViewById(R.id.idNivel);
            txtModo=itemView.findViewById(R.id.idModo);
            txtPuntos=itemView.findViewById(R.id.idPuntos);
        }

    }
}
