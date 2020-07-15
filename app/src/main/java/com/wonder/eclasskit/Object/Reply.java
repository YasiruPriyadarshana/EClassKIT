package com.wonder.eclasskit.Object;

public class Reply {
    private String userrep;
    private String replydesc;
    private String urirep;
    private String stuId;

    public Reply() {
    }

    public Reply(String userrep, String replydesc, String urirep, String stuId) {
        this.userrep = userrep;
        this.replydesc = replydesc;
        this.urirep = urirep;
        this.stuId = stuId;
    }

    public Reply(String userrep, String replydesc, String urirep) {
        this.userrep = userrep;
        this.replydesc = replydesc;
        this.urirep = urirep;
    }

    public String getUserrep() {
        return userrep;
    }

    public void setUserrep(String userrep) {
        this.userrep = userrep;
    }

    public String getReplydesc() {
        return replydesc;
    }

    public void setReplydesc(String replydesc) {
        this.replydesc = replydesc;
    }

    public String getUrirep() {
        return urirep;
    }

    public void setUrirep(String urirep) {
        this.urirep = urirep;
    }

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }
}
