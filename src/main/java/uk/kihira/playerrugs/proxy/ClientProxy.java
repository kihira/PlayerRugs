package uk.kihira.playerrugs.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import uk.kihira.playerrugs.client.PlayerRugTESR;
import uk.kihira.playerrugs.common.tileentities.PlayerRugTE;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(PlayerRugTE.class, new PlayerRugTESR());
    }
}
