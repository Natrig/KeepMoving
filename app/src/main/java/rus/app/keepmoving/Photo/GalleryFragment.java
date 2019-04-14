package rus.app.keepmoving.Photo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

import rus.app.keepmoving.R;
import rus.app.keepmoving.Util.FilePaths;
import rus.app.keepmoving.Util.FileSearch;
import rus.app.keepmoving.Util.GridImageAdapter;
import rus.app.keepmoving.Util.KPFirebase;

public class GalleryFragment extends Fragment {
    private static final String TAG = "GalleryFragment";

    // const
    private static final int NUM_GRID_COLUMNS = 3;
    private KPFirebase mKPFirebase;

    // widgets
    private GridView gridView;
    private ImageView galleryImage;
    private ProgressBar mProgressBar;
    private Spinner directorySpinner;

    // variables
    private ArrayList<String> directories;
    private String mAppend = "file:/";
    private String mSelectedImg = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        mKPFirebase = new KPFirebase(getActivity());

        galleryImage = (ImageView) view.findViewById(R.id.galleryImageView);
        gridView = (GridView) view.findViewById(R.id.gridView);
        directorySpinner = (Spinner) view.findViewById(R.id.spinnerDirectory);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);

        directories = new ArrayList<>();

        ImageView shareClose = (ImageView) view.findViewById(R.id.ivCloseShare);
        shareClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        TextView nextScreen = (TextView) view.findViewById(R.id.tvNext);
        nextScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mSelectedImg.equals("")) {
                    mKPFirebase.uploadProfilePhoto(mSelectedImg, null);
                    getActivity().finish();
                }
            }
        });

        init();

        return view;
    }

    private void init() {
        FilePaths filePaths = new FilePaths();

        if (FileSearch.getDirectoryPath(filePaths.PICTURES) != null) {
            directories = FileSearch.getDirectoryPath(filePaths.PICTURES);
        }

        directories.add(filePaths.CAMERA);

        ArrayList<String> directoryNames = new ArrayList<>();
        for (String directory : directories) {
            int index = directory.lastIndexOf("/");
            String subStr = directory.substring(index + 1);

            directoryNames.add(subStr);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, directoryNames);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        directorySpinner.setAdapter(adapter);

        directorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setupGridView(directories.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setupGridView(String selectedDirectory) {
        final ArrayList<String> imgURLs = FileSearch.getFilePath(selectedDirectory);

        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth / NUM_GRID_COLUMNS;
        gridView.setColumnWidth(imageWidth);

        GridImageAdapter gridImageAdapter = new GridImageAdapter(getActivity(), R.layout.layout_grid_imageview, mAppend, imgURLs);
        gridView.setAdapter(gridImageAdapter);

        if (imgURLs.size() != 0) {
            setImage(imgURLs.get(0), galleryImage, mAppend);
            mSelectedImg = imgURLs.get(0);
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                setImage(imgURLs.get(position), galleryImage, mAppend);
                mSelectedImg = imgURLs.get(position);
            }
        });
    }

    private void setImage(String imgURLs, ImageView image, String append) {
        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.displayImage(append + imgURLs, image, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}
