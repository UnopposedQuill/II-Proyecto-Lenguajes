package model;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

public class ImageAdapter extends PagerAdapter {

    private ArrayList<ImageView> image_views = new ArrayList<>();

    public ImageAdapter(){
    }

    @Override
    public int getCount() {
        return image_views.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        /*
        @TODO: Limpiar esto
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //imageView.setImageResource(image_views.get(position));
        */

        //tomo el ImageView del ArrayList
        ImageView v = image_views.get(position);
        //Lo coloco
        container.addView(v, 0);
        //Retorno
        return v;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }

    public void addView(ImageView imageView) {
        image_views.add(imageView);
        notifyDataSetChanged();
    }

    /* Por el momento no se implementa el borrado en ningún sitio
    public void removeView(int index) {
        image_views.remove(index);
        notifyDataSetChanged();
    }
    */

    @Override
    public int getItemPosition(Object object) {
        if (image_views.contains(object)) {
            return image_views.indexOf(object);
        } else {
            return POSITION_NONE;
        }
    }
}
