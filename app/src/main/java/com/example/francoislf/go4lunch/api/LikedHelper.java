package com.example.francoislf.go4lunch.api;

import com.example.francoislf.go4lunch.models.RestaurantLiked;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class LikedHelper {

    private static final String COLLECTION_LIKED = "restaurants";

    //COLLECTION REFERENCE
    public static CollectionReference getLikedCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_LIKED);
    }

    /**
     *  CREATE
     */

    public static Task<Void> createLiked(String name, String placeId, String user){
        RestaurantLiked restaurantLiked = new RestaurantLiked(name, placeId, user);
        return getLikedCollection().document(placeId).set(restaurantLiked);
    }

    /**
     *  GET
     */

    public static Task<DocumentSnapshot> getLiked(String placeId){
        return getLikedCollection().document(placeId).get();
    }

    public static Query getAllLiked(){
        return getLikedCollection().orderBy("restaurantName");
    }

    /**
     *  UPDATE
     */

    public static Task<Void> updateLiked(String placeId, int like){
        return getLikedCollection().document(placeId).update("numberOfLike", like);
    }

    public static Task<Void> updateParticipants(String placeId, String participants){
        return getLikedCollection().document(placeId).update("participants", participants);
    }

    /*
     *  DELETE
     */

    //nothing

}
