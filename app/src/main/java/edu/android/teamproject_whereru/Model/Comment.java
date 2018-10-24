package edu.android.teamproject_whereru.Model;

public class Comment {

    private String commentId;
    private String postId;
    private String guestId;
    private String content;

    public Comment() {}

    public Comment(String commentId, String postId, String guestId, String content) {
        this.commentId = commentId;
        this.postId = postId;
        this.guestId = guestId;
        this.content = content;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId='" + commentId + '\'' +
                ", postId='" + postId + '\'' +
                ", guestId='" + guestId + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

}
