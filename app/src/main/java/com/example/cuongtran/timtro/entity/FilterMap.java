package com.example.cuongtran.timtro.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class FilterMap implements Parcelable {
    boolean freshNeed;
    float minGia,maxGia;
    long minDienTich,maxDienTich;

    public FilterMap(boolean freshNeed, float minGia, float maxGia, long minDienTich, long maxDienTich) {
        this.freshNeed = freshNeed;
        this.minGia = minGia;
        this.maxGia = maxGia;
        this.minDienTich = minDienTich;
        this.maxDienTich = maxDienTich;
    }

    public boolean isFreshNeed() {
        return freshNeed;
    }

    public void setFreshNeed(boolean freshNeed) {
        this.freshNeed = freshNeed;
    }

    public float getMinGia() {
        return minGia;
    }

    public void setMinGia(float minGia) {
        this.minGia = minGia;
    }

    public float getMaxGia() {
        return maxGia;
    }

    public void setMaxGia(float maxGia) {
        this.maxGia = maxGia;
    }

    public long getMinDienTich() {
        return minDienTich;
    }

    public void setMinDienTich(long minDienTich) {
        this.minDienTich = minDienTich;
    }

    public long getMaxDienTich() {
        return maxDienTich;
    }

    public void setMaxDienTich(long maxDienTich) {
        this.maxDienTich = maxDienTich;
    }

    protected FilterMap(Parcel in) {
        freshNeed = in.readByte() != 0;
        minGia = in.readFloat();
        maxGia = in.readFloat();
        minDienTich = in.readLong();
        maxDienTich = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (freshNeed ? 1 : 0));
        dest.writeFloat(minGia);
        dest.writeFloat(maxGia);
        dest.writeLong(minDienTich);
        dest.writeLong(maxDienTich);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FilterMap> CREATOR = new Creator<FilterMap>() {
        @Override
        public FilterMap createFromParcel(Parcel in) {
            return new FilterMap(in);
        }

        @Override
        public FilterMap[] newArray(int size) {
            return new FilterMap[size];
        }
    };
}
