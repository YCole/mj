package cole.fragments;

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import com.gome.beautymirror.activities.BeautyMirrorActivity;
import com.gome.beautymirror.data.DataService;
import com.gome.beautymirror.data.DataUtil;
import com.gome.beautymirror.data.provider.DatabaseUtil;
import com.gome.beautymirror.R;

import org.linphone.core.ChatRoom;

import cole.activities.AboutActivity;
import cole.activities.CropActivity;
import cole.activities.MyDeviceActivity;
import cole.activities.PersonalInformationActivity;
import cole.view.MyActionBar;
import cole.view.MyOneLineView;

import com.gome.beautymirror.ui.RoundImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MineFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MineFragment extends Fragment {
    private static final String TAG = "MineFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    private OnFragmentInteractionListener mListener;
    private Context mContext;
    private MyOneLineView mMyDevices, mUses, mAbout;
    private MyActionBar mActionBar;
    private TextView mEditMine;

    private String mAccount, mName;
    private RoundImageView mivIcon;
    private Button gotoEdit;

    public MineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MineFragment newInstance(String param1, String param2) {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }

    public void displayFirstIint() {

        // com.gome.beautymirror.activities.BeautyMirrorActivity.instance().displayEmptyFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        mContext = getActivity().getApplicationContext();
        mMyDevices = (MyOneLineView) view.findViewById(R.id.myolv_devices);
        mUses = (MyOneLineView) view.findViewById(R.id.myolv_use);
        mAbout = (MyOneLineView) view.findViewById(R.id.myolv_about);
        mEditMine = (TextView) view.findViewById(R.id.myolv_edit_mine);
        mActionBar = (MyActionBar) view.findViewById(R.id.actionbar_fragment_mine);
        mivIcon = (RoundImageView) view.findViewById(R.id.iv_mine_fragment_icon);
        gotoEdit = (Button) view.findViewById(R.id.BT_edit_mine);

        mMyDevices.initMine(R.drawable.me_ic_mirror, mContext.getString(R.string.my_device), "", true,true);
        mUses.initMine(R.drawable.me_ic_illustration, mContext.getString(R.string.instructions), "", true,true);
        mAbout.initMine(R.drawable.me_ic_about, mContext.getString(R.string.about_beautymirror), "", true,false);
        mEditMine.setText("编辑个人信息");
        mEditMine.setTextSize(20);

        initClick();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Cursor cursor = DataService.instance().getAccountsAndDevices(null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            initData(cursor.getString(DatabaseUtil.Account.COLUMN_ACCOUNT),
                    cursor.getString(DatabaseUtil.Account.COLUMN_NAME),
                    DataUtil.getImage(cursor.getBlob(DatabaseUtil.Account.COLUMN_ICON)));
        }
        if (cursor != null) cursor.close();
    }

    private void initClick() {
        gotoEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), PersonalInformationActivity.class));
            }
        });
        mMyDevices.setOnRootClickListener(new MyOneLineView.OnRootClickListener() {
            @Override
            public void onRootClick(View view) {
                startActivity(new Intent(getContext(), MyDeviceActivity.class));
            }
        }, 2);
        mUses.setOnRootClickListener(new MyOneLineView.OnRootClickListener() {
            @Override
            public void onRootClick(View view) {
                Intent intent = new Intent();

                if (Build.VERSION.SDK_INT < 19) {
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                } else {
                    intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                }
                startActivityForResult(intent, 1);
            }
        }, 3);
        mAbout.setOnRootClickListener(new MyOneLineView.OnRootClickListener() {
            @Override
            public void onRootClick(View view) {
                startActivity(new Intent(getContext(), AboutActivity.class));
            }
        }, 4);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void initData(String account, String name, Bitmap icon) {
        mAccount = account;
        mName = name;
        if (!TextUtils.isEmpty(account) || !TextUtils.isEmpty(name)) {
            mEditMine.setTextSize(10);
            mEditMine.setText("\n美镜账号" + (TextUtils.isEmpty(account) ? "" : "：" + account));
        }
        mivIcon.setImageBitmap(icon);

    }

}
