package edu.android.teamproject_whereru;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.android.teamproject_whereru.Model.Post;

public class PostWriteActivity extends AppCompatActivity {

    private static final String TAG = "photo";
    private static final String TBL_NAME = "post";

    private static final int GALLERY_CODE = 111;

    private static final int REQUEST_PERMISSION_CODE = 1;

    private EditText editTitle, editBody;
    private ImageView imageWrite;
    private TextView writeGuestName, writeToday;

    private FirebaseDatabase database;
    private DatabaseReference writeReference;
    private ChildEventListener childEventListener;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_write);

        editTitle = findViewById(R.id.editTitle);
        editBody = findViewById(R.id.editBody);
        imageWrite = findViewById(R.id.imageWrite);

        writeGuestName = findViewById(R.id.writeGuestName);
        writeToday = findViewById(R.id.writeToday);

        Date date = new Date();

        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

        String dCreated = format.format(date);
        Post post = new Post(dCreated);

       writeToday.setText(post.getdCreated());



        database = FirebaseDatabase.getInstance();
        writeReference = database.getReference(TBL_NAME);
        writeReference = database.getReference().child("포스트");


        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.i(TAG, "파일 추가 완료");

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        };

    }


        // 사용자 권한 허용하기위해 Permission 결과를 되돌려받음
        @Override
        public void onRequestPermissionsResult ( int requestCode,
        @NonNull String[] permissions,
        @NonNull int[] grantResults){

            Log.i(TAG, "PerMission 실행");

            switch (requestCode) {
                case REQUEST_PERMISSION_CODE:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // 동의한 경우
                        Intent intent = new Intent(Intent.ACTION_PICK);

                        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        intent.setType("image/*");

                        startActivityForResult(intent, GALLERY_CODE);
                    } else {
                        Toast.makeText(this, "기능 사용을 위해선 동의가 필요합니다", Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }

        public void addPhoto (View view){
            // TODO : 사진 추가 버튼 암시적 인텐트로 갤러리 화면 열기

            // 권한 허용부터시작
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_CODE);

            Log.i(TAG, "액티비티 실행");
        }
        @Override
        protected void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){
            super.onActivityResult(requestCode, resultCode, data);

            Log.i(TAG, resultCode + ": " + RESULT_OK);

            if (resultCode == RESULT_OK) {
                Log.i(TAG, "결과값 돌아오는거 실행");

                sendPicture(data.getData());
            }
        }

        // 이미지 뷰에 비트맵 넣기
        private void sendPicture (Uri imgUri){

            Log.i(TAG, "sendPicture 실행");

            String imagePath = getRealPathFromURI(imgUri);
            ExifInterface exif = null;

            try {
                exif = new ExifInterface(imagePath);
            } catch (Exception e) {
                e.printStackTrace();
            }

            int exifOrientation =
                    exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int exifDegree = exifOrientationToDegree(exifOrientation);

            // 경로를 통해 비트맵으로 전환
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            imageWrite.setImageBitmap(rotate(bitmap, exifDegree));

        }

        // 사진 회전값 처리
        private int exifOrientationToDegree ( int exifOrientation){
            if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
                return 90;
            } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
                return 180;
            } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
                return 270;
            }
            return 0;
        }

        // 사진 정방향대로 회전하기
        private Bitmap rotate (Bitmap src,float degree){

            Matrix matrix = new Matrix();

            // 회전각도 셋팅
            matrix.postRotate(degree);

            // 이미지와 Matrix 를 셋팅해서 BitMap 객체 생성
            return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        }

        // 사진에 저장된 절대 경로값을 가져오는 메소드
        private String getRealPathFromURI (Uri contentUri){

            int index = 0;
            String[] proj = {MediaStore.Images.Media.DATA};

            Cursor cursor =
                    getContentResolver().query(contentUri, proj, null, null, null);

            if (cursor.moveToFirst()) {
                index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            }
            return cursor.getString(index);
        }

        public void postCancel (View view){
            // TODO : 취소버튼

        }

        public void postResult (View view){
            // TODO : 확인버튼
            String title = editTitle.getText().toString();
            String content = editBody.getText().toString();
            String id = String.valueOf(0);
            firebasePostAdd(id, title, content);


        }

        public void firebasePostAdd (String id, String title, String content) {

            Post p = new Post(title, content);
            p.setPostId(id);

            writeReference.child(id).setValue(p);

            int index = Integer.parseInt(id);
            index++;
            p.setPostId(String.valueOf(index));

        }

    }
