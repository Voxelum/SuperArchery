package voxelum.superarchery.superarchery;

import net.minecraft.client.renderer.entity.RenderTippedArrow;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
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

    public static class CommonProxy {
        private Queue<Runnable> nextWorldTick = new ArrayDeque<>();

        @SubscribeEvent
        public void register(RegistryEvent.Register<EntityEntry> entityEntryRegistryEvent) {
            entityEntryRegistryEvent.getRegistry().register(EntityEntryBuilder.create()
                    .name("ga")
                    .entity(EntityGravitationalPullArrow.class)
                    .id("gravity_arrow", 0)
                    .tracker(150, 5, true)
                    .build());
        }

        @SubscribeEvent
        public void regEntityModel(ModelRegistryEvent registryEvent) {
            RenderingRegistry.registerEntityRenderingHandler(EntityGravitationalPullArrow.class, RenderTippedArrow::new);
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
            if (!playerInteractEvent.getWorld().isRemote) {
                EntityGravitationalPullArrow arrow = new EntityGravitationalPullArrow(playerInteractEvent.getWorld(), playerInteractEvent.getEntityPlayer());
                EntityPlayer entityPlayer = playerInteractEvent.getEntityPlayer();
                arrow.shoot(entityPlayer, entityPlayer.rotationPitch, entityPlayer.rotationYaw, 0, 5, 1);
                playerInteractEvent.getWorld().spawnEntity(arrow);
            }

//            if (!playerInteractEvent.getWorld().isRemote) {
//               ShootingStar.spawnArrows(playerInteractEvent.getEntityPlayer(), playerInteractEvent.getPos(), 10, playerInteractEvent.getWorld().rand);
//            }
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

    }

}
