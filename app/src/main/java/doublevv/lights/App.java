package doublevv.lights;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

public class App extends Application {

    public static Resources resources;

    @Override
    public void onCreate() {
        super.onCreate();
        resources = getResources();
    }
}
