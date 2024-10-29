package com.example.a1200970_khuffashtd2;

import java.io.Serializable;

public class Question implements Serializable {
    private String Q;
    private String A;

    public Question() {
    }

    public Question(String q, String a) {
        Q = q;
        A = a;
    }

    public String getQ() {
        return Q;
    }

    public void setQ(String q) {
        Q = q;
    }

    public String getA() {
        return A;
    }

    public void setA(String a) {
        A = a;
    }
}
