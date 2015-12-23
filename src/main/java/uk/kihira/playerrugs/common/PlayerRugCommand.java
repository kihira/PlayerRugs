package uk.kihira.playerrugs.common;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import uk.kihira.playerrugs.PlayerRugs;

public class PlayerRugCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "playerrug";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        EntityPlayer player = (EntityPlayer) sender;
        ItemStack itemStack = PlayerRugs.INSTANCE.getPlayerRugStack(args.length == 1 ? MinecraftServer.getServer().getPlayerProfileCache().getGameProfileForUsername(args[0]) : player.getGameProfile());
        player.setCurrentItemOrArmor(0, itemStack);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }
}
