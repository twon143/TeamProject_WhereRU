package edu.android.teamproject_whereru;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.android.teamproject_whereru.Model.Guest;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private Button btnSignUp, btnLogin, btnGoogle_Login, btnFaceBook_Login;
    private EditText editId, editPw;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    private TextView textView;
    public static final int REQ_CODE = 100;
    private static final int RC_SIGN_IN = 1000;
    List<String> list;
    private static final String TAG = "teamproject_whereru";
    public static final String GOOGLE_REQUEST_CODE = "google";
    private static final String FIREBASE_GUEST_PW = "guestPw";
    private ChildEventListener childEventListener;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mReference;
    private static final String TBL_NAME = "guest";


    public LoginActivity() {}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("로그인");
        final GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        editId = findViewById(R.id.editId);
        editPw = findViewById(R.id.editPw);



        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String log_id = editId.getText().toString();
//                Log.i(TAG, log_id); 입력한 아이디, 비번은 보임
                String log_pw = editPw.getText().toString();
                firebaseDatabase = FirebaseDatabase.getInstance();
//                Log.i(TAG, firebaseDatabase.toString());
                mReference = firebaseDatabase.getInstance().getReference(TBL_NAME).child(log_id);


                childEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        String id = dataSnapshot.getKey();
                        if(id.equals(FIREBASE_GUEST_PW)) {
                            String userData = dataSnapshot.getValue().toString();
                            if(userData.equals(editPw.getText().toString())) {
                                Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                            }

                        }
//                        Log.i(TAG, "key값" + mReference.getKey());



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
                mReference.addChildEventListener(childEventListener);
            }
        });


        mAuth = FirebaseAuth.getInstance();

        btnGoogle_Login = findViewById(R.id.btnGoogle_Login);
        btnGoogle_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent,RC_SIGN_IN);
            }
        });

    }

//    private void login(String log_id, String log_pw) {
//        if(dataSnapshot.hasChild("log_id")) {
//            Guest guest = dataSnapshot.getValue(Guest.class);
//
//
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                //구글 로그인 성공해서 파베에 인증
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
            else{
                //구글 로그인 실패
                Toast.makeText(this, "로그인 인증 실패", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct){
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(),null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "구글 계정 인증 실패", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(LoginActivity.this, "구글 로그인 인증 성공", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getInstance().getCurrentUser();
                            if(user !=null) {
                                String userName = user.getDisplayName();
                                String userEmail = user.getEmail();
                                StringBuilder builder = new StringBuilder();
                                builder.append(userName).append("\n").append(userEmail);
                                String googleLoginedInfo = builder.toString();
                                list = new ArrayList<>();
                                list.add(builder.toString());
                                // TODO: 로그인 성공시 로그인한 유저의 정보를 가지고 MainActivity로 이동하도록 코드 작성
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra(GOOGLE_REQUEST_CODE, googleLoginedInfo);
                                startActivityForResult(intent, REQ_CODE);
                            }


                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



    /**
     * @param view
     * @return null
     * @deprecated 로그아웃 버튼을 누를시에 로그아웃 되도록 하는 기능
     **/
    public void userLogout(View view) {
        FirebaseAuth.getInstance().signOut();
    }

    // 회원가입 이벤트를 처리하는 onCLick()
    public void SignUp(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }


}