package alfredobarron.com.rallytic.models.repository;

import android.support.annotation.NonNull;

import alfredobarron.com.rallytic.models.entity.Rallies;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by alfredobarron on 27/07/15.
 */
public class RalliesRepository {

    public static Rallies create(@NonNull Realm realm, int id, String name, String description) {
        realm.beginTransaction();
        Rallies rally = realm.createObject(Rallies.class);
        rally.setId(id);
        rally.setName(name);
        rally.setDescription(description);
        rally.setScore(0);
        rally.setCompleted(false);
        realm.commitTransaction();
        return rally;
    }

    public static void truncate(@NonNull Realm realm) {
        realm.beginTransaction();
        realm.clear(Rallies.class);
        realm.commitTransaction();
    }

    public static Rallies updateByCompleted(@NonNull Realm realm, Rallies rally, boolean completed) {
        realm.beginTransaction();
        rally.setCompleted(completed);
        realm.commitTransaction();
        return rally;
    }

    public static Rallies updateByScore(@NonNull Realm realm, Rallies rally, int score) {
        realm.beginTransaction();
        rally.setScore(score);
        realm.commitTransaction();
        return rally;
    }

    public static Rallies findFirst(@NonNull Realm realm) {
        return realm.where(Rallies.class)
                .findFirst();
    }
}
