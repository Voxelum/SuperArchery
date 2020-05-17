package voxelum.superarchery.superarchery;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderTippedArrow;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTippedArrow;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import org.apache.logging.log4j.Logger;
import voxelum.superarchery.superarchery.entities.EntityExplosionArrow;
import voxelum.superarchery.superarchery.entities.EntityGravitationalPullArrow;
import voxelum.superarchery.superarchery.entities.EntityTeleportArrow;

import java.util.*;

@Mod(modid = SuperArcheryMod.MODID, name = SuperArcheryMod.NAME, version = SuperArcheryMod.VERSION)
public class SuperArcheryMod {
    public static final String MODID = "superarchery";
    public static final String NAME = "Super Archery Mod";
    public static final String VERSION = "1.0";
    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(proxy);
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    // block, entity, item
    // model


    @SidedProxy
    public static CommonProxy proxy;

    public static final String GRAVITATIONAL_ARROW_NAME = "gravitational_arrow";
    public static final String EXPLOSION_ARROW_NAME = "explosion_arrow";
    public static final String TP_ARROW_NAME = "tp_arrow";

    public static final Item TP_ARROW_ITEM = new ItemArrow() {
        @Override
        public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter) {
            return new EntityTeleportArrow(worldIn, shooter);
        }
    }.setRegistryName(TP_ARROW_NAME).setTranslationKey(TP_ARROW_NAME);
    public static final Item EXPLOSION_ARROW_ITEM = new ItemArrow() {
        @Override
        public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter) {
            return new EntityExplosionArrow(worldIn, shooter);
        }
    }.setRegistryName(EXPLOSION_ARROW_NAME).setTranslationKey(EXPLOSION_ARROW_NAME);
    public static final Item GRAVITATIONAL_ARROW_ITEM = new ItemArrow() {
        @Override
        public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter) {
            return new EntityGravitationalPullArrow(worldIn, shooter);
        }
    }.setRegistryName(GRAVITATIONAL_ARROW_NAME).setTranslationKey(GRAVITATIONAL_ARROW_NAME);

    public static class CommonProxy {
        private Queue<Runnable> nextWorldTick = new ArrayDeque<>();

        @SubscribeEvent
        public void registerEntity(RegistryEvent.Register<EntityEntry> entityEntryRegistryEvent) {
            entityEntryRegistryEvent.getRegistry().register(EntityEntryBuilder.create()
                    .name(GRAVITATIONAL_ARROW_NAME)
                    .entity(EntityGravitationalPullArrow.class)
                    .id(GRAVITATIONAL_ARROW_NAME, 0)
                    .tracker(150, 5, true)
                    .build());

            entityEntryRegistryEvent.getRegistry().register(EntityEntryBuilder.create()
                    .name(EXPLOSION_ARROW_NAME)
                    .entity(EntityExplosionArrow.class)
                    .id(EXPLOSION_ARROW_NAME, 1)
                    .tracker(150, 5, true)
                    .build());

            entityEntryRegistryEvent.getRegistry().register(EntityEntryBuilder.create()
                    .name(TP_ARROW_NAME)
                    .entity(EntityTeleportArrow.class)
                    .id(TP_ARROW_NAME, 2)
                    .tracker(150, 5, true)
                    .build());
        }

        @SubscribeEvent
        public void registerItem(RegistryEvent.Register<Item> itemRegister) {
            System.out.println("REGIST ITEM");
            itemRegister.getRegistry().register(TP_ARROW_ITEM);
            itemRegister.getRegistry().register(GRAVITATIONAL_ARROW_ITEM);
            itemRegister.getRegistry().register(EXPLOSION_ARROW_ITEM);
        }

//        @SubscribeEvent
//        public void onPlayer(PlayerInteractEvent.RightClickEmpty playerInteractEvent) {
////            Block block =  playerInteractEvent.getWorld().getBlockState(playerInteractEvent.getPos()).getBlock();
//            if (!playerInteractEvent.getWorld().isRemote) {
//                EntityGravitationalPullArrow arrow = new EntityGravitationalPullArrow(playerInteractEvent.getWorld(), playerInteractEvent.getEntityLiving());
//                EntityPlayer entityPlayer = playerInteractEvent.getEntityPlayer();
//                arrow.shoot(entityPlayer, entityPlayer.rotationPitch, entityPlayer.rotationYaw, 0, 5, 1);
//                playerInteractEvent.getWorld().spawnEntity(arrow);
//            }
//        }


        @SubscribeEvent
        public void onPlayer(PlayerInteractEvent.RightClickBlock playerInteractEvent) {
//            Block block =  playerInteractEvent.getWorld().getBlockState(playerInteractEvent.getPos()).getBlock();
//            if (!playerInteractEvent.getWorld().isRemote) {
//                EntityGravitationalPullArrow arrow = new EntityGravitationalPullArrow(playerInteractEvent.getWorld(), playerInteractEvent.getEntityPlayer());
//                EntityPlayer entityPlayer = playerInteractEvent.getEntityPlayer();
//                arrow.shoot(entityPlayer, entityPlayer.rotationPitch, entityPlayer.rotationYaw, 0, 5, 1);
//                playerInteractEvent.getWorld().spawnEntity(arrow);
//            }

//            if (!playerInteractEvent.getWorld().isRemote) {
//               ShootingStar.spawnArrows(playerInteractEvent.getEntityPlayer(), playerInteractEvent.getPos(), 10, playerInteractEvent.getWorld().rand);
//            }
        }

        @SubscribeEvent
        public void onHurt(LivingHurtEvent event) {
            int tbd = 1;
            DamageSource source = event.getSource();
            if (source instanceof EntityDamageSource && source.getDamageType().equals("explosion.explosion_arrow") && source.getTrueSource() instanceof EntityExplosionArrow) {
                event.getEntityLiving().setFire(5);
                event.setAmount(tbd);
            }
        }

        @SubscribeEvent
        public void onArrowHit(ProjectileImpactEvent.Arrow event) {
            if (event.getEntity().world.isRemote) return;
//            event.setCanceled(true);
            EntityArrow arrow = event.getArrow();
            if (arrow.getTags().contains("shooting-start")) {
                if (event.getRayTraceResult().entityHit == arrow.shootingEntity) {
                    event.setCanceled(true);
                }
                arrow.setDead();
            }
        }

        @SubscribeEvent
        public void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
//            if (event.getEntity().world.isRemote) {
//                return;
//            }
//            EntityLivingBase entityLiving = event.getEntityLiving();
//            for (EntityGravitationalPullArrow arrow : EntityGravitationalPullArrow.getEntityGravitationalPullArrowList()) {
//                if (arrow.shootingEntity == entityLiving || (arrow.shootingEntity != null && arrow.shootingEntity.getEntityId() == entityLiving.getEntityId())) {
//                    continue;
//                }
//                if (arrow.getDistanceSq(entityLiving) < 900) {
//                    entityLiving.motionX += (arrow.posX - entityLiving.posX) * 0.1;
//                    entityLiving.motionY += (arrow.posY - entityLiving.posY) * 0.1;
//                    entityLiving.motionZ += (arrow.posZ - entityLiving.posZ) * 0.1;
//                }
//            }
        }

        @SubscribeEvent
        public void onWorldTick(TickEvent.WorldTickEvent event) {
            if (event.side.isClient() || event.phase == TickEvent.Phase.START) {
                return;
            }

            Queue<Runnable> nextWorldTick = new ArrayDeque<>(this.nextWorldTick);
            this.nextWorldTick.clear();
            while (!nextWorldTick.isEmpty()) {
                Runnable runnable = nextWorldTick.poll();
                runnable.run();
            }
        }

        public void nextWorldTick(Runnable r) {
            nextWorldTick.offer(r);
        }

    }

    public static class ClientProxy extends CommonProxy {
        @SubscribeEvent
        public void regEntityModel(ModelRegistryEvent registryEvent) {
            System.out.println("REGIST MODEL");
            RenderingRegistry.registerEntityRenderingHandler(EntityGravitationalPullArrow.class, RenderTippedArrow::new);
            RenderingRegistry.registerEntityRenderingHandler(EntityTeleportArrow.class, RenderTippedArrow::new);
            RenderingRegistry.registerEntityRenderingHandler(EntityExplosionArrow.class, RenderTippedArrow::new);

            ModelLoader.setCustomModelResourceLocation(EXPLOSION_ARROW_ITEM, 0, new ModelResourceLocation(EXPLOSION_ARROW_ITEM.getRegistryName(), "inventory"));
            ModelLoader.setCustomModelResourceLocation(GRAVITATIONAL_ARROW_ITEM, 0, new ModelResourceLocation(GRAVITATIONAL_ARROW_ITEM.getRegistryName(), "inventory"));
            ModelLoader.setCustomModelResourceLocation(TP_ARROW_ITEM, 0, new ModelResourceLocation(TP_ARROW_ITEM.getRegistryName(), "inventory"));
        }
    }

}
