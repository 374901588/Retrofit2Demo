package com.example.zero;

/**
 * Created by Zero on 2017/3/11.
 */

public class Contributor {

    public final String login;
    public final int id;
    public final int contributions;

    public Contributor(String login, int id, int contributions) {
        this.login = login;
        this.id=id;
        this.contributions = contributions;
    }

    @Override
    public String toString() {
        return "[" +login+",id="+id+",contributions="+contributions +"]";
    }
}
