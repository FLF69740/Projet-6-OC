package com.example.francoislf.go4lunch.api;

import com.example.francoislf.go4lunch.models.RestaurantLiked;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LikedHelper {

    private static final String COLLECTION_LIKED = "restaurants";

    //COLLECTION REFERENCE
    public static CollectionReference getLikedCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_LIKED);
    }

    /**
     *  CREATE
     */

    public static Task<Void> createLiked(String name, String placeId){
        RestaurantLiked restaurantLiked = new RestaurantLiked(name, placeId);
        return getLikedCollection().document(placeId).set(restaurantLiked);
    }

    /**
     *  GET
     */

    public static Task<DocumentSnapshot> getLiked(String placeId){
        return getLikedCollection().document(placeId).get();
    }

    /**
     *  UPDATE
     */

    public static Task<Void> updateParticipant(String placeId, int number){
        return getLikedCollection().document(placeId).update("participantsOfTheDay", number);
    }

    public static Task<Void> updateHourChoice(String placeId, int hour){
        return getLikedCollection().document(placeId).update("hourChoice", hour);
    }

    public static Task<Void> updateDateChoice(String placeId, int date){
        return getLikedCollection().document(placeId).update("dateChoice", date);
    }


    /*
     *  DELETE
     */

    //nothing

}
