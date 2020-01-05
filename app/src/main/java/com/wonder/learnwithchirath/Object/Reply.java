package com.wonder.learnwithchirath.Object;

public class Reply {
    private String userrep;
    private String replydesc;
    private String urirep;

    public Reply() {
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
}
