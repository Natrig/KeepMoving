package rus.app.keepmoving.Util;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import rus.app.keepmoving.R;

public class KPTripUtil {
    public static void deleteRequest(String tripId, String userId, Context mContext) {
        DatabaseReference databaseRef = FirebaseDatabase
                .getInstance()
                .getReference();

        DatabaseReference tripRequest = databaseRef
                .child(mContext.getString(R.string.db_user_trip))
                .child(tripId)
                .child("requests")
                .child(userId);

        tripRequest.setValue("false");
    }

    public static void acceptRequest(String tripId, String userId, Context mContext) {
        DatabaseReference databaseRef = FirebaseDatabase
                .getInstance()
                .getReference();

        DatabaseReference tripRef = databaseRef
                .child(mContext.getString(R.string.db_user_trip))
                .child(tripId);

        DatabaseReference requestRef = tripRef.child("requests");
        requestRef.setValue(createMap(userId));

        tripRef.child("trip_status").setValue("PREPARED");
    }

    private static HashMap<String, String> createMap(String userId) {
        HashMap<String, String> myMap = new HashMap<String, String>();

        myMap.put(userId, "true");

        return myMap;
    }
}
