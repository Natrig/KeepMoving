package rus.app.keepmoving.Util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import rus.app.keepmoving.Entities.RequestListInfo;
import rus.app.keepmoving.R;

public class KPRequestAdapter extends ArrayAdapter<RequestListInfo> {
    private static final String TAG = "KPRequestAdapter";

    private Context mContext;
    private int mResource;
    private List<RequestListInfo> requests;

    public KPRequestAdapter(@NonNull Context context, int resource, @NonNull List<RequestListInfo> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
        this.requests = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(mResource, parent, false);

        String name = getItem(position).getUser_name();
        final String trip_id = getItem(position).getTrip_id();
        final String user_id = getItem(position).getUser_id();
        final String user_photo = getItem(position).getImgUrl();

        final TextView tvName = (TextView) convertView.findViewById(R.id.requestNameInput);
        final Button tvAcceptButton = (Button) convertView.findViewById(R.id.requestAcceptButton);
        final Button tvRejectButton = (Button) convertView.findViewById(R.id.requestRejectButton);

        final View finalConvertView = convertView;

        tvAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KPTripUtil.acceptRequest(trip_id, user_id, mContext);

                finalConvertView.setVisibility(View.INVISIBLE);
                tvAcceptButton.setVisibility(View.INVISIBLE);
                tvRejectButton.setVisibility(View.INVISIBLE);
            }
        });

        tvRejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KPTripUtil.deleteRequest(trip_id, user_id, mContext);

                requests.remove(position);
                notifyDataSetChanged();
            }
        });

        tvName.setText(" " + name);
        KPImageLoader.setImage(user_photo, (ImageView) convertView.findViewById(R.id.requestProfileImage), null, "");

        return convertView;
    }
}
