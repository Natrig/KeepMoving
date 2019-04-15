package rus.app.keepmoving.Photo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import rus.app.keepmoving.R;
import rus.app.keepmoving.Util.KPFirebase;
import rus.app.keepmoving.Util.KPPhotoPermissions;

public class PhotoFragment extends Fragment {
    private static final String TAG = "PhotoFragment";
    private static final int CAMERA_REQUEST_CODE = 2301;

    private KPFirebase mKPFirebase;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);

        mKPFirebase = new KPFirebase(getActivity());

        ImageView btnLaunchCamera = (ImageView) view.findViewById(R.id.btnLaunchCamera);
        btnLaunchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((PhotoActivity) getActivity()).checkSinglePermission(KPPhotoPermissions.CAMERA_PERMISSIONS[0])) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                } else {
                    Intent intent = new Intent(getActivity(), PhotoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            Bitmap bm = (Bitmap) data.getExtras().get("data");
            if (requestCode == CAMERA_REQUEST_CODE) {

                mKPFirebase.uploadProfilePhoto("", bm);
                getActivity().finish();
            }
        }
    }
}
