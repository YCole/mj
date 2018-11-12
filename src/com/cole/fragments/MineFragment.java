package cole.fragments;

import android.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gome.beautymirror.R;

import org.linphone.core.ChatRoom;

import cole.view.MyActionBar;
import cole.view.MyOneLineView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MineFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MineFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    private OnFragmentInteractionListener mListener;
    private Context mContext;
    private MyOneLineView mMyDevices ,mUses,mAbout;
    private MyActionBar mActionBar;
    private MyOneLineView mEditMine;

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
    }

    public void displayFirstIint() {

           // com.gome.beautymirror.activities.BeautyMirrorActivity.instance().displayEmptyFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        mContext = getActivity().getApplicationContext();
        mMyDevices = (MyOneLineView)view.findViewById(R.id.myolv_devices);
        mUses =  (MyOneLineView)view.findViewById(R.id.myolv_use);
        mAbout =  (MyOneLineView)view.findViewById(R.id.myolv_about);
        mEditMine =  (MyOneLineView)view.findViewById(R.id.myolv_edit_mine);
        mActionBar =  (MyActionBar)view.findViewById(R.id.actionbar_fragment_mine);

        mMyDevices.initMine(R.mipmap.ic_launcher, "我的设备", "", true);
        mUses.initMine(R.mipmap.ic_launcher, "使用说明", "", true);
        mAbout.initMine(R.mipmap.ic_launcher, "关于美镜", "", true);
        mEditMine.initSubMine(R.mipmap.ic_launcher, "编辑查看个人信息", "美镜账号：","", true);

        mActionBar.setTranslucent(100);
        mActionBar.setTitle("我");
        mActionBar.setStatusBarHeight(mContext, false);



        initClick();

        return view;
    }

    private void initClick() {
        mEditMine.setOnRootClickListener(new MyOneLineView.OnRootClickListener() {
            @Override
            public void onRootClick(View view) {

            }
        },1);
        mMyDevices.setOnRootClickListener(new MyOneLineView.OnRootClickListener() {
            @Override
            public void onRootClick(View view) {

            }
        },2);
        mUses.setOnRootClickListener(new MyOneLineView.OnRootClickListener() {
            @Override
            public void onRootClick(View view) {

            }
        },3);
        mAbout.setOnRootClickListener(new MyOneLineView.OnRootClickListener() {
            @Override
            public void onRootClick(View view) {

            }
        },4);
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
}
