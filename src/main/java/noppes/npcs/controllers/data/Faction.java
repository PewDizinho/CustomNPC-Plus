package noppes.npcs.controllers.data;

import java.util.HashSet;
import java.util.Iterator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.NBTTags;
import noppes.npcs.controllers.FactionController;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.scripted.CustomNPCsException;
import noppes.npcs.scripted.interfaces.handler.data.IFaction;
import noppes.npcs.scripted.interfaces.entity.ICustomNpc;
import noppes.npcs.scripted.interfaces.entity.IPlayer;

public class Faction implements IFaction {
	public String name = "";
	public int color = Integer.parseInt("FF00", 16);
	
	public HashSet<Integer> attackFactions;
	public int id = -1;
	
	public int neutralPoints = 500;
	public int friendlyPoints = 1500;
		
	public int defaultPoints = 1000;
	
	public boolean hideFaction = false;
	public boolean getsAttacked = false;
	
	public Faction(){
		attackFactions = new HashSet<Integer>();
	}
	
	public Faction(int id, String name,int color, int defaultPoints){
		this.name = name;
		this.color = color;
		this.defaultPoints = defaultPoints;
		this.id = id;
		attackFactions = new HashSet<Integer>();
	}
	public static String formatName(String name){
		name = name.toLowerCase().trim();
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}
	public void readNBT(NBTTagCompound compound){
        name = compound.getString("Name");
        color = compound.getInteger("Color");
        id = compound.getInteger("Slot");
        
        neutralPoints= compound.getInteger("NeutralPoints");
        friendlyPoints = compound.getInteger("FriendlyPoints");
        defaultPoints = compound.getInteger("DefaultPoints");

        hideFaction = compound.getBoolean("HideFaction");
        getsAttacked = compound.getBoolean("GetsAttacked");
        
        attackFactions = NBTTags.getIntegerSet(compound.getTagList("AttackFactions", 10));
	}
	public void writeNBT(NBTTagCompound compound){
		compound.setInteger("Slot", id);
		compound.setString("Name", name);
		compound.setInteger("Color", color);
		
		compound.setInteger("NeutralPoints", neutralPoints);
		compound.setInteger("FriendlyPoints", friendlyPoints);
		compound.setInteger("DefaultPoints", defaultPoints);

		compound.setBoolean("HideFaction", hideFaction);
		compound.setBoolean("GetsAttacked", getsAttacked);
		
		compound.setTag("AttackFactions", NBTTags.nbtIntegerSet(attackFactions));
	}

	public boolean isFriendlyToPlayer(EntityPlayer player) {
		PlayerFactionData data = PlayerDataController.instance.getPlayerData(player).factionData;
		return data.getFactionPoints(id) >= friendlyPoints;
	}

	public boolean isAggressiveToPlayer(EntityPlayer player) {
		PlayerFactionData data = PlayerDataController.instance.getPlayerData(player).factionData;		
		return data.getFactionPoints(id) < neutralPoints;
	}
	
	public boolean isNeutralToPlayer(EntityPlayer player) {
		PlayerFactionData data = PlayerDataController.instance.getPlayerData(player).factionData;
		int points = data.getFactionPoints(id);
		return points >= neutralPoints && points < friendlyPoints;
	}

	public boolean isAggressiveToNpc(EntityNPCInterface entity) {
		return attackFactions.contains(entity.faction.id);
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public int getDefaultPoints() {
		return this.defaultPoints;
	}

	public int getColor() {
		return this.color;
	}

	public int playerStatus(IPlayer player) {
		PlayerFactionData data = PlayerData.get((EntityPlayer) player.getMCEntity()).factionData;
		int points = data.getFactionPoints(this.id);
		if (points >= this.friendlyPoints) {
			return 1;
		} else {
			return points < this.neutralPoints ? -1 : 0;
		}
	}

	public boolean hostileToNpc(ICustomNpc npc) {
		return this.attackFactions.contains(npc.getFaction().getId());
	}

	public void setDefaultPoints(int points) {
		this.defaultPoints = points;
	}

	public boolean hostileToFaction(int factionId) {
		return this.attackFactions.contains(factionId);
	}

	public int[] getHostileList() {
		int[] a = new int[this.attackFactions.size()];
		int i = 0;

		Integer val;
		for(Iterator var3 = this.attackFactions.iterator(); var3.hasNext(); a[i++] = val) {
			val = (Integer)var3.next();
		}

		return a;
	}

	public void addHostile(int id) {
		if (this.attackFactions.contains(id)) {
			throw new CustomNPCsException("Faction " + this.id + " is already hostile to " + id, new Object[0]);
		} else {
			this.attackFactions.add(id);
		}
	}

	public void removeHostile(int id) {
		this.attackFactions.remove(id);
	}

	public boolean hasHostile(int id) {
		return this.attackFactions.contains(id);
	}

	public boolean getIsHidden() {
		return this.hideFaction;
	}

	public void setIsHidden(boolean bo) {
		this.hideFaction = bo;
	}

	public boolean getAttackedByMobs() {
		return this.getsAttacked;
	}

	public void setAttackedByMobs(boolean bo) {
		this.getsAttacked = bo;
	}

	public void save() {
		FactionController.getInstance().saveFaction(this);
	}
}