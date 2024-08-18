package com.skilrock.bean;

import java.util.List;

public class StateModal {
	public List<StateList> stateList;
	public String errorMsg;
	public boolean isSuccess;

	public List<StateList> getStateList() {
		return stateList;
	}

	public void setStateList(List<StateList> stateList) {
		this.stateList = stateList;
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

	public class StateList {
		public String getStateName() {
			return stateName;
		}

		public void setStateName(String stateName) {
			this.stateName = stateName;
		}

		public String getStateCode() {
			return stateCode;
		}

		public void setStateCode(String stateCode) {
			this.stateCode = stateCode;
		}

		public String stateName;
		public String stateCode;
	}
}