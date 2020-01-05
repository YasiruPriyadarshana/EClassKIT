package com.wonder.learnwithchirath.Object;

public class Reply {
    private String usercmt;
    private String commentdesc;
    private String uricmt;

    public Reply() {
    }

    public Reply(String usercmt, String commentdesc, String uricmt) {
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
}
