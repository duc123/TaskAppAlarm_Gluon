/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.taskalarmapp_gluon.model;

import java.time.LocalTime;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author duc
 */
public class LocalTimeAdapter extends XmlAdapter<String, LocalTime>{

    @Override
    public LocalTime unmarshal(String v) throws Exception {
        return LocalTime.parse(v);
    }

    @Override
    public String marshal(LocalTime v) throws Exception {
        return v.toString();
    }
    
}
