package com.skilrock.bean;

import java.util.List;

public class CityModal {
	public List<CityList> cityList;
	public String errorMsg;
	public boolean isSuccess;

	public List<CityList> getCityList() {
		return cityList;
	}

	public void setCityList(List<CityList> cityList) {
		this.cityList = cityList;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public class CityList {
		public String cityName;
		public String cityCode;

		public String getCityName() {
			return cityName;
		}

		public void setCityName(String cityName) {
			this.cityName = cityName;
		}

		public String getCityCode() {
			return cityCode;
		}

		public void setCityCode(String cityCode) {
			this.cityCode = cityCode;
		}
	}
}