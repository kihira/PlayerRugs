package uk.kihira.playerrugs.common.tileentities;

import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nullable;

public class PlayerRugTE extends TileEntity {

    private GameProfile playerProfile;

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        if (getTileData().hasKey("PlayerProfile")) {
            playerProfile = NBTUtil.readGameProfileFromNBT(getTileData().getCompoundTag("PlayerProfile"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);
        if (getPlayerProfile() != null) {
            NBTTagCompound playerProfileTag = getTileData().getCompoundTag("PlayerProfile");
            NBTUtil.writeGameProfile(playerProfileTag, getPlayerProfile());
            getTileData().setTag("PlayerProfile", playerProfileTag);
        }
        super.writeToNBT(nbtTagCompound);
        return nbtTagCompound;
    }

    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 3, this.getUpdateTag());
    }

    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        NBTTagCompound tagCompound = pkt.getNbtCompound();
        readFromNBT(tagCompound);
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(getPos().add(-1, -1, -1), getPos().add(1, 1, 1));
    }

    public GameProfile getPlayerProfile() {
        return playerProfile;
    }

    public void setPlayerProfile(GameProfile playerProfile) {
        this.playerProfile = playerProfile;
        this.playerProfile = TileEntitySkull.updateGameprofile(this.playerProfile);
        this.markDirty();
    }
}
