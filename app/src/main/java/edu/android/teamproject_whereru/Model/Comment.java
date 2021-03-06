package edu.android.teamproject_whereru.Model;

import java.io.Serializable;

public class Comment implements Serializable {
    private String commentNumber;
    private String commentId;
    private String postId;
    private String guestId;
    private String content;

    public Comment() {}

    public String getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(String commentNumber) {
        this.commentNumber = commentNumber;
    }

    public Comment(String commentId, String content) {
        this.commentId = commentId;
        this.content = content;
    }

    public Comment(String commentNumber, String commentId, String content) {
        this.commentNumber = commentNumber;
        this.commentId = commentId;
        this.content = content;
    }

    public Comment(String commentId, String postId, String guestId, String content) {
        this.commentId = commentId;  //
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
                "commentNumber='" + commentNumber + '\'' +
                ", commentId='" + commentId + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

}
