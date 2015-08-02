package alfredobarron.com.rallytic;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by alfredobarron on 26/07/15.
 */
public class RallyTIC extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initRealm();
        //initCalligraphy();
    }

    private void initRealm() {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(getApplicationContext())
                .name(getString(R.string.db_name))
                .schemaVersion(getResources().getInteger(R.integer.db_version))
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    public void initCalligraphy() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(getString(R.string.font_roboto_light))
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
