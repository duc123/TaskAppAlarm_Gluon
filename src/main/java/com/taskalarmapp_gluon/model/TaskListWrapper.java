/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.taskalarmapp_gluon.model;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author duc
 */
@XmlRootElement(name="tasks")
public class TaskListWrapper {
    private List<Task> tasks;
    
    @XmlElement(name="task")
    public List<Task> getTasks(){
        return tasks;
    }
    
    public void setTasks(List<Task> tasks){
        this.tasks = tasks;
    }
}
