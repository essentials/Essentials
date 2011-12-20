/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anjocaido.groupmanager.data;

//import com.sun.org.apache.bcel.internal.generic.AALOAD;
import java.util.ArrayList;

import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.dataholder.WorldDataHolder;
import org.anjocaido.groupmanager.events.GMUserEvent.Action;
import org.anjocaido.groupmanager.events.GroupManagerEventHandler;

import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


/**
 * 
 * @author gabrielcouto/ElgarL
 */
public class User extends DataUnit implements Cloneable {

	/**
     *
     */
	private String group = null;
	private ArrayList<String> subGroups = new ArrayList<String>();
	/**
	 * This one holds the fields in INFO node. like prefix = 'c' or build =
	 * false
	 */
	private UserVariables variables = new UserVariables(this);
	private transient Player bukkitPlayer = null;

	/**
	 * 
	 * @param name
	 */
	public User(WorldDataHolder source, String name) {
		super(source, name);
		this.group = source.getDefaultGroup().getName();
	}

	/**
	 * 
	 * @return User clone
	 */
	@Override
	public User clone() {
		User clone = new User(getDataSource(), this.getName());
		clone.group = this.group;
		for (String perm : this.getPermissionList()) {
			clone.addPermission(perm);
		}
		// clone.variables = this.variables.clone();
		// clone.flagAsChanged();
		return clone;
	}

	/**
	 * Use this to deliver a user from one WorldDataHolder to another
	 * 
	 * @param dataSource
	 * @return null if given dataSource already contains the same user
	 */
	public User clone(WorldDataHolder dataSource) {
		if (dataSource.isUserDeclared(this.getName())) {
			return null;
		}
		User clone = dataSource.createUser(this.getName());
		if (dataSource.getGroup(group) == null) {
			clone.setGroup(dataSource.getDefaultGroup());
		} else {
			clone.setGroup(dataSource.getGroup(this.getGroupName()));
		}
		for (String perm : this.getPermissionList()) {
			clone.addPermission(perm);
		}
		clone.variables = this.variables.clone(this);
		clone.flagAsChanged();
		return clone;
	}

	public Group getGroup() {
		Group result = getDataSource().getGroup(group);
		if (result == null) {
			this.setGroup(getDataSource().getDefaultGroup());
			result = getDataSource().getDefaultGroup();
		}
		return result;
	}

	/**
	 * @return the group
	 */
	public String getGroupName() {
		Group result = getDataSource().getGroup(group);
		if (result == null) {
			group = getDataSource().getDefaultGroup().getName();
		}
		return group;
	}

	/**
	 * @param group
	 *            the group to set
	 */
	@Deprecated
	public void setGroup(String group) {
		this.group = group;
		flagAsChanged();
		if (GroupManager.isLoaded())
			if (GroupManager.BukkitPermissions.player_join = false)
				GroupManager.BukkitPermissions.updateAllPlayers();
	}

	/**
	 * @param group
	 *            the group to set
	 */
	public void setGroup(Group group) {
		if (!this.getDataSource().groupExists(group.getName())) {
			getDataSource().addGroup(group);
		}
		group = getDataSource().getGroup(group.getName());
		String oldGroup = this.group;
		this.group = group.getName();
		flagAsChanged();
		if (GroupManager.isLoaded()) {
			if (GroupManager.BukkitPermissions.player_join = false)
				GroupManager.BukkitPermissions.updateAllPlayers();

			// Do we notify of the group change?
			String defaultGroupName = getDataSource().getDefaultGroup().getName();
			// if we were not in the default group
			// or we were in the default group and the move is to a different
			// group.
			boolean notify = (!oldGroup.equalsIgnoreCase(defaultGroupName)) || ((oldGroup.equalsIgnoreCase(defaultGroupName)) && (!this.group.equalsIgnoreCase(defaultGroupName)));

			if (notify)
				GroupManager.notify(this.getName(), String.format(" moved to the group %s.", group.getName()));
			
			GroupManagerEventHandler.callEvent(this, Action.USER_GROUP_CHANGED);
		}
	}

	public void addSubGroup(Group subGroup) {
		if (this.group.equalsIgnoreCase(subGroup.getName())) {
			return;
		}
		if (!this.getDataSource().groupExists(subGroup.getName())) {
			getDataSource().addGroup(subGroup);
		}
		subGroup = getDataSource().getGroup(subGroup.getName());
		removeSubGroup(subGroup);
		subGroups.add(subGroup.getName());
		flagAsChanged();
		if (GroupManager.isLoaded()) {
			if (GroupManager.BukkitPermissions.player_join = false)
				GroupManager.BukkitPermissions.updateAllPlayers();
			GroupManagerEventHandler.callEvent(this, Action.USER_SUBGROUP_CHANGED);
		}
	}

	public int subGroupsSize() {
		return subGroups.size();
	}

	public boolean isSubGroupsEmpty() {
		return subGroups.isEmpty();
	}

	public boolean containsSubGroup(Group subGroup) {
		return subGroups.contains(subGroup.getName());
	}

	public boolean removeSubGroup(Group subGroup) {
		try {
			if (subGroups.remove(subGroup.getName())) {
				flagAsChanged();
				if (GroupManager.isLoaded())
					if (GroupManager.BukkitPermissions.player_join = false)
						GroupManager.BukkitPermissions.updateAllPlayers();
				GroupManagerEventHandler.callEvent(this, Action.USER_SUBGROUP_CHANGED);
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	public ArrayList<Group> subGroupListCopy() {
		ArrayList<Group> val = new ArrayList<Group>();
		for (String gstr : subGroups) {
			Group g = getDataSource().getGroup(gstr);
			if (g == null) {
				removeSubGroup(g);
				continue;
			}
			val.add(g);
		}
		return val;
	}

	public ArrayList<String> subGroupListStringCopy() {
		return new ArrayList<String>(subGroups);
	}

	/**
	 * @return the variables
	 */
	public UserVariables getVariables() {
		return variables;
	}

	/**
	 * 
	 * @param varList
	 */
	public void setVariables(Map<String, Object> varList) {
		//UserVariables temp = new UserVariables(this, varList);
		variables.clearVars();
		for (String key : varList.keySet()) {
			System.out.print("Adding variable - " + key);
			variables.addVar(key, varList.get(key));
		}
		flagAsChanged();
		if (GroupManager.isLoaded()) {
			if (GroupManager.BukkitPermissions.player_join = false)
				GroupManager.BukkitPermissions.updateAllPlayers();
			GroupManagerEventHandler.callEvent(this, Action.USER_INFO_CHANGED);
		}
	}

	public User updatePlayer(Player player) {
		if (player != null) {
			bukkitPlayer = player;
		}
		return this;
	}

	public Player getBukkitPlayer() {
		if (bukkitPlayer == null) {
			bukkitPlayer = Bukkit.getPlayer(this.getName());
		}
		return bukkitPlayer;
	}
}
