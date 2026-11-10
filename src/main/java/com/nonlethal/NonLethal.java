package com.nonlethal;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.*;

@Mod(NonLethal.MODID)
public class NonLethal {
    public static final String MODID = "nonlethal";
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final RegistryObject<Item> RUBBER_BULLET = ITEMS.register("rubber_bullet", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FLASHBALL = ITEMS.register("flashball", () -> new FlashballItem(new Item.Properties().stacksTo(1).durability(250)));
    public static final RegistryObject<Item> PEPPER_SPRAY = ITEMS.register("pepper_spray", () -> new PepperSprayItem(new Item.Properties().stacksTo(1).durability(40)));

    public static final RegistryObject<EntityType<RubberBulletEntity>> BULLET = ENTITIES.register("rubber_bullet",
        () -> EntityType.Builder.<RubberBulletEntity>of(RubberBulletEntity::new, MobCategory.MISC)
            .sized(0.25f, 0.25f).clientTrackingRange(4).updateInterval(10).build("rubber_bullet"));

    public static final RegistryObject<CreativeModeTab> TAB = TABS.register("main", () -> CreativeModeTab.builder()
        .title(Component.translatable("itemGroup.nonlethal"))
        .icon(() -> new ItemStack(FLASHBALL.get()))
        .displayItems((p, o) -> { o.accept(FLASHBALL.get()); o.accept(RUBBER_BULLET.get()); o.accept(PEPPER_SPRAY.get()); })
        .build());

    public NonLethal() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(bus); ENTITIES.register(bus); TABS.register(bus);
    }
}
