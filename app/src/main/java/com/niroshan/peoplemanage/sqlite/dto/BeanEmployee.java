package com.niroshan.peoplemanage.sqlite.dto;

import java.util.Date;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by niroshan on 8/16/2017.
 */

public class BeanEmployee implements Parcelable {

	private int id;
	private String name;
	private Date dateOfBirth;
	private double salary;

	private BeanDepartment beanDepartment;

	public BeanEmployee() {
		super();
	}

	private BeanEmployee(Parcel in) {
		super();
		this.id = in.readInt();
		this.name = in.readString();
		this.dateOfBirth = new Date(in.readLong());
		this.salary = in.readDouble();

		this.beanDepartment = in.readParcelable(BeanDepartment.class.getClassLoader());
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

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public BeanDepartment getBeanDepartment() {
		return beanDepartment;
	}

	public void setBeanDepartment(BeanDepartment beanDepartment) {
		this.beanDepartment = beanDepartment;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name + ", dateOfBirth="
				+ dateOfBirth + ", salary=" + salary + ", department="
				+ beanDepartment + "]";
	}

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
		BeanEmployee other = (BeanEmployee) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeInt(getId());
		parcel.writeString(getName());
		parcel.writeLong(getDateOfBirth().getTime());
		parcel.writeDouble(getSalary());
		parcel.writeParcelable(getBeanDepartment(), flags);
	}

	public static final Creator<BeanEmployee> CREATOR = new Creator<BeanEmployee>() {
		public BeanEmployee createFromParcel(Parcel in) {
			return new BeanEmployee(in);
		}

		public BeanEmployee[] newArray(int size) {
			return new BeanEmployee[size];
		}
	};

}
