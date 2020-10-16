package com.example.matcha_memo_app_android.DTO;

public class Memo {
    private long id = -1;
    private String name = "";
    private String createdAt;

    public final long getId() {
        return this.id;
    }

    public final void setId(long var1) {
        this.id = var1;
    }

    public final String getName() {
        return this.name;
    }

    public final void setName( String name) {
        this.name = name;
    }


    public final String getCreatedAt() {
        return this.createdAt;
    }

    public final void setCreatedAt(String date) {
        this.createdAt = date;
    }

}
