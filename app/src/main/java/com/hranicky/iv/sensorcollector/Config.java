package com.hranicky.iv.sensorcollector;

/**
 * Created by Aďo on 18.02.2017.
 */
public class Config {
    //Address of our scripts of the CRUD
    public static final String URL_ADD="http://147.175.98.76:443/~xhranicky/mysql/CRUD/SCadd.php";
    public static final String URL_GET_ALL = "http://147.175.98.76:443/~xhranicky/mysql/CRUD/getAll.php";
    public static final String URL_GET_EMP = "http://147.175.98.76:443/~xhranicky/mysql/CRUD/get.php?id=";
    public static final String URL_UPDATE_EMP = "http://147.175.98.76:443/~xhranicky/mysql/CRUD/update.php";
    public static final String URL_DELETE_EMP = "http://147.175.98.76:443/~xhranicky/mysql/CRUD/delete.php?id=";

    //Keys that will be used to send the request to php scripts
    public static final String KEY_ID = "id";
    public static final String KEY_TYP = "typ";
    public static final String KEY_SUBOROV = "suborov";
    public static final String KEY_VELKOST = "velkost";
    public static final String KEY_CAS = "cas";
    public static final String KEY_DATUM = "datum";

    //JSON Tags
    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_ID = "id";
    public static final String TAG_NAME = "name";
    public static final String TAG_DESG = "desg";
    public static final String TAG_SAL = "salary";

    //employee id to pass with intent
    public static final String EMP_ID = "emp_id";
}
