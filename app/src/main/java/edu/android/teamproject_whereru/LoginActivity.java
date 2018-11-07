package edu.android.teamproject_whereru;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.LogPrinter;
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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import edu.android.teamproject_whereru.Model.Guest;

// 로그인관련 기능을 처리하는 클래스
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private Button btnSignUp, btnLogin, btnGoogle_Login, btnFaceBook_Login;
    private EditText editLogId, editLogPw;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    private TextView textView;
    public static final int REQ_CODE = 100;
    private static final int RC_SIGN_IN = 1000;
    List<String> idList = new ArrayList<>();
    List<String> pwList = new ArrayList<>();
    private static final String UIDS = null;
    private static final String TAG = "teamproject_whereru";
    public static final String GOOGLE_REQUEST_CODE = "google";
    private static final String FIREBASE_GUEST_PW = "guestPw";
    private ChildEventListener childEventListener;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mReference;
    private static final String TBL_NAME = "guest";
    private Gson gson;
    private static final String SAVED_GUEST_DATA = "WhereRU_Guest_Data";
    private static final String GUEST_DATA = "guestData";
    private SharedPreferences sharedPreferences;


    public LoginActivity() {
    }

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
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        editLogId = findViewById(R.id.editLogId);
        editLogPw = findViewById(R.id.editLogPw);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String log_id = editLogId.getText().toString();
                final String log_pw = editLogPw.getText().toString();
                firebaseDatabase = FirebaseDatabase.getInstance();
//                Log.i(TAG, firebaseDatabase.toString());
                mReference = firebaseDatabase.getReference(TBL_NAME);

                // ku8230, ku82301, ku82302
                // mReferece 참조할 위치를 나타냄 child를 추가하면 세부항목으로 들어감


                childEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        if(dataSnapshot.getKey().equals(log_id)) {
                            if(dataSnapshot.child(FIREBASE_GUEST_PW).getValue().toString().equals(log_pw)) {
                                MainActivity.guestList = dataSnapshot.getValue(Guest.class);
                                MainActivity.guestList.setGuestId(log_id);
                                onSaveGuestData(MainActivity.guestList, getApplicationContext());
                                Toast.makeText(LoginActivity.this, "로그인 성공!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Toast.makeText(LoginActivity.this, "잘못된 비밀번호 입력!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } // end OnchildAdded()

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
                    } // end onCancelled
                }; // end childEventListner
                mReference.addChildEventListener(childEventListener);

            }
        });


        mAuth = FirebaseAuth.getInstance();

//        btnGoogle_Login = findViewById(R.id.btnGoogle_Login);
//        btnGoogle_Login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//                startActivityForResult(signInIntent,RC_SIGN_IN);
//            }
//        });

    }
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
//                                list = new ArrayList<>();
//                                list.add(builder.toString());
//                                // TODO: 로그인 성공시 로그인한 유저의 정보를 가지고 MainActivity로 이동하도록 코드 작성
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

    public void onSaveGuestData(Guest guest, Context context) {
        gson = new Gson();
        Log.i("aaa", "guestGson값: " + guest.toString());
        String guestToGson = gson.toJson(guest, Guest.class);
        sharedPreferences = context.getSharedPreferences(GUEST_DATA, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SAVED_GUEST_DATA, guestToGson);
        editor.commit();


    }


}