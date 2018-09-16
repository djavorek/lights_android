package doublevv.lights.misc;

import android.support.annotation.ColorInt;
import android.view.View;
import android.widget.ImageView;

public class ColorSlot {
    private String name;
    @ColorInt private int color;
    private View view;

    public ColorSlot(String name, int color, View view) {
        this.name = name;
        this.view = view;
        setColor(color);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        view.setBackgroundColor(color);
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}
