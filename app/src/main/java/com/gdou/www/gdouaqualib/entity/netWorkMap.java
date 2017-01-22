package com.gdou.www.gdouaqualib.entity;

import java.util.Map;

/**
 * Created by Veyron on 2017/1/22.
 * Function：存放两个重要的map
 */
public class netWorkMap {
    private Map<String, Object> mapList;
    private Map<String, Object> mapTree;

    public netWorkMap(){

    }
    private static class LazyHolder {
        private static final netWorkMap INSTANCE = new netWorkMap();
    }
    public static final netWorkMap getInstance() {
        return LazyHolder.INSTANCE;
    }
    public Map<String, Object> getMapList() {
        return mapList;
    }

    public void setMapList(Map<String, Object> mapList) {
        this.mapList = mapList;
    }


    public Map<String, Object> getMapTree() {
        return mapTree;
    }

    public void setMapTree(Map<String, Object> mapTree) {
        this.mapTree = mapTree;
    }

}
