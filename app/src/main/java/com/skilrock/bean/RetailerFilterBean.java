package com.skilrock.bean;

import java.util.List;

public class RetailerFilterBean {
    private List<String> types;
    private List<String> services;
    private boolean[] typesChecked;
    private boolean[] servicesChecked;

    public int[] getTypesRes() {
        return typesRes;
    }

    private int[] typesRes;

    public int[] getServicesRes() {
        return servicesRes;
    }

    private int[] servicesRes;

    public RetailerFilterBean(List<String> types, List<String> services,
                              boolean[] typesChecked, boolean[] servicesChecked, int[] typesRes,
                              int[] servicesRes) {
        super();
        this.typesRes = typesRes;
        this.servicesRes = servicesRes;
        this.types = types;
        this.services = services;
        this.typesChecked = typesChecked;
        this.servicesChecked = servicesChecked;
    }

    public List<String> getTypes() {
        return types;
    }

    public List<String> getServices() {
        return services;
    }

    public boolean[] getTypesChecked() {
        return typesChecked;
    }

    public boolean[] getServicesChecked() {
        return servicesChecked;
    }

}