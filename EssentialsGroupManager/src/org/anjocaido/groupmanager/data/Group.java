/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anjocaido.groupmanager.data;

import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.dataholder.WorldDataHolder;
import org.anjocaido.groupmanager.events.GMGroupEvent.Action;
import org.anjocaido.groupmanager.events.GroupManagerEventHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *
 * @author gabrielcouto/ElgarL
 */
public class Group extends DataUnit implements Cloneable {

    /**
     * The group it inherits DIRECTLY!
     */
    private ArrayList<String> inherits = new ArrayList<String>();
    /**
     *This one holds the fields in INFO node.
     * like prefix = 'c'
     * or build = false
     */
    private GroupVariables variables = new GroupVariables(this);

    /**
     * Constructor for individual World Groups.
     *
     * @param name
     */
    public Group(WorldDataHolder source, String name) {
        super(source, name);
    }
    
    /**
     *  Constructor for Global Groups.
     *  
     * @param name
     */
    public Group(String name) {
        super(name);
    }
	
    /**
     * Clone this group
     * @return a clone of this group
     */
    @Override
    public Group clone() {
    	Group clone;
    	
    	if (getDataSource() == null) {
    		clone = new Group(this.getName());
    	} else {
    		clone = new Group(getDataSource(), this.getName());
    		clone.inherits = new ArrayList<String>(this.getInherits());
    	}
    	
        for (String perm : this.getPermissionList()) {
            clone.addPermission(perm);
        }
        clone.variables = ((GroupVariables) variables).clone(clone);
        //clone.flagAsChanged();
        return clone;
    }

    /**
     * Use this to deliver a group from a different dataSource to another
     * @param dataSource
     * @return Null or Clone
     */
    public Group clone(WorldDataHolder dataSource) {
        if (dataSource.groupExists(this.getName())) {
            return null;
        }
        
        Group clone = dataSource.createGroup(this.getName());
        
        // Don't add inheritance for GlobalGroups
        if (getDataSource() != null) {
    		clone.inherits = new ArrayList<String>(this.getInherits());
    	}
        for (String perm : this.getPermissionList()) {
            clone.addPermission(perm);
        }
        clone.variables = variables.clone(clone);
        clone.flagAsChanged(); //use this to make the new dataSource save the new group
        return clone;
    }

    /**
     * an unmodifiable list of inherits list
     * You can't manage the list by here
     * Lol... version 0.6 had a problem because this.
     * @return the inherits
     */
    public List<String> getInherits() {
        return Collections.unmodifiableList(inherits);
    }

    /**
     * @param inherit the inherits to set
     */
    public void addInherits(Group inherit) {
        if (!this.getDataSource().groupExists(inherit.getName())) {
            getDataSource().addGroup(inherit);
        }
        if (!inherits.contains(inherit.getName().toLowerCase())) {
            inherits.add(inherit.getName().toLowerCase());
        }
        flagAsChanged();
        if (GroupManager.isLoaded()) {
        	GroupManager.BukkitPermissions.updateAllPlayers();
        	GroupManagerEventHandler.callEvent(this, Action.GROUP_INHERITANCE_CHANGED);
        }
    }

    public boolean removeInherits(String inherit) {
        if (this.inherits.contains(inherit.toLowerCase())) {
            this.inherits.remove(inherit.toLowerCase());
            flagAsChanged();
            GroupManagerEventHandler.callEvent(this, Action.GROUP_INHERITANCE_CHANGED);
            return true;
        }
        return false;
    }

    /**
     * @return the variables
     */
    public GroupVariables getVariables() {
        return variables;
    }

    /**
     *
     * @param varList
     */
    public void setVariables(Map<String, Object> varList) {
        GroupVariables temp = new GroupVariables(this, varList);
        variables.clearVars();
        for (String key : temp.getVarKeyList()) {
            variables.addVar(key, temp.getVarObject(key));
        }
        flagAsChanged();
        if (GroupManager.isLoaded()) {
        	GroupManager.BukkitPermissions.updateAllPlayers();
        	GroupManagerEventHandler.callEvent(this, Action.GROUP_INFO_CHANGED);
        }
    }
}
