package com.conicskill.app.data.model.carousel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Options implements Serializable {

	@SerializedName("A")
	private String A;

	@SerializedName("B")
	private String B;

	@SerializedName("C")
	private String C;

	@SerializedName("D")
	private String D;

	public void setA(String A){
		this.A = A;
	}

	public String getA(){
		return A;
	}

	public void setB(String B){
		this.B = B;
	}

	public String getB(){
		return B;
	}

	public void setC(String C){
		this.C = C;
	}

	public String getC(){
		return C;
	}

	public void setD(String D){
		this.D = D;
	}

	public String getD(){
		return D;
	}
}