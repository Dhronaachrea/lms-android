package com.skilrock.bean;

public class TicketsFilterBean {
    private String[] types;
    private String[] services;
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

    public TicketsFilterBean(String[] types, String[] services,
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

    public String[] getTypes() {
        return types;
    }

    public String[] getServices() {
        return services;
    }

    public boolean[] getTypesChecked() {
        return typesChecked;
    }

    public boolean[] getServicesChecked() {
        return servicesChecked;
    }

}
