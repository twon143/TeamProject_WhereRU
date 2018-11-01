package edu.android.teamproject_whereru;

import android.Manifest;
import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    private static final int REQ_CODE = 10;
    private static final int GALLERY_CODE = 111;
    private ImageView imagePuppy;
    private Bitmap bitmap;
    private Uri imagUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        imagePuppy = findViewById(R.id.imagePuppy);
    }

    public void changeProfile(View view) {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}
                , REQ_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQ_CODE:
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {

            sendPicture(data.getData());
        }
    }

    private void sendPicture(Uri imgUri) {
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
        bitmap = BitmapFactory.decodeFile(imagePath);
        imagePuppy.setImageBitmap(rotate(bitmap, exifDegree));

        // 갤러리에서 선택된 사진파일 멤버에 저장
        imagUri = imgUri;

    }

    // 사진 회전값 처리
    private int exifOrientationToDegree(int exifOrientation) {
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
    private Bitmap rotate(Bitmap src, float degree) {

        Matrix matrix = new Matrix();

        // 회전각도 셋팅
        matrix.postRotate(degree);

        // 이미지와 Matrix 를 셋팅해서 BitMap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    // 사진에 저장된 절대 경로값을 가져오는 메소드
    private String getRealPathFromURI(Uri contentUri) {

        int index = 0;
        String[] proj = {MediaStore.Images.Media.DATA};

        Cursor cursor =
                getContentResolver().query(contentUri, proj, null, null, null);

        if (cursor.moveToFirst()) {
            index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }
        return cursor.getString(index);
    }
}
