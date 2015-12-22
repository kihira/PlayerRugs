package uk.kihira.playerrugs.proxy;

import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import uk.kihira.playerrugs.PlayerRugs;
import uk.kihira.playerrugs.client.PlayerRugItemRenderer;
import uk.kihira.playerrugs.client.PlayerRugTESR;
import uk.kihira.playerrugs.common.tileentities.PlayerRugTE;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerRenderers() {
        PlayerRugTESR playerRugTESR = new PlayerRugTESR();
        ClientRegistry.bindTileEntitySpecialRenderer(PlayerRugTE.class, playerRugTESR);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(PlayerRugs.INSTANCE.playerRugBlock), new PlayerRugItemRenderer(playerRugTESR));
    }
}
