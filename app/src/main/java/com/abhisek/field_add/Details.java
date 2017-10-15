package com.abhisek.field_add;

import java.io.Serializable;

/**
 * Created by bapu on 3/19/2017.
 */

public class Details implements Serializable {
    private String name;
    private String id;

    public Details(String n, String e) { name = n; id = e; }

    public String getName() { return name; }
    public String getId() { return id; }

    @Override
    public String toString() {
        return  name; }



}