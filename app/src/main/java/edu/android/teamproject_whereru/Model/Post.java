package edu.android.teamproject_whereru.Model;

import android.net.Uri;

import java.io.Serializable;

public class Post implements Serializable {

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    // 작성자 아이디
    private String postKey;
    private String guestId;
    // 날짜
    private String today;

    // 제목
    private String title;
    // 사진
    private String image;
    // 내용
    private String content;
    // 뷰 숫자
    private int viewCount;


    public Post(){}


    public Post(String guestId, String content) {
        this.guestId = guestId;
        this.content = content;
    }



    // 글쓰기 생성자 전용
    public Post(String postKey , String guestId, String today, String title, String image, String content) {
        this.postKey = postKey;
        this.guestId = guestId;
        this.today = today;
        this.title = title;
        this.image = image;
        this.content = content;
    }
    // PostMainFragment에 쓸 생성자

    public Post(String postKey,String guestId, String today, String title, String image, String content, int viewCount) {
        this.postKey = postKey;
        this.guestId = guestId;
        this.today = today;
        this.title = title;
        this.image = image;
        this.content = content;
        this.viewCount = viewCount;
    }


    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    @Override
    public String toString() {
        return "Post{" +
                "postKey='" + postKey + '\'' +
                ", guestId='" + guestId + '\'' +
                ", today='" + today + '\'' +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", content='" + content + '\'' +
                ", viewCount=" + viewCount +
                '}';
    }
}
