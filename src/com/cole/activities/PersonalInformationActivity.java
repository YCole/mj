package cole.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gome.beautymirror.data.DataService;
import com.gome.beautymirror.R;
import com.gome.beautymirror.data.provider.DatabaseUtil;
import com.gome.beautymirror.data.DataUtil;
import com.gome.beautymirror.ui.RoundImageView;

import java.io.File;
import java.util.ArrayList;

import cole.view.MyOneLineView;
import gome.beautymirror.ui.blurdialog.BlurDialog;

public class PersonalInformationActivity  extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "PersonalInformationActivity";

    private static final int PHOTO_REQUEST_CAREMA = 1;
    private static final int PHOTO_REQUEST_GALLERY = 2;
    private static final int PHOTO_REQUEST_CUT = 1000;

    private static final int PERMISSIONS_REQUEST_CAREMA = 1;

    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";

    Context mContext;
    private LinearLayout mActionBar;
    private TextView mTvTitleName,mResetAvatar;
    private MyOneLineView mNickName;
    private MyOneLineView mAccount;
    private MyOneLineView mResetPassword;
    private RoundImageView mAvatarIcon;
    private Button mLogout;
    private String mStrAccount, mStrNickName;
    private Bitmap mAvatarBitmap;
    private ArrayList<String> mResetAvatarlist;
    private File tempFile;
    private ImageView mBtBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = PersonalInformationActivity.this;
    }
    @Override
    protected void loadXml() {
        setContentView(R.layout.activity_personal_information);
    }

    @Override
    protected void initView() {
        mActionBar = findViewById(R.id.action_bar);
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        mActionBar.setPadding(0, getResources().getDimensionPixelSize(resourceId),0,0);

        mTvTitleName=findViewById(R.id.tv_title_name);
        mTvTitleName.setText(getString(R.string.personal_information));

        mAvatarIcon = findViewById(R.id.mine_avatar_icon);
        mLogout =  findViewById(R.id.btn_logout);
        mResetAvatar =  findViewById(R.id.reset_avatar);
        mBtBack = findViewById(R.id.bt_back);

        mNickName = (MyOneLineView) findViewById(R.id.nick_name);
        mNickName.initMine(0, getString(R.string.nick_name), "", true,true);
        mNickName.showLeftIcon(false);
        mAccount = (MyOneLineView) findViewById(R.id.account);
        mAccount.initMine(0, getString(R.string.beautymirror_account), "", true,false);
        mAccount.showLeftIcon(false);
        mResetPassword = findViewById(R.id.reset_password);
        mResetPassword.initMine(0, getString(R.string.reset_password), "", true,false);
        mResetPassword.showLeftIcon(false);


    }

    @Override
    protected void initData() {
        Cursor cursor = DataService.instance().getAccountsAndDevices(null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            mStrAccount = cursor.getString(DatabaseUtil.Account.COLUMN_ACCOUNT);
            mAvatarBitmap = DataUtil.getImage(cursor.getBlob(DatabaseUtil.Account.COLUMN_ICON));
            mStrNickName = cursor.getString(DatabaseUtil.Account.COLUMN_NAME);
            refresh();
        }

        mResetAvatarlist = new ArrayList<String>();
        mResetAvatarlist.add(getString(R.string.delete_photo));
        mResetAvatarlist.add(getString(R.string.take_photo));
        mResetAvatarlist.add(getString(R.string.select_photo_from_gallery));
    }

    private void refresh(){
        mNickName.setRightText(mStrNickName);
        mAccount.setRightText(mStrAccount);
        mAvatarIcon.setImageBitmap(mAvatarBitmap);
    }

    @Override
    protected void setListener() {
        mNickName.setOnClickListener(this);
        mLogout.setOnClickListener(this);
        mResetAvatar.setOnClickListener(this);
        mResetPassword.setOnClickListener(this);
        mBtBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.nick_name:
                showNickNameDialog();
                break;
            case R.id.btn_logout:
                showLogoutDialog();
                break;
            case R.id.reset_avatar:
                ShowListDialog();
                break;
            case R.id.reset_password:
                Intent in = new Intent(this, ResetPasswordActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("phone", mStrAccount);
                in.putExtras(bundle);
                startActivity(in);
                break;
            case R.id.bt_back:
                finish();
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_CAREMA) {
            if (hasSdcard()) {
                crop(Uri.fromFile(tempFile));
            } else {
                Toast.makeText(mContext, "SD error", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                Uri uri = data.getData();
                crop(uri);
            } else {
                Log.d(TAG, "onActivityResult: PHOTO_REQUEST_GALLERY is null");
            }
        }else if (requestCode == PHOTO_REQUEST_CUT) {
            if (data != null) {
                final Bitmap bitmap = data.getParcelableExtra("data");
                if (bitmap != null) {
                    DataService.instance().updateAccount(mStrAccount, mStrNickName, bitmap, new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            if (DataService.checkResult(msg)) {
                                mAvatarBitmap = bitmap;
                                refresh();
                            } else {
                                Toast.makeText(mContext, "Fail", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, 0);
                } else {
                    Toast.makeText(mContext, "Cancel", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d(TAG, "onActivityResult: PHOTO_REQUEST_CUT is null");
            }
            try {
                tempFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    private void startCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAREMA);
            return;
        }

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (hasSdcard()) {
            tempFile = new File(Environment.getExternalStorageDirectory(),
                    PHOTO_FILE_NAME);
            Uri uri = Uri.fromFile(tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
    }

    private void startGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    private void crop(Uri uri) {
        Intent intent = new Intent(mContext, CropActivity.class);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);

        intent.putExtra("outputFormat", "PNG");
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    private void deletePhoto() {
        DataService.instance().updateAccount(mStrAccount, mStrNickName, null, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (DataService.checkResult(msg)) {
                    mAvatarBitmap =  null;
                    refresh();
                } else {
                    Toast.makeText(mContext, "Fail", Toast.LENGTH_SHORT).show();
                }
            }
        }, 0);
    }

    private void showNickNameDialog() {
        final View view= LayoutInflater.from(this).inflate(R.layout.dialog_edittext_layout, null);
        TextView title =view.findViewById(R.id.dialog_title);
        title.setText(R.string.edit_nick_name);
        final TextView cancel =view.findViewById(R.id.choosepage_cancel);
        final TextView sure =view.findViewById(R.id.choosepage_sure);
        final EditText edittext =view.findViewById(R.id.choosepage_edittext);
        edittext.setText(mStrNickName);
        new BlurDialog(this,false){
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(view);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
                sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String nickename=edittext.getText().toString();
                        if(!"".equals(nickename)){
                           DataService.instance().updateAccount(mStrAccount, nickename, mAvatarBitmap, new Handler() {
                                @Override
                                public void handleMessage(Message msg) {
                                    if (DataService.checkResult(msg)) {
                                        mStrNickName = nickename;
                                        refresh();
                                    } else {
                                        Toast.makeText(mContext, "Fail", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, 0);
                        }
                        dismiss();
                    }
                });
            }
        }.show();
    }

    public void ShowListDialog() {
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.dialog_list, null);
        final ListView myListView = layout.findViewById(R.id.spinner_list);
        MyAdapter adapter = new MyAdapter(mContext, mResetAvatarlist);
        myListView.setAdapter(adapter);

        BlurDialog dialog = new BlurDialog(this,false){
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(layout);
                myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int positon, long id) {
                        if(positon == 0){
                            deletePhoto();
                        }else if(positon ==1){
                            startCamera();
                        }else {
                            startGallery();
                        }
                        dismiss();
                    }
                });
            }
        };

        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.x = 0;
        lp.y = (int)dpToPx(mContext,20);
        window.setAttributes(lp);
        dialog.show();

    }

    private void showLogoutDialog(){
            final View view= LayoutInflater.from(this).inflate(R.layout.dialog_logout, null);
            final TextView cancel =view.findViewById(R.id.cancel);
            final TextView sure =view.findViewById(R.id.sure);
            final TextView title =view.findViewById(R.id.title);
            title.setText(getString(R.string.dialog_logout));
            new BlurDialog(this,false){
                @Override
                protected void onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                    setContentView(view);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                        }
                    });
                    sure.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (DataService.instance().logoutAccount(null) > 0) {
                                com.gome.beautymirror.activities.BeautyMirrorActivity.instance().startRigisterAndLogin();
                            }
                            dismiss();
                        }
                    });
                }
            }.show();


    }

    private float dpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return dp * density;
    }

    //自定义的适配器
    class MyAdapter extends BaseAdapter {
        private ArrayList<String> mlist;
        private Context mContext;
        private TextView mSelectItem;

        public MyAdapter(Context context, ArrayList<String> list) {
            this.mContext = context;
            mlist = new ArrayList<String>();
            this.mlist = list;
        }

        @Override
        public int getCount() {
            return mlist.size();
        }

        @Override
        public Object getItem(int position) {
            return mlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(mContext);
                convertView = inflater.inflate(R.layout.dialog_list_item,null);
                mSelectItem = convertView.findViewById(R.id.txtext);
                convertView.setTag(mSelectItem);
            }else{
                mSelectItem = (TextView) convertView.getTag();
            }
            mSelectItem.setText(mlist.get(position).toString());
            return convertView;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CAREMA) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.CAMERA) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    startCamera();
                } else {
                    Toast.makeText(mContext, "Permissions fail", Toast.LENGTH_SHORT).show();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
