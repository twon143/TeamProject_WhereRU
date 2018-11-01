package edu.android.teamproject_whereru;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import edu.android.teamproject_whereru.Model.Post;

public class DocumentAdapter extends BaseAdapter {

    /* 아이템을 세트로 담기 위한 어레이 */
    private ArrayList<Post> pItems = new ArrayList<>();

    TextView textTitle_1, textDate_1, textViews_1, textLike_1;

    @Override
    public int getCount() {
        return pItems.size();
    }

    @Override
    public Object getItem(int position) {
        return pItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        /* 'my_document_item' Layout을 inflate하여 convertView 참조 획득 */
        if (convertView ==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.my_document_item, parent, false);
        }

        /* 'my_document_item'에 정의된 위젯에 대한 참조 획득 */
        textTitle_1 = convertView.findViewById(R.id.textTitle_1);
        textDate_1 = convertView.findViewById(R.id.textDate_1);
        textViews_1 = convertView.findViewById(R.id.textViews_1);
        textLike_1 = convertView.findViewById(R.id.textLike_1);

        /* 각 리스트에 뿌려줄 아이템을 받아오는데 post 재활용 */
        Post post = (Post) getItem(position);

        /* 각 위젯에 세팅된 아이템을 뿌려준다 */

        textTitle_1.setText(post.getTitle());
//        textDate_1.setText(post.getdCreated());
        textViews_1.setText(post.getViewCount());
        textLike_1.setText(post.getRecommendation());

        /* (위젯에 대한 이벤트리스너를 지정하고 싶다면 여기에 작성하면된다..)  */

        return convertView;
    }

     /* 아이템 데이터 추가를 위한 함수. */
    public void addItem(String Title_1, String Date_1, int Views_1, int Like_1) {
        Post post = new Post();

        /* post에 아이템을 seting한다 */
        post.setTitle(Title_1);
//        post.setdCreated(Date_1);
        post.setViewCount(Views_1);
        post.setRecommendation(Like_1);

        /* pItems에 post을 추가한다.*/
        pItems.add(post);
    }
}
