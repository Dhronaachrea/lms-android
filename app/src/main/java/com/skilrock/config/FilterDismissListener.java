package com.skilrock.config;

public interface FilterDismissListener {
	void onDismissFilterDiloag(String[] typesResult, String[] servicesResult);

	void onCancelFilterDiloag(boolean[] types, boolean[] services);
}
