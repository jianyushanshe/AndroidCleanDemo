package com.example.myapplication.cache;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class AppJunk implements Parcelable {
    public static final Parcelable.Creator<AppJunk> CREATOR = new Parcelable.Creator<AppJunk>() {
        @Override
        public AppJunk createFromParcel(Parcel source) {
            return new AppJunk(source);
        }

        @Override
        public AppJunk[] newArray(int size) {
            return new AppJunk[size];
        }
    };

    private boolean b;

    private boolean c;

    private String d;

    private Drawable e;

    private int f;

    private String g;

    private String h;

    private String i;

    private long j;

    private String k;

    private long l;

    private List<String> m;

    private Object n;

    protected AppJunk() {
        this.m = new ArrayList<String>();
    }

    protected AppJunk(Parcel paramParcel) {
        boolean bool2;
        this.m = new ArrayList<String>();
        byte b = paramParcel.readByte();
        boolean bool1 = true;
        if (b != 0) {
            bool2 = true;
        } else {
            bool2 = false;
        }
        this.b = bool2;
        if (paramParcel.readByte() != 0) {
            bool2 = bool1;
        } else {
            bool2 = false;
        }
        this.c = bool2;
        this.d = paramParcel.readString();
        this.f = paramParcel.readInt();
        this.g = paramParcel.readString();
        this.h = paramParcel.readString();
        this.i = paramParcel.readString();
        this.j = paramParcel.readLong();
        this.k = paramParcel.readString();
        this.l = paramParcel.readLong();
        this.m = paramParcel.createStringArrayList();
    }

    public void a(String paramString) {
        if (TextUtils.isEmpty(paramString))
            return;
        if (!this.m.contains(paramString))
            this.m.add(paramString);
    }

    public boolean b(Context paramContext) {
        if (l()) {
            for (String str : this.m) {
                if (!TextUtils.isEmpty(str))
                    FileUtil.b(str);
            }
        } else {
            String str;
            if (k()) {
                str = this.i;
            } else {
                str = this.k;
            }
            if (TextUtils.isEmpty(str))
                return false;
            FileUtil.b(str);
        }
        return true;
    }

    public Drawable c() {
        return this.e;
    }

    public String d() {
        return this.d;
    }

    public int describeContents() {
        return 0;
    }

    public String e() {
        return this.k;
    }

    public long f() {
        return this.l;
    }

    public String g() {
        return this.i;
    }

    public List<String> h() {
        return this.m;
    }

    public String i() {
        return this.h;
    }

    public Object j() {
        return this.n;
    }

    public boolean k() {
        return this.c;
    }

    public boolean l() {
        return this.b;
    }

    void m(Drawable paramDrawable) {
        this.e = paramDrawable;
    }

    void n(String paramString) {
        this.d = paramString;
    }

    void o(int paramInt) {
        this.f = paramInt;
    }

    void p(String paramString) {
        this.g = paramString;
    }

    void q(String paramString) {
        this.k = paramString;
    }

    void r(long paramLong) {
        this.l = paramLong;
    }

    public void s(String paramString) {
        this.i = paramString;
    }

    void t(long paramLong) {
        this.j = paramLong;
    }

    void u(String paramString) {
        this.h = paramString;
    }

    public void v(Object paramObject) {
        this.n = paramObject;
    }

    public void w(boolean paramBoolean) {
        this.b = paramBoolean;
    }

    public void writeToParcel(Parcel paramParcel, int paramInt) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            paramParcel.writeBoolean(this.b);
            paramParcel.writeBoolean(this.c);
        }
        paramParcel.writeString(this.d);
        paramParcel.writeInt(this.f);
        paramParcel.writeString(this.g);
        paramParcel.writeString(this.h);
        paramParcel.writeString(this.i);
        paramParcel.writeLong(this.j);
        paramParcel.writeString(this.k);
        paramParcel.writeLong(this.l);
        paramParcel.writeStringList(this.m);
    }
}
