package com.laiyl;

public class LgPortsPO {
    private int id;
    private String level;
    private String areaCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    @Override
    public String toString() {
        return "LgPortsPO{" +
                "id=" + id +
                ", level='" + level + '\'' +
                ", areaCode='" + areaCode + '\'' +
                '}';
    }
}
