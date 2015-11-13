package uk.kihira.playerrugs.common;

import com.mojang.authlib.GameProfile;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.world.World;
import uk.kihira.playerrugs.PlayerRugs;

public class PlayerRugRecipe implements IRecipe {
    @Override
    public boolean matches(InventoryCrafting crafting, World world) {
        ItemStack skullStack = crafting.getStackInSlot(1);
        // Check for skull
        if (skullStack == null || skullStack.getItem() != Items.skull || skullStack.getItemDamage() != 3) {
            return false;
        }
        // Check for leather
        for (int slot = 3; slot < 6; slot++) {
            if (!checkLeather(crafting.getStackInSlot(slot))) {
                return false;
            }
        }
        return checkLeather(crafting.getStackInSlot(7));

    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting crafting) {
        return PlayerRugs.INSTANCE.getPlayerRugStack(getGameProfile(crafting.getStackInSlot(1)));
    }

    @Override
    public int getRecipeSize() {
        return 9;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }

    private boolean checkLeather(ItemStack itemStack) {
        return itemStack != null && itemStack.getItem() == Items.leather;
    }

    private GameProfile getGameProfile(ItemStack stack) {
        if (stack == null || !stack.hasTagCompound()) {
            return null;
        }
        GameProfile playerProfile = null;
        NBTTagCompound nbttagcompound = stack.getTagCompound();

        if (nbttagcompound.hasKey("SkullOwner", 10)) {
            playerProfile = NBTUtil.func_152459_a(nbttagcompound.getCompoundTag("SkullOwner"));
        }
        // Old version skulls
        else if (nbttagcompound.hasKey("SkullOwner", 8) && nbttagcompound.getString("SkullOwner").length() > 0) {
            playerProfile = new GameProfile(null, nbttagcompound.getString("SkullOwner"));
        }

        return playerProfile;
    }
}
