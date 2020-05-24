package voxelum.superarchery.superarchery;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

@EventBusSubscriber
public class ShootingStar {
    @SubscribeEvent
    public static void onArrowHit(ProjectileImpactEvent.Arrow event) {
        if (event.getEntity().world.isRemote) {
            return;
        }
        EntityArrow arrow = event.getArrow();
        if (arrow.getTags().contains("shooting-star")) {
            if (event.getRayTraceResult().entityHit == arrow.shootingEntity) {
                event.setCanceled(true);
            }
            arrow.setDead();
        }
    }

    private static void shoot(EntityPlayer player, BlockPos pos, int radius, Random r) {
        World world = player.getEntityWorld();
        EntityTippedArrow stars = new EntityTippedArrow(world, player);
        int rad2 = radius + radius;
        stars.setPosition(pos.getX() + r.nextInt(rad2) - radius, pos.getY() + 20, pos.getZ() + r.nextInt(rad2) - radius);
        stars.shoot(0, -1, 0, 1, 0.5f);
        world.spawnEntity(stars);
        stars.getTags().add("shooting-star");
    }

    /**
     * Create a shooting star area.
     *
     * @param player The player who create the effect
     * @param pos    The center effect
     * @param radius Thre rad of the effect
     * @param r      The random of the effect
     */
    public static void create(EntityPlayer player, BlockPos pos, int radius, Random r) {
        WorldTickSchedule.repeat((left, cancel) -> {
            if (left == 0) {
                cancel.run();
            } else {
                shoot(player, pos, radius, r);
            }
            return left - 1;
        }, 20 * 3);
    }

    @SubscribeEvent
    public static void onNock(ArrowNockEvent event) {
        ItemBow bow = (ItemBow) event.getBow().getItem();
        ItemStack stack = findAmmo(event.getEntityPlayer());
        if (stack.getItem() == SuperArchery.SHOOTING_STAR_ARROW_ITEM) {
            if (event.getEntityPlayer().getTags().contains("shooting_star_nock")) return;
            event.getEntityPlayer().getTags().add("shooting_star_nock");
            new EntityPositionMaintainer(event.getEntityLiving(), event.getEntityLiving().posY + 10);
        }
    }

    @SubscribeEvent
    public static void onLoose(ArrowLooseEvent event) {
        ItemStack stack = findAmmo(event.getEntityPlayer());
        if (stack.getItem() == SuperArchery.SHOOTING_STAR_ARROW_ITEM) {
            event.getEntityPlayer().getTags().remove("shooting_star_nock");
            event.getEntityPlayer().getTags().add("shooting_star_fall");
        }
    }

    @SubscribeEvent
    public static void onFallEvent(LivingFallEvent event) {
        if (event.getEntityLiving().getTags().contains("shooting_star_fall")) {
            event.setCanceled(true);
            event.getEntityLiving().getTags().remove("shooting_star_fall");
        }
    }

    static boolean isArrow(ItemStack stack) {
        return stack.getItem() instanceof ItemArrow;
    }

    public static ItemStack findAmmo(EntityPlayer player) {
        if (isArrow(player.getHeldItem(EnumHand.OFF_HAND))) {
            return player.getHeldItem(EnumHand.OFF_HAND);
        } else if (isArrow(player.getHeldItem(EnumHand.MAIN_HAND))) {
            return player.getHeldItem(EnumHand.MAIN_HAND);
        } else {
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
                ItemStack itemstack = player.inventory.getStackInSlot(i);

                if (isArrow(itemstack)) {
                    return itemstack;
                }
            }

            return ItemStack.EMPTY;
        }
    }

    public static class EntityPositionMaintainer {
        private EntityLivingBase livingBase;
        private double targetHeight;

        public EntityPositionMaintainer(EntityLivingBase livingBase, double targetHeight) {
            this.livingBase = livingBase;
            this.targetHeight = targetHeight;
            MinecraftForge.EVENT_BUS.register(this);
        }

        @SubscribeEvent
        public void update(LivingEvent.LivingUpdateEvent event) {
            if (event.getEntity() != livingBase) return;
            if (!livingBase.getTags().contains("shooting_star_nock")
                    || (livingBase.getHeldItemMainhand().getItem() != Items.BOW
                    && livingBase.getHeldItemOffhand().getItem() != Items.BOW)) {
                destroy();
            } else if (livingBase.posY < targetHeight) {
                double motion = (targetHeight - livingBase.posY) * 0.02;
                livingBase.motionY += motion;
                if (livingBase.motionY < 0) {
                    livingBase.motionY = motion;
                }
            }
        }

        private void destroy() {
            MinecraftForge.EVENT_BUS.unregister(this);
        }
    }
}
