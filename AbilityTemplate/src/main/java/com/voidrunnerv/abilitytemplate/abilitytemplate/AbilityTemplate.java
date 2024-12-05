package com.voidrunnerv.abilitytemplate.abilitytemplate;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.*;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.ComboAbility;
import com.projectkorra.projectkorra.ability.util.ComboManager.AbilityInformation;
import com.projectkorra.projectkorra.util.ClickType;

import java.util.ArrayList;

public final class AbilityTemplate extends AirAbility implements AddonAbility, Listener /*, ComboAbility*/ {

    //Template Variables

    //Main vars
    private static final String NAME = "AbilityTemplate-TEST";
    private static final String DESCRIPTION = "AbilityTemplate-001";
    private static final String AUTHOR = "VoidRunnerv";
    private static final String VERSION = "1.0";
    private static final String INSTRUCTIONS = "AbilityTemplate Instructions: PhaseChange (Tap Sneak) > PhaseChange (Tap Sneak) > Torrent (Left Click to a water source)";

    //COMMAND
    public static final String[] COMMANDS = {"say hi"};

    //Additional Vars
    private static final boolean requireWater = false;
    private static final boolean requireArrows = false;
    private static final boolean requireIron = false;

    private long cooldown = 20;

    //COMBO - To make this a combo, set isCombo to true, and remove the comments at the start after "AddonAbility" and the ones around createNewComboInstance() and getCombination() methods
    /*

    To set the combination, the format is ability:clickType
    Click types: TapSneak, HoldSneak, and LeftClick
    If you want more, add it ;)
     */
    private static final boolean isCombo = false;
    private static final String[] abilities = {"PhaseChange:TapSneak", "PhaseChange:TapSneak", "Torrent:LeftClick"};

    /*

    Non Combo: Go to the bottom and setup a listener, or use the default left click.

     */

    //DONT EDIT
    private Block source;
    private BendingPlayer bPlayer;
    private long durationStart;


    public AbilityTemplate(Player player) {
        super(player);

        bPlayer = BendingPlayer.getBendingPlayer(player);

        if(!isCombo){ nonCombo(); return; }

        if (bPlayer.isOnCooldown(this)) {
            return;
        } else if (!bPlayer.canBendIgnoreBindsCooldowns(this)) {
            return;
        }

        if (hasAbility(player, AbilityTemplate.class)) {
            return;
        }

        durationStart = -1;
        start();
    }

    public void runComands(Player player){
        for(String command : COMMANDS){
            player.performCommand(command);
        }
    }

    public void nonCombo(){
        this.bPlayer.addCooldown(this);

        //player.sendMessage("AbilityTemplate");
        runComands(player);
    }


    @Override
    public void progress() {
        //Do stuff here

        this.bPlayer.addCooldown(this);

        runComands(player);

        remove();
    }

    @Override
    public long getCooldown() {
        return cooldown;
    }

    @Override
    public Location getLocation() {
        return player != null ? player.getLocation() : null;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isHarmlessAbility() {
        return false;
    }

    @Override
    public boolean isSneakAbility() {
        return false;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public String getInstructions() {
        return INSTRUCTIONS;
    }

    @Override
    public String getAuthor() {
        return AUTHOR;
    }

    @Override
    public String getVersion() {
        return VERSION;
    }

    @Override
    public void remove() {
        super.remove();
    }

    @Override
    public void load() {
        ProjectKorra.plugin.getServer().getPluginManager().registerEvents(this, ProjectKorra.plugin);
        ProjectKorra.log.info("Successfully enabled " + getName() + " by " + getAuthor());

    }

    @Override
    public void stop() {
        ProjectKorra.log.info("Successfully disabled " + getName() + " by " + getAuthor());
        super.remove();
    }

    //Listener
    @EventHandler
    public void onLeftClick(PlayerAnimationEvent event){
        Player player = event.getPlayer();
        BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);

        //player.sendMessage("Bruh");

        if(isCombo || event.isCancelled() || bPlayer == null || bPlayer.getBoundAbilityName() == null) return;

        if(bPlayer.getBoundAbilityName().equals(NAME)){
            new AbilityTemplate(player);
        }
    }

}
