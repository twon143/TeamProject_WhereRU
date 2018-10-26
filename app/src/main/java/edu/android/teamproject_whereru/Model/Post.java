package edu.android.teamproject_whereru.Model;

import java.io.Serializable;

public class Post implements Serializable {

    private String postId;
    // 제목
    private String title;
    // 내용
    private String content;
    // 뷰 숫자
    private int viewCount;
    // 추천수
    private int recommendation;
    private String image;
    private String guestId;

    private int imageTest;

    public Post(){}



    // RecyclerView에 List로 보여주기 위해서 필요한 것들만 만듬
    // Test 생성자 dummydata
    public Post(String image, String guestId, int recommendation) {
        this.image = image;
        this.guestId = guestId;
        this.recommendation = recommendation;
    }

    public Post(int imageTest, String guestId, int recommendation) {
        this.imageTest = imageTest;
        this.guestId = guestId;
        this.recommendation = recommendation;
    }

    public Post(String postId, String title,
                String content, int viewCount, int recommendation, String image, String guestId) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.recommendation = recommendation;
        this.image = image;
        this.guestId = guestId;
    }

    public int getImageTest() {
        return imageTest;
    }

    public void setImageTest(int imageTest) {
        this.imageTest = imageTest;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(int recommendation) {
        this.recommendation = recommendation;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    @Override
    public String toString() {
        return "Post{" +
                "postId='" + postId + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", viewCount=" + viewCount +
                ", recommendation=" + recommendation +
                ", image='" + image + '\'' +
                ", guestId='" + guestId + '\'' +
                '}';
    }

}
