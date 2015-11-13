package uk.kihira.playerrugs.common.items;

import com.mojang.authlib.GameProfile;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTUtil;

import java.util.List;

public class PlayerRugItem extends ItemBlock {

    public PlayerRugItem(Block block) {
        super(block);
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean p_77624_4_) {
        if (itemStack.hasTagCompound()) {
            GameProfile profile = NBTUtil.func_152459_a(itemStack.getTagCompound().getCompoundTag("PlayerProfile"));

            list.add("Player: " + (profile != null ? profile.getName() : "None"));
        }
    }
}
