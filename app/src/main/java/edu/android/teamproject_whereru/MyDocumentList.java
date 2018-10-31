package edu.android.teamproject_whereru;



import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MyDocumentList extends AppCompatActivity {

    private ListView MDListView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_document_list);

        Intent intent = getIntent();


        MDListView = findViewById(R.id.MDListView);

        MDListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startPostDetailActivity();
            }
        });

        // 어댑터 등록
        dataSetting();
    }

    private void startPostDetailActivity() {
        Intent intent = new Intent(this, PostDetailActivity.class);
        startActivity(intent);
    }

    private void dataSetting() {
        DocumentAdapter adapter = new DocumentAdapter();

        /* 리스트뷰에 어댑터 등록 */
        MDListView.setAdapter(adapter);
    }
}












