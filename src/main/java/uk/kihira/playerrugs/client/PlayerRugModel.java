package uk.kihira.playerrugs.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class PlayerRugModel extends ModelBase {

    private ModelRenderer head;

    public PlayerRugModel() {
        textureWidth = 64;
        textureHeight = 32;
        head = new ModelRenderer(this, 0, 0);
        head.addBox(-4f, -8f, -4f, 8, 8, 8, 0f);
        head.setRotationPoint(0f, 0f, 0f);
    }

    public void renderAll() {
        head.render(0.0625f);
    }
}
