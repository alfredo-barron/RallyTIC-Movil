package alfredobarron.com.rallytic.models.repository;

import android.support.annotation.NonNull;

import alfredobarron.com.rallytic.models.entity.Task;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by alfredobarron on 26/07/15.
 */
public class TaskRepository {

    public static Task create(@NonNull Realm realm, int id, String name, int time, int intents,
                              String station, String lat, String lng, int questionId, String question,
                              String asnwer, String track, boolean penalty, int order) {
        realm.beginTransaction();
        Task task = realm.createObject(Task.class);
        task.setId(id);
        task.setName(name);
        task.setTime(time);
        task.setIntents(intents);
        task.setStation(station);
        task.setLat(lat);
        task.setLng(lng);
        task.setQuestionId(questionId);
        task.setQuestion(question);
        task.setAnswer(asnwer);
        task.setTrack(track);
        task.setPenalty(penalty);
        task.setCompleted(false);
        task.setOrder(order);
        realm.commitTransaction();
        return task;
    }

    public static void delete(@NonNull Realm realm, Task task) {
        realm.beginTransaction();
        task.removeFromRealm();
        realm.commitTransaction();
    }

    public static void truncate(@NonNull Realm realm) {
        realm.beginTransaction();
        realm.clear(Task.class);
        realm.commitTransaction();
    }

    public static Task updateByCompleted(@NonNull Realm realm, Task task, boolean completed) {
        realm.beginTransaction();
        task.setCompleted(completed);
        realm.commitTransaction();
        return task;
    }

    public static long count(@NonNull Realm realm) {
        return realm.where(Task.class)
                .count();
    }

    public static Task checkin(@NonNull Realm realm, String lat, String lng) {
        return realm.where(Task.class)
                .equalTo("lat", lat)
                .equalTo("lng", lng)
                .findFirst();
    }

    public static Task comparePenalty(@NonNull Realm realm, int id) {
        return realm.where(Task.class)
                .equalTo("id", id)
                .equalTo("penalty", true)
                .findFirst();
    }

    public static long countByCompleted(@NonNull Realm realm, boolean completed) {
        return realm.where(Task.class)
                .equalTo("completed", completed)
                .count();
    }

    public static RealmResults<Task> findAll(@NonNull Realm realm) {
        return realm.where(Task.class)
                .findAll();
    }

    public static Task find(@NonNull Realm realm, int id) {
        return realm.where(Task.class)
                .equalTo("id",id)
                .findFirst();
    }

    public static RealmResults<Task> findAllByCompleted(@NonNull Realm realm, boolean completed) {
        return realm.where(Task.class)
                .equalTo("completed", completed)
                .findAll();
    }

    public static RealmResults<Task> findAllByGroupName(@NonNull Realm realm,
                                                        String groupName) {
        return realm.where(Task.class)
                .equalTo("groupName", groupName)
                .findAll();
    }
}
