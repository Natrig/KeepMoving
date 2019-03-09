package rus.app.keepmoving.Util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import rus.app.keepmoving.Entities.TripListInfo;
import rus.app.keepmoving.R;

public class KPTripAdapter extends ArrayAdapter<TripListInfo> {
    private static final String TAG = "KPTripAdapter";

    private Context mContext;
    private int mResource;

    public KPTripAdapter(@NonNull Context context, int resource, @NonNull List<TripListInfo> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position).getCreator_name();
        String fromPlace = getItem(position).getFrom_place();
        String wherePlace = getItem(position).getWhere_place();
        String carModel = getItem(position).getCar_model();
        String departureDate = getItem(position).getDeparture_date();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvName = (TextView) convertView.findViewById(R.id.nameInput);
        TextView tvFromPlace = (TextView) convertView.findViewById(R.id.fromPlaceInput);
        TextView tvWherePlace = (TextView) convertView.findViewById(R.id.wherePlaceInput);
        TextView tvCarModel = (TextView) convertView.findViewById(R.id.carModelInput);
        TextView tvDepartureDate = (TextView) convertView.findViewById(R.id.departureInput);

        tvName.setText(" " + name);
        tvFromPlace.setText(fromPlace);
        tvWherePlace.setText(" > " + wherePlace);
        tvCarModel.setText(" " + carModel);
        tvDepartureDate.setText(" " + departureDate);

        return convertView;
    }
}
