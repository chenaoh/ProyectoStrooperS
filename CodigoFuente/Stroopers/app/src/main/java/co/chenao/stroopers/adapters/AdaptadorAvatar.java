package co.chenao.stroopers.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import co.chenao.stroopers.R;
import co.chenao.stroopers.clases.PreferenciasJuego;
import co.chenao.stroopers.clases.Utilidades;
import co.chenao.stroopers.clases.vo.AvatarVo;

public class AdaptadorAvatar extends RecyclerView.Adapter<AdaptadorAvatar.ViewHolderAvatar> implements View.OnClickListener{

    private View.OnClickListener listener;
    List<AvatarVo> listaAvatars;
    View vista;

    int posicionMarcada=0;

    public AdaptadorAvatar(List<AvatarVo> listaAvatars  ) {
        this.listaAvatars = listaAvatars;
    }

    @NonNull
    @Override
    public ViewHolderAvatar onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        vista=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_grid_avatar,viewGroup,false);

        vista.setOnClickListener(this);

        return new ViewHolderAvatar(vista);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolderAvatar viewHolderAvatar, int i) {

        final int pos=i;
        final ViewHolderAvatar viewHolder=viewHolderAvatar;

        viewHolderAvatar.imgAvatar.setImageResource(listaAvatars.get(i).getAvatarId());

        viewHolderAvatar.cardAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                posicionMarcada=pos;
                Utilidades.avatarSeleccion=listaAvatars.get(pos);
                Utilidades.avatarIdSeleccion=pos+1;
                notifyDataSetChanged();
            }
        });
        if (Utilidades.avatarIdSeleccion==0){
            if (posicionMarcada==i){
                viewHolder.barraSeleccion.setBackgroundColor(vista.getResources().getColor(PreferenciasJuego.colorTema));
            }else{
                viewHolder.barraSeleccion.setBackgroundColor(vista.getResources().getColor(R.color.colorBlanco));
            }
        }else{
            //se valida para cuando se tenga la consulta de un jugador registrado y pueda pintar el avatar definido
            if (Utilidades.avatarIdSeleccion-1==pos){
                viewHolder.barraSeleccion.setBackgroundColor(vista.getResources().getColor(PreferenciasJuego.colorTema));

            }else{
                viewHolder.barraSeleccion.setBackgroundColor(vista.getResources().getColor(R.color.colorBlanco));
            }
        }
    }

    @Override
    public int getItemCount() {
        return listaAvatars.size();
    }


    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener!=null){
            listener.onClick(view);
        }
    }


    public class ViewHolderAvatar extends RecyclerView.ViewHolder {

        CardView cardAvatar;
        ImageView imgAvatar;
        TextView barraSeleccion;

        public ViewHolderAvatar(@NonNull View itemView) {
            super(itemView);
            cardAvatar=itemView.findViewById(R.id.cardAvatar);
            imgAvatar=itemView.findViewById(R.id.idAvatar);
            barraSeleccion=itemView.findViewById(R.id.barraSeleccionId);

        }

    }
}
