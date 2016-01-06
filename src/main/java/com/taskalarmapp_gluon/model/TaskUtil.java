/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.taskalarmapp_gluon.model;

/**
 *
 * @author duc
 */
public class TaskUtil {

    public static int compareTaskByTime(Task task1, Task task2) {
        if(task1.getTime().getHour() > task2.getTime().getHour()) {
            return 1;
        }
        if (task1.getTime().getHour() == task2.getTime().getHour()) {
            if (task1.getTime().getMinute() > task2.getTime().getMinute()) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }
}
