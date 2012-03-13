package com.earth2me.essentials.update.states;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class InstallationFinishedEvent extends Event
{
    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers()
    {
        return handlers;
    }
}
