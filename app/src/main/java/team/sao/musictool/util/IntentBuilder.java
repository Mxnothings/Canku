package team.sao.musictool.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * \* Author: MrWangx
 * \* Date: 2019/5/31
 * \* Time: 16:20
 * \* Description:
 **/
public class IntentBuilder {

    private Intent intent;

    public IntentBuilder() {
        this.intent = new Intent();
    }

    public IntentBuilder reset() {
        intent = null;
        intent = new Intent();
        return this;
    }

    public IntentBuilder action(String action) {
        intent.setAction(action);
        return this;
    }

    public Intent build() {
        return this.intent;
    }

    public void send(Context context) {
        context.sendBroadcast(this.intent);
    }

    public IntentBuilder extra(String name, boolean value) {
        intent.putExtra(name, value);
        return this;
    }

    public IntentBuilder extra(String name, byte value) {
        intent.putExtra(name, value);
        return this;
    }

    public IntentBuilder extra(String name, char value) {
        intent.putExtra(name, value);
        return this;
    }

    public IntentBuilder extra(String name, short value) {
        intent.putExtra(name, value);
        return this;
    }

    public IntentBuilder extra(String name, int value) {
        intent.putExtra(name, value);
        return this;
    }

    public IntentBuilder extra(String name, long value) {
        intent.putExtra(name, value);
        return this;
    }

    public IntentBuilder extra(String name, float value) {
        intent.putExtra(name, value);
        return this;
    }

    public IntentBuilder extra(String name, double value) {
        intent.putExtra(name, value);
        return this;
    }

    public IntentBuilder extra(String name, String value) {
        intent.putExtra(name, value);
        return this;
    }

    public IntentBuilder extra(String name, CharSequence value) {
        intent.putExtra(name, value);
        return this;
    }

    public IntentBuilder extra(String name, Parcelable value) {
        intent.putExtra(name, value);
        return this;
    }

    public IntentBuilder extra(String name, Serializable value) {
        intent.putExtra(name, value);
        return this;
    }

    public IntentBuilder extra(String name, Bundle value) {
        intent.putExtra(name, value);
        return this;
    }

    public IntentBuilder addFlags(int flags) {
        intent.addFlags(flags);
        return this;
    }

    public IntentBuilder clazz(Context context, Class clazz) {
        intent.setClass(context, clazz);
        return this;
    }




}
