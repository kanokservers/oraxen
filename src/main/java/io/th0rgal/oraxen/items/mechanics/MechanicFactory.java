package io.th0rgal.oraxen.items.mechanics;

import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class MechanicFactory {

    private Map<String, Mechanic> mechanicByItem = new HashMap<>();
    private ConfigurationSection section;

    public MechanicFactory(ConfigurationSection section) {
        this.section = section;
    }

    protected ConfigurationSection getSection() {
        return this.section;
    }

    public abstract Mechanic parse(ConfigurationSection itemMechanicConfiguration);

    protected void addToImplemented(Mechanic mechanic) {
        mechanicByItem.put(mechanic.getItemID(), mechanic);
    }

    public Set<String> getItems() {
        return mechanicByItem.keySet();
    }

    public boolean isImplementedIn(String itemID) {
        return mechanicByItem.containsKey(itemID);
    }

    public Mechanic getMechanic(String itemID) {
        return  mechanicByItem.get(itemID);
    }

    public String getMechanicID() {
        return section.getName();
    }

}