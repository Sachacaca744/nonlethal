package com.nonlethal;

import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = NonLethal.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void renderers(EntityRenderersEvent.RegisterRenderers e) {
        e.registerEntityRenderer(NonLethal.BULLET.get(), ThrownItemRenderer::new);
    }

    // Animation : l'avancement du délai de recharge pilote les images du modèle 3D
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent e) {
        e.enqueueWork(() -> {
            ClampedItemPropertyFunction recoil = (stack, level, entity, seed) ->
                entity instanceof Player p ? p.getCooldowns().getCooldownPercent(stack.getItem(), 0f) : 0f;
            ResourceLocation id = new ResourceLocation(NonLethal.MODID, "recoil");
            ItemProperties.register(NonLethal.FLASHBALL.get(), id, recoil);
            ItemProperties.register(NonLethal.PEPPER_SPRAY.get(), id, recoil);
        });
    }
}
