package com.skilrock.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class FAQBean implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private boolean isSuccess;
	private String responseCode;
	private String responseMsg;
	private ArrayList<FAQData> faqData;

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMsg() {
		return responseMsg;
	}

	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}

	public ArrayList<FAQData> getFaqData() {
		return faqData;
	}

	public void setFaqData(ArrayList<FAQData> faqData) {
		this.faqData = faqData;
	}

	public class FAQData implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String head;
		private ArrayList<FAQ> faq;

		public String getHead() {
			return head;
		}

		public void setHead(String head) {
			this.head = head;
		}

		public ArrayList<FAQ> getFaq() {
			return faq;
		}

		public void setFaq(ArrayList<FAQ> faq) {
			this.faq = faq;
		}

	}

	public class FAQ implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String A;
		private String Q;

		public String getA() {
			return A;
		}

		public void setA(String a) {
			A = a;
		}

		public String getQ() {
			return Q;
		}

		public void setQ(String q) {
			Q = q;
		}

	}

}