package com.example.matchitup;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.viewpager.widget.PagerAdapter;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

public class SliderAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;


    static final int[] slideImages = {
            R.drawable.jugar,
            R.drawable.dict,
            R.drawable.perfil
    };

    static final Class[] slideClasses = {
            GameActivity.class,
            DictionaryActivity.class,
            ProfileActivity.class
    };

    //TODO: CONSTANTES QUE CONTIENEN LA INFO DE LAS PANTALLAS
    //TODO: ESTO NO MOLA MUCHO, NO SÉ SI FUNCIONARÁ CON EL CAMBIO DE IDIOMA
    static final String[] slideTitles = {
            "JUGAR",
            "DICCIONARIO",
            "PERFIL"
    };

    static final String[] slideDescriptions = {
            "Comienza a jugar e intenta emparejar el mayor número de palabras posibles con su significado."
                    + " Cuantas más emparejes, mayor puntuación obtendrás.",
            "Consulta las palabras con las que hayas dudado al jugar y apréndelas de una manera más" +
                    "tradicional.",
            "Encuentra las mayores puntuaciones que has alcanzado en todos los niveles jugados" +
                    " y cambia el idioma de la aplicación por otro distinto."
    };


    public SliderAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return slideTitles.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout)object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position){
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_card, container, false);

        ImageView slideLogo = (ImageView) view.findViewById(R.id.slide_logo);
        TextView slideTitle = (TextView) view.findViewById(R.id.slide_title);
        TextView slideDescription = (TextView) view.findViewById(R.id.slide_description);

        slideLogo.setImageResource(slideImages[position]);
        slideTitle.setText(slideTitles[position]);
        slideDescription.setText(slideDescriptions[position]);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, (Class<?>) slideClasses[position]);
                //intent.putextra("your_extra","your_class_value");
                //Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(context, android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
                context.startActivity(intent);
                //Animatoo.animateShrink(context);

            }
        });

        container.addView(view);

        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((LinearLayout) object);
    }

}
