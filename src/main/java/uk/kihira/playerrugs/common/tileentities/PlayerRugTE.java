package uk.kihira.playerrugs.common.tileentities;

import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class PlayerRugTE extends TileEntity {

    private GameProfile playerProfile;

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);

        if (getTileData().hasKey("PlayerProfile")) {
            setPlayerProfile(NBTUtil.readGameProfileFromNBT(getTileData().getCompoundTag("PlayerProfile")));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        if (getPlayerProfile() != null) {
            NBTTagCompound playerProfileTag = getTileData().getCompoundTag("PlayerProfile");
            NBTUtil.writeGameProfile(playerProfileTag, getPlayerProfile());
            getTileData().setTag("PlayerProfile", playerProfileTag);
        }

        super.writeToNBT(nbtTagCompound);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        writeToNBT(tagCompound);
        return new S35PacketUpdateTileEntity(getPos(), 3, tagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound tagCompound = pkt.getNbtCompound();
        readFromNBT(tagCompound);
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return super.getRenderBoundingBox().expand(1,1,1);
    }

    public GameProfile getPlayerProfile() {
        return playerProfile;
    }

    public void setPlayerProfile(GameProfile playerProfile) {
        this.playerProfile = playerProfile;
    }
}
