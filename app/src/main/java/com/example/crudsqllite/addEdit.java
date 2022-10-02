package com.example.crudsqllite;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.crudsqllite.helper.Db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class addEdit extends AppCompatActivity {

    EditText txt_id, txt_name, txt_address;
    Button btn_submit, btn_cancel;
    Db SQLite = new Db(this);
    String id, name, address,ImgFilePaht;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt_id = (EditText) findViewById(R.id.txt_id);
        txt_name = (EditText) findViewById(R.id.txt_nama);
        txt_address = (EditText) findViewById(R.id.txt_address);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);

        id = getIntent().getStringExtra(MainActivity.TAG_ID);
        name = getIntent().getStringExtra(MainActivity.TAG_NAME);
        address = getIntent().getStringExtra(MainActivity.TAG_ADDRESS);
        ImgFilePaht = getIntent().getStringExtra(MainActivity.TAG_IMG);

        if(id == null || id.equals("")){
            setTitle("Add Data");
        }else{
            setTitle("Edit Data");
            txt_id.setText(id);
            txt_name.setText(name);
            txt_address.setText(address);
            ImageView imageView=findViewById(R.id.image);
            Bitmap img= BitmapFactory.decodeFile(ImgFilePaht);
            imageView.setImageBitmap(img);
        }

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(txt_id.getText().toString().equals("")){
                        save();
                    }else{
                        edit();
                    }
                }catch (Exception e){
                    Log.e("Submit", e.toString());
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blank();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed(){
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                blank();
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    //Kosongkan semua EditText
    private void blank(){
        txt_name.requestFocus();
        txt_id.setText(null);
        txt_name.setText(null);
        txt_address.setText(null);
    }

    //Menyimpan Data ke Database SQLite
    private void save(){
        if(String.valueOf(txt_name.getText()).equals(null) || String.valueOf(txt_name.getText()).equals("") ||
                String.valueOf(txt_address.getText()).equals(null) || String.valueOf(txt_address.getText()).equals("")||ImgFilePaht==null||ImgFilePaht.equals("")){
            Toast.makeText(getApplicationContext(), "Please input name, address and image ...", Toast.LENGTH_SHORT).show();
        } else {
            SQLite.insert(txt_name.getText().toString().trim(), txt_address.getText().toString().trim(),this.ImgFilePaht);
            blank();
            finish();
        }
    }

    //Update Data ke Database SQLite
    private void edit(){
        if(String.valueOf(txt_name.getText()) == null || String.valueOf(txt_name.getText()).equals("") ||
                String.valueOf(txt_address.getText()) == null || String.valueOf(txt_address.getText()).equals("")||ImgFilePaht==null||ImgFilePaht.equals("")){
            Toast.makeText(getApplicationContext(), "Please input name or address ...", Toast.LENGTH_SHORT).show();
        } else {
            SQLite.update(Integer.parseInt(txt_id.getText().toString().trim()), txt_name.getText().toString().trim(),
                    txt_address.getText().toString().trim(),ImgFilePaht);
            blank();
            finish();
        }
    }


    public void getimage(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
            if (ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)return;

        }
        Intent getImage=new Intent(Intent.ACTION_PICK);
        getImage.setType("image/*");
        startActivityForResult(getImage,1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode!=RESULT_OK){
            Toast.makeText(this, "gagal ", Toast.LENGTH_SHORT).show();
            return;
        }
        ImageView imageView=findViewById(R.id.image);
        Uri uriImage= data.getData();
        System.out.println(uriImage);
        imageView.setImageURI(uriImage);

        try {
            InputStream inputImage= getContentResolver().openInputStream(uriImage);
            Bitmap image= BitmapFactory.decodeStream(inputImage);

            File root=getApplicationContext().getDir("data",MODE_PRIVATE);
            File folderimg=new File(root,"img");
            if (!folderimg.exists()){
                folderimg.mkdir();
            }
            String imgName=Math.random()+"_"+"image.jpg";
            File imgFile=new File(folderimg,imgName);
            FileOutputStream imgOut=new FileOutputStream(imgFile);
            image.compress(Bitmap.CompressFormat.JPEG,100,imgOut);
            imgOut.flush();
            imgOut.close();
            this.ImgFilePaht=imgFile.getAbsolutePath().toString();

        } catch (IOException e) {
            Toast.makeText(this, "data gagal di simpan", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }
}