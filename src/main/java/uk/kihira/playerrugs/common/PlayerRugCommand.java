package uk.kihira.playerrugs.common;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
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
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayer player = (EntityPlayer) sender;
        ItemStack itemStack = PlayerRugs.INSTANCE.getPlayerRugStack(args.length == 1 ? server.getPlayerProfileCache().getGameProfileForUsername(args[0]) : player.getGameProfile());
        player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, itemStack);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }
}
