package uk.kihira.playerrugs.common;

import com.mojang.authlib.GameProfile;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
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
        ItemStack itemStack = new ItemStack(PlayerRugs.INSTANCE.playerRugBlock);
        NBTTagCompound tagCompound = new NBTTagCompound();
        NBTTagCompound playerProfileTag = new NBTTagCompound();
        NBTUtil.func_152460_a(playerProfileTag, args.length == 1 ? new GameProfile(null, args[0]) : player.getGameProfile());
        tagCompound.setTag("PlayerProfile", playerProfileTag);
        itemStack.setTagCompound(tagCompound);
        player.setCurrentItemOrArmor(0, itemStack);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }
}
