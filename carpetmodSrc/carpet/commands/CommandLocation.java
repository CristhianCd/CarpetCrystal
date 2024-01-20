package carpet.commands;

import carpet.utils.Messenger;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandLocation extends CommandCarpetBase {
    private EntityPlayerMP entityPlayerMP;

    @Override
    public String getName() {
        return "location";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "location <player>";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (!command_enabled("commandLocation", sender))
            return;
        if (args.length == 0) {
            throw new WrongUsageException(getUsage(sender));
        }
        String name = args[0];
        entityPlayerMP = server.getPlayerList().getPlayerByUsername(name);
        if (entityPlayerMP == null) {
            throw new PlayerNotFoundException("Player " + name + " not found");
        }
        int posX = entityPlayerMP.getPosition().getX();
        int posY = entityPlayerMP.getPosition().getY();
        int posZ = entityPlayerMP.getPosition().getZ();
        msg(sender, Messenger.m(null, "w " + TextFormatting.DARK_PURPLE + name + " @ " + dimensionFormat() + TextFormatting.WHITE + " ",
                "c [x: " + posX + " | y: " + posY + " | z: " + posZ + "]", "^w Add waypoint", "!/waypoint add " + name + " " + posX + " " + posY + " " + posZ + " " + getDimension()));
        entityPlayerMP.addPotionEffect(new PotionEffect(Potion.getPotionById(24), 200));
    }

    private String dimensionFormat() {
        String dim = getDimension();
        String textFormat = null;
        if (dim == "overworld") {
            textFormat = TextFormatting.DARK_GREEN + "Overworld";
        } else if (dim == "end") {
            textFormat = TextFormatting.DARK_PURPLE + "End";
        } else if (dim == "nether") {
            textFormat = TextFormatting.DARK_RED + "Nether";
        }
        return textFormat;
    }

    private String getDimension() {
        int dim = entityPlayerMP.dimension;
        String dimension = null;
        if (dim == 0) {
            dimension = "overworld";
        } else if (dim == 1) {
            dimension = "end";
        } else if (dim == -1) {
            dimension = "nether";
        }
        return dimension;
    }

    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) : Collections.emptyList();
    }
}
