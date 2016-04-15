package com.example.john.miniproj1httpasync;

/**
 * Created by John on 15/4/2016.
 */
public interface Callback {
    void success(Toilet toilet);
    void fail(Exception exception);
}
