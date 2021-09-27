package carpet.commands;

import carpet.CarpetSettings;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandInventory extends CommandCarpetBase {
    @Override
    public String getName() {
        return "inventory";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/inventory <player>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return sender.canUseCommand(this.getRequiredPermissionLevel(), this.getName());
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (!command_enabled("commandInventory", sender))
            return;
        if (args.length == 0) {
            throw new WrongUsageException(getUsage(sender));
        }
        String name = args[0];
        EntityPlayerMP entityPlayerMP = server.getPlayerList().getPlayerByUsername(name);
        EntityPlayerMP player = server.getPlayerList().getPlayerByUsername(sender.getName());
        if (entityPlayerMP == null) {
            throw new PlayerNotFoundException("Player " + name + " not found");
        } else if (entityPlayerMP == player) {
            throw new WrongUsageException("You can't see inventory yourself");
        }
        InventoryPlayer inventoryPlayer = entityPlayerMP.inventory;
        player.displayGUIChest(inventoryPlayer);
    }

    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (!CarpetSettings.commandEnderChest) {
            return Collections.<String>emptyList();
        }
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) : Collections.emptyList();
    }
}
