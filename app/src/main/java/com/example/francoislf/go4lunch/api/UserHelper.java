package com.example.francoislf.go4lunch.api;

import com.example.francoislf.go4lunch.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import static com.example.francoislf.go4lunch.controllers.activities.BaseActivity.USER_DATE_CHOICE;
import static com.example.francoislf.go4lunch.controllers.activities.BaseActivity.USER_HOUR_CHOICE;
import static com.example.francoislf.go4lunch.controllers.activities.BaseActivity.USER_RESTAURANT_CHOICE;

public class UserHelper {

    private static final String COLLECTION_NAME = "users";

    // COLLECTION REFERENCE
    public static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    /**
     *  CREATE
     */

    public static Task<Void> createUser(String uid, String username, String urlPicture){
        User user = new User(uid,username,urlPicture);
        return getUsersCollection().document(uid).set(user);
    }

    /**
     *  GET
     */

    public static Task<DocumentSnapshot> getUser(String uid){
        return getUsersCollection().document(uid).get();
    }

    public static Query getAllUsers(){
        return getUsersCollection().orderBy("username");
    }

    /**
     *  UPDATE
     */

    public static Task<Void> updateRestaurantChoice(String restaurantChoice, String uid){
        return getUsersCollection().document(uid).update(USER_RESTAURANT_CHOICE , restaurantChoice);
    }

    public static Task<Void> updateDateChoice(String dateChoice, String uid){
        return getUsersCollection().document(uid).update(USER_DATE_CHOICE, dateChoice);
    }

    public static Task<Void> updateHourChoice(String hourChoice, String uid){
        return getUsersCollection().document(uid).update(USER_HOUR_CHOICE, hourChoice);
    }

    /**
     *  DELETE
     */

    public static Task<Void> deleteUser(String uid){
        return getUsersCollection().document(uid).delete();
    }
}
