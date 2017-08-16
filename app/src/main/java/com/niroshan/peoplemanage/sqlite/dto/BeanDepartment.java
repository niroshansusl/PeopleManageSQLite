package com.niroshan.peoplemanage.sqlite.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by niroshan on 8/16/2017.
 */

public class BeanDepartment implements Parcelable {
	private int id;
	private String name;

	public BeanDepartment() {
		super();
	}

	public BeanDepartment(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public BeanDepartment(String name) {
		this.name = name;
	}

	private BeanDepartment(Parcel in) {
		super();
		this.id = in.readInt();
		this.name = in.readString();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "id:" + id + ", name:" + name;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeInt(getId());
		parcel.writeString(getName());
	}

	public static final Creator<BeanDepartment> CREATOR = new Creator<BeanDepartment>() {
		public BeanDepartment createFromParcel(Parcel in) {
			return new BeanDepartment(in);
		}

		public BeanDepartment[] newArray(int size) {
			return new BeanDepartment[size];
		}
	};

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BeanDepartment other = (BeanDepartment) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
