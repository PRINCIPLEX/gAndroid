package com.gproject.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.*;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.gproject.MySqlData;
import com.gproject.androidcoffee12.*;
import com.gproject.tool.*;

import com.gproject.entity.CustomerEntity;
import com.gproject.tool.SectionExtensionItemView;
import com.gproject.tool.TakePhotoFragment;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import okhttp3.*;

import java.io.File;
import java.io.IOException;


public class UserFragment extends TakePhotoFragment {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    String URL;
    String phone;
    byte[] userAvatar;
    String nickName;


    private ImageView iv_avatar2;   //TakePhoto
    private ImageView img_avatar;   //TakePhoto

    private TakePhoto takePhoto;
    private File file;
    private Uri imageUri;
    private CustomerEntity customerEntity;
    private TextView tv_userName;
    private TextView tv_phone;
    View view;

    int size = 1024*100;
    CompressConfig config;
    CropOptions crop;

    public UserFragment(){
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_user,container,false);
        SectionExtensionItemView avatar = (SectionExtensionItemView)view.findViewById(R.id.item_avatar);
        SectionTextItemView itemUserName = (SectionTextItemView)view.findViewById(R.id.item_username);
        SectionTextItemView itemMobile = (SectionTextItemView)view.findViewById(R.id.item_mobile);
        SectionTextItemView itemPassword = (SectionTextItemView)view.findViewById(R.id.item_password);
        Button btnSignOut = (Button)view.findViewById(R.id.btn_sign_out);
        img_avatar =(ImageView)view.findViewById(R.id.img_avatar);
        iv_avatar2 =(ImageView)view.findViewById(R.id.img_avatar2);
        tv_userName=(TextView)view.findViewById(R.id.txt_nickname);
        tv_phone=(TextView)view.findViewById(R.id.txt_user_phone);




        //系统照相机权限判断
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

        initUser();
        initTakePhoto();

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //写入安卓机
                SharedPreferences sp = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
                //这个的话是用来写文件的
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("customerEntity", null);
                //写完我们的文件的话记得要将我们的文件进行提交
                editor.apply();
                ((MySqlData)getActivity().getApplication()).closeConnect();

                Intent loginIntent = new Intent(getContext(),LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(loginIntent);
            }
        });

        itemUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent usernameIntent =  new Intent(getContext(), ChangUserNameActivity.class);
                startActivity(usernameIntent);
            }
        });

        itemPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent passwordIntent = new Intent(getContext(), ChangePasswordActivity.class);
                startActivity(passwordIntent);
            }
        });

        itemMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phoneIntent =  new Intent(getContext(), ChangePhoneActivity.class);
                startActivity(phoneIntent);
            }
        });


        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto.onPickFromGalleryWithCrop(imageUri,crop);
            }
        });

        return view;
    }

    public void initTakePhoto()
    {
        file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        imageUri = Uri.fromFile(file);
        takePhoto =getTakePhoto();
        config=new CompressConfig.Builder().setMaxSize(50*1024).setMaxPixel(800).create();
        takePhoto.onEnableCompress(config,true);
        crop=new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create();
    }



    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        TImage image = result.getImage();
        File file = new File(image.getOriginalPath());
        userAvatar = Tool.FileToByte(file);
        if (userAvatar.length>size)
        {
            Toast.makeText(getActivity(),"照片不能大于100k" ,Toast.LENGTH_SHORT).show();
        }
        else
        {
            changeUserAvatar();

        }

    }


    public void changeUserAvatar(){


        RequestBody body = RequestBody.create(JSON, userAvatar);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .post(body)
                .url(URL+"change_user_avatar/"+phone)
                .build();
        Call call = client.newCall(request);


        //异步调用,并设置回调函数
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(getActivity(),"网络出错,更新用户头像失败",Toast.LENGTH_SHORT).show();
                Looper.loop();
                Log.e("yztest", "yzChangeUserNameFail");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((MySqlData)getActivity().getApplicationContext()).getCustomerEntity().setAvatar(userAvatar);
                        ((MySqlData)getActivity().getApplicationContext()).reWriteShared();

                        Bitmap imageBitmap = BitmapFactory.decodeByteArray(userAvatar, 0, userAvatar.length);
                        img_avatar.setImageBitmap(imageBitmap);
                        iv_avatar2.setImageBitmap(imageBitmap);

                        Toast.makeText(getActivity(),"更新用户头像成功",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }


    public void initUser()
    {
        URL = ((MySqlData)getActivity().getApplicationContext()).getURL();
        nickName = ((MySqlData)getActivity().getApplicationContext()).getCustomerEntity().getUsername();
        phone = ((MySqlData)getActivity().getApplicationContext()).getCustomerEntity().getPhone();
        tv_userName.setText(nickName);
        tv_phone.setText(phone);
        byte[]data = ((MySqlData)getActivity().getApplicationContext()).getCustomerEntity().getAvatar();
        if(data!=null)
        {
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            img_avatar.setImageBitmap(imageBitmap);
            iv_avatar2.setImageBitmap(imageBitmap);
        }

    }



    @Override
    public void onResume() {
        super.onResume();
        nickName = ((MySqlData)getActivity().getApplicationContext()).getCustomerEntity().getUsername();
        phone = ((MySqlData)getActivity().getApplicationContext()).getCustomerEntity().getPhone();
        tv_userName.setText(nickName);
        tv_phone.setText(phone);
    }
}
