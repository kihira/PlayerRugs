package uk.kihira.playerrugs.common.tileentities;

import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class PlayerRugTE extends TileEntity {

    public GameProfile playerProfile;

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);

        playerProfile = NBTUtil.func_152459_a(nbtTagCompound.getCompoundTag("PlayerProfile"));
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);

        if (playerProfile != null) {
            NBTTagCompound playerProfileTag = nbtTagCompound.getCompoundTag("PlayerProfile");
            NBTUtil.func_152460_a(playerProfileTag, playerProfile);
            nbtTagCompound.setTag("PlayerProfile", playerProfileTag);
        }
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        writeToNBT(tagCompound);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 3, tagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound tagCompound = pkt.func_148857_g();
        readFromNBT(tagCompound);
    }
}
