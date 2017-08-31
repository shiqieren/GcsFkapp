package com.gcs.fengkong.ui.bean;

import android.graphics.Bitmap;

public class ContactVo {

	private long id;
	private String name;
	private String mobile;
	private String company;

	public ContactVo() {
		super();
	}

	public ContactVo(long id, String name, String mobile, String company) {
		super();
		this.id = id;
		this.name = name;
		this.mobile = mobile;
		this.company = company;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	@Override
	public String toString() {
		return "ContactVo [id=" + id + ", name=" + name + ", moble=" + mobile
				+ ", company=" + company + "]";
	}


}
