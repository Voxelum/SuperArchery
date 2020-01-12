package voxelum.superarchery.superarchery;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderTippedArrow;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
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
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.GameData;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
                    .entity(GravityArrow.class)
                    .id("gravity_arrow", 0)
                    .tracker(150, 5, true)
                    .build());
        }

        @SubscribeEvent
        public void regEntityModel(ModelRegistryEvent registryEvent) {
            RenderingRegistry.registerEntityRenderingHandler(GravityArrow.class, RenderTippedArrow::new);
        }

//        @SubscribeEvent
//        public void onPlayer(PlayerInteractEvent.RightClickEmpty playerInteractEvent) {
////            Block block =  playerInteractEvent.getWorld().getBlockState(playerInteractEvent.getPos()).getBlock();
//            if (!playerInteractEvent.getWorld().isRemote) {
//                GravityArrow arrow = new GravityArrow(playerInteractEvent.getWorld(), playerInteractEvent.getEntityLiving());
//                EntityPlayer entityPlayer = playerInteractEvent.getEntityPlayer();
//                arrow.shoot(entityPlayer, entityPlayer.rotationPitch, entityPlayer.rotationYaw, 0, 5, 1);
//                playerInteractEvent.getWorld().spawnEntity(arrow);
//            }
//        }


        @SubscribeEvent
        public void onPlayer(PlayerInteractEvent.RightClickBlock playerInteractEvent) {
//            Block block =  playerInteractEvent.getWorld().getBlockState(playerInteractEvent.getPos()).getBlock();
            if (!playerInteractEvent.getWorld().isRemote) {
// ????               GravityArrow arrow = new GravityArrow(playerInteractEvent.getWorld(), playerInteractEvent.getEntityPlayer());
//                EntityPlayer entityPlayer = playerInteractEvent.getEntityPlayer();
//                arrow.shoot(entityPlayer, entityPlayer.rotationPitch, entityPlayer.rotationYaw, 0, 5, 1);
//                playerInteractEvent.getWorld().spawnEntity(arrow);
                Random r = new Random(254);
                World world = playerInteractEvent.getWorld();
                Chunk chunk = world.getChunkFromChunkCoords(0, 0);
                ObjectIntIdentityMap<IBlockState> blockStateIDMap = GameData.getBlockStateIDMap();
                StringBuilder builder = new StringBuilder();

//                for (int x = 0; x < 16; x++) {
//                    for (int y = 0; y < 16; y++) {
//                        for (int z = 0; z < 16; z++) {
//                            chunk.setBlockState(new BlockPos(x, y, z), Blocks.AIR.getDefaultState());
//                        }
//                    }
//                }

                class BlocKIterator {
                    int i = 1;
                    Set<Block> banded = Sets.newHashSet(
                            Blocks.LAVA,
                            Blocks.FLOWING_LAVA,
                            Blocks.FLOWING_WATER,
                            Blocks.WATER,
                            Blocks.WHITE_SHULKER_BOX,
                            Blocks.ORANGE_SHULKER_BOX,
                            Blocks.MAGENTA_SHULKER_BOX,
                            Blocks.LIGHT_BLUE_SHULKER_BOX,
                            Blocks.YELLOW_SHULKER_BOX,
                            Blocks.LIME_SHULKER_BOX,
                            Blocks.PINK_SHULKER_BOX,
                            Blocks.GRAY_SHULKER_BOX,
                            Blocks.SILVER_SHULKER_BOX,
                            Blocks.CYAN_SHULKER_BOX,
                            Blocks.PURPLE_SHULKER_BOX,
                            Blocks.BLUE_SHULKER_BOX,
                            Blocks.BROWN_SHULKER_BOX,
                            Blocks.GREEN_SHULKER_BOX,
                            Blocks.RED_SHULKER_BOX,
                            Blocks.BLACK_SHULKER_BOX
                    );

                    public IBlockState next() {
                        IBlockState state;
                        Material material;
                        do {
                            if (i == 256) {
                                i = 1;
                            }
                            Block block = Block.getBlockById(i++);
                            state = block.getDefaultState();
                            material = state.getMaterial();
                        } while (banded.contains(state.getBlock()) || material.isLiquid() || !material.isSolid());
                        return state;
//                        IBlockState result = validStates.get(stateIndex);
//                        if ((stateIndex + 1) < validStates.size()) {
//                            stateIndex += 1;
//                        } else {
//                            Block object = Block.REGISTRY.getRandomObject(r);
//                            while (!object.getMaterial(object.getDefaultState()).isOpaque() || visited.contains(Block.REGISTRY.getIDForObject(object))) {
//                                object = Block.REGISTRY.getRandomObject(r);
//                            }
//                            i = Block.REGISTRY.getIDForObject(object);
//                            visited.add(i);
//                            stateIndex = 0;
//                            validStates = Block.REGISTRY.getObjectById(i).getBlockState().getValidStates();
//                        }
//                        return result;
                    }
                }
                for (int x = 0; x < 16; x++) {
                    for (int y = 0; y < 16; y++) {
                        for (int z = 0; z < 16; z++) {
                            IBlockState blockState = chunk.getBlockState(new BlockPos(x, y, z));
                            builder.append(x).append(" ").append(y).append(" ").append(z).append(" ").append(blockStateIDMap.get(blockState)).append("\n");
                        }
                    }
                }
//                BlocKIterator blocKIterator = new BlocKIterator();
//                for (int x = 0; x < 16; x += 2) {
//                    for (int y = 0; y < 16; y += 2) {
//                        for (int z = 0; z < 16; z += 2) {
////                            ImmutableList<IBlockState> validStates = Block.REGISTRY.getRandomObject(r).getBlockState().getValidStates();
////                            IBlockState state = validStates.get(/*r.nextInt(validStates.size())*/ 0);
//                            IBlockState state = blocKIterator.next();
//                            chunk.setBlockState(new BlockPos(x, y, z), state);
//                            builder.append(x).append(" ").append(y).append(" ").append(z).append(" ").append(blockStateIDMap.get(state)).append("\n");
//                        }
//                    }
//                }
                try {
                    Files.write(Paths.get("C:\\Users\\226\\Desktop\\mock.txt"), builder.toString().getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                for (int i = player.getPosition().getX(); i < player.getPosition().getX() + 16; i++) {
//                    for (int j = player.getPosition().getY(); j < player.getPosition().getY() + 16; j++) {
//                        for (int k = player.getPosition().getZ(); k < player.getPosition().getZ() + 16; k++) {
//                            BlockPos pos = new BlockPos(i, j, k);
//                            playerInteractEvent.getWorld().setBlockState(pos, Block.REGISTRY.getRandomObject(r).getDefaultState());
//                        }
//                    }
//                }
            }

            if (!playerInteractEvent.getWorld().isRemote) {
//                ShootingStar.spawnArrows(playerInteractEvent.getEntityPlayer(), playerInteractEvent.getPos(), 10, playerInteractEvent.getWorld().rand);
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
            if (event.getEntity().world.isRemote) {
                return;
            }
            EntityLivingBase entityLiving = event.getEntityLiving();
            for (GravityArrow arrow : GravityArrow.getGravityArrowList()) {
                if (arrow.shootingEntity == entityLiving || (arrow.shootingEntity != null && arrow.shootingEntity.getEntityId() == entityLiving.getEntityId())) {
                    continue;
                }
                if (arrow.getDistanceSq(entityLiving) < 900) {
                    entityLiving.motionX += (arrow.posX - entityLiving.posX) * 0.1;
                    entityLiving.motionY += (arrow.posY - entityLiving.posY) * 0.1;
                    entityLiving.motionZ += (arrow.posZ - entityLiving.posZ) * 0.1;
                }
            }
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
