package com.pointhouse.chiguan.k1_12;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gyh on 2017/7/26.
 */

public class Option implements Parcelable {

    // 选项号（1,2,3,4...)，1代表第一个选项，依次类推
    private String optionNo;
    // 选项号（表示用）
    private String optionNoLabel;
    // 选项内容
    private String optionValue;
    // 是否选择
    private boolean selected;

    public String getOptionNo() {
        return optionNo;
    }

    public void setOptionNo(String optionNo) {
        this.optionNo = optionNo;
    }

    public String getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getOptionNoLabel() {
        return optionNoLabel;
    }

    public void setOptionNoLabel(String optionNoLabel) {
        this.optionNoLabel = optionNoLabel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.optionNo);
        dest.writeString(this.optionNoLabel);
        dest.writeString(this.optionValue);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
    }

    public Option() {
    }

    protected Option(Parcel in) {
        this.optionNo = in.readString();
        this.optionNoLabel = in.readString();
        this.optionValue = in.readString();
        this.selected = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Option> CREATOR = new Parcelable.Creator<Option>() {
        @Override
        public Option createFromParcel(Parcel source) {
            return new Option(source);
        }

        @Override
        public Option[] newArray(int size) {
            return new Option[size];
        }
    };
}
