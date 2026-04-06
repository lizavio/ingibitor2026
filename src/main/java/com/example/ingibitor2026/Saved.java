package com.example.ingibitor2026;

import java.io.Serializable;

public class Saved implements Serializable {

    String res;

    public Saved(String result){
        res = result;
    }

    public String getRes() {
        return res;
    }
}
