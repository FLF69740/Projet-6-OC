package com.example.francoislf.go4lunch.api;

import com.example.francoislf.go4lunch.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

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

    public static Query getAllUserForRecyclerViewWorkmates(){
        return getUsersCollection().orderBy("username");
    }

    /**
     *  UPDATE
     */

    public static Task<Void> updateRestaurantChoice(String restaurantChoice, String uid){
        return getUsersCollection().document(uid).update("restaurantChoice" , restaurantChoice);
    }

    public static Task<Void> updateDateChoice(String dateChoice, String uid){
        return getUsersCollection().document(uid).update("dateChoice", dateChoice);
    }

    public static Task<Void> updateHourChoice(String hourChoice, String uid){
        return getUsersCollection().document(uid).update("hourChoice", hourChoice);
    }

    /**
     *  DELETE
     */

    public static Task<Void> deleteUser(String uid){
        return UserHelper.getUsersCollection().document(uid).delete();
    }
}
