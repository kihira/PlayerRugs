package uk.kihira.playerrugs.proxy;

import net.minecraft.item.Item;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import uk.kihira.playerrugs.PlayerRugs;
import uk.kihira.playerrugs.client.PlayerRugTESR;
import uk.kihira.playerrugs.common.tileentities.PlayerRugTE;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(PlayerRugTE.class, new PlayerRugTESR());
        ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(PlayerRugs.INSTANCE.playerRugBlock), 0, PlayerRugTE.class);
        //Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().registerBuiltInBlocks(PlayerRugs.INSTANCE.playerRugBlock);
    }
}
