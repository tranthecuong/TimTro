package com.example.cuongtran.timtro.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class FilterPacket implements Parcelable {
    Boolean refreshNeed;
    String Tinh;
    String Huyen;
    long minGia,maxGia;
    Long minDienTich,maxDienTich;

    public FilterPacket(Boolean refreshNeed, String tinh, String huyen, long minGia, long maxGia, Long minDienTich, Long maxDienTich) {
        this.refreshNeed = refreshNeed;
        Tinh = tinh;
        Huyen = huyen;
        this.minGia = minGia;
        this.maxGia = maxGia;
        this.minDienTich = minDienTich;
        this.maxDienTich = maxDienTich;
    }

    protected FilterPacket(Parcel in) {
        byte tmpRefreshNeed = in.readByte();
        refreshNeed = tmpRefreshNeed == 0 ? null : tmpRefreshNeed == 1;
        Tinh = in.readString();
        Huyen = in.readString();
        minGia = in.readLong();
        maxGia = in.readLong();
        if (in.readByte() == 0) {
            minDienTich = null;
        } else {
            minDienTich = in.readLong();
        }
        if (in.readByte() == 0) {
            maxDienTich = null;
        } else {
            maxDienTich = in.readLong();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (refreshNeed == null ? 0 : refreshNeed ? 1 : 2));
        dest.writeString(Tinh);
        dest.writeString(Huyen);
        dest.writeLong(minGia);
        dest.writeLong(maxGia);
        if (minDienTich == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(minDienTich);
        }
        if (maxDienTich == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(maxDienTich);
        }
    }

    public static final Creator<FilterPacket> CREATOR = new Creator<FilterPacket>() {
        @Override
        public FilterPacket createFromParcel(Parcel in) {
            return new FilterPacket(in);
        }

        @Override
        public FilterPacket[] newArray(int size) {
            return new FilterPacket[size];
        }
    };

    public Boolean getRefreshNeed() {
        return refreshNeed;
    }

    public void setRefreshNeed(Boolean refreshNeed) {
        this.refreshNeed = refreshNeed;
    }

    public String getTinh() {
        return Tinh;
    }

    public void setTinh(String tinh) {
        Tinh = tinh;
    }

    public String getHuyen() {
        return Huyen;
    }

    public void setHuyen(String huyen) {
        Huyen = huyen;
    }

    public long getMinGia() {
        return minGia;
    }

    public void setMinGia(long minGia) {
        this.minGia = minGia;
    }

    public long getMaxGia() {
        return maxGia;
    }

    public void setMaxGia(long maxGia) {
        this.maxGia = maxGia;
    }

    public Long getMinDienTich() {
        return minDienTich;
    }

    public void setMinDienTich(Long minDienTich) {
        this.minDienTich = minDienTich;
    }

    public Long getMaxDienTich() {
        return maxDienTich;
    }

    public void setMaxDienTich(Long maxDienTich) {
        this.maxDienTich = maxDienTich;
    }



    @Override
    public int describeContents() {
        return 0;
    }


//
//    public FilterPacket(int maTinh, int maHuyen, double gia1, double gia2, double dientich1, double dientich2) {
//        this.maTinh = maTinh;
//        this.maHuyen = maHuyen;
//        this.gia1 = gia1;
//        this.gia2 = gia2;
//        this.dientich1 = dientich1;
//        this.dientich2 = dientich2;
//    }
//
//    @Override
//    public String toString() {
//        return "FilterPacket{" +
//                "maTinh=" + maTinh +
//                ", maHuyen=" + maHuyen +
//                ", gia1=" + gia1 +
//                ", gia2=" + gia2 +
//                ", dientich1=" + dientich1 +
//                ", dientich2=" + dientich2 +
//                '}';
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(this.maTinh);
//        dest.writeInt(this.maHuyen);
//        dest.writeDouble(this.gia1);
//        dest.writeDouble(this.gia2);
//        dest.writeDouble(this.dientich1);
//        dest.writeDouble(this.dientich2);
//    }
//
//    protected FilterPacket(Parcel in) {
//        this.maTinh = in.readInt();
//        this.maHuyen = in.readInt();
//        this.gia1 = in.readDouble();
//        this.gia2 = in.readDouble();
//        this.dientich1 = in.readDouble();
//        this.dientich2 = in.readDouble();
//    }
//
//    public static final Parcelable.Creator<FilterPacket> CREATOR = new Parcelable.Creator<FilterPacket>() {
//        @Override
//        public FilterPacket createFromParcel(Parcel source) {
//            return new FilterPacket(source);
//        }
//
//        @Override
//        public FilterPacket[] newArray(int size) {
//            return new FilterPacket[size];
//        }
//    };
}
