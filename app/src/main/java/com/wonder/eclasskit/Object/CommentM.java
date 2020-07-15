package com.wonder.eclasskit.Object;


public class CommentM {
    private String usercmt;
    private String commentdesc;
    private String uricmt;
    private String stuId;


    public CommentM() {
    }

    public CommentM(String usercmt, String commentdesc, String uricmt, String stuId) {
        this.usercmt = usercmt;
        this.commentdesc = commentdesc;
        this.uricmt = uricmt;
        this.stuId = stuId;
    }

    public CommentM(String usercmt, String commentdesc, String uricmt) {
        this.usercmt = usercmt;
        this.commentdesc = commentdesc;
        this.uricmt = uricmt;
    }

    public String getUsercmt() {
        return usercmt;
    }

    public void setUsercmt(String usercmt) {
        this.usercmt = usercmt;
    }

    public String getCommentdesc() {
        return commentdesc;
    }

    public void setCommentdesc(String commentdesc) {
        this.commentdesc = commentdesc;
    }

    public String getUricmt() {
        return uricmt;
    }

    public void setUricmt(String uricmt) {
        this.uricmt = uricmt;
    }

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }
}
