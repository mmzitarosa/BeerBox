/**
 * This file was generated by Api2Pojo
 * See <a href="https://github.com/mmzitarosa/Api2Pojo">https://github.com/mmzitarosa/Api2Pojo</a>
 * Any modifications to this file will be lost upon recompilation of the source schema.
 * Generated on: Sun Feb 23 15:53:31 CET 2020
 */

package com.punkapi.api2pojo.beers;

public class Boil_volume {

    private String unit;
    private Number value;

    public Boil_volume() {
    }

    public Boil_volume(String unit, Number value) {
        this.unit = unit;
        this.value = value;
    }

    public String getUnit() {
        return this.unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Number getValue() {
        return this.value;
    }

    public void setValue(Number value) {
        this.value = value;
    }

}