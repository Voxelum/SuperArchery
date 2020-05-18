package voxelum.superarchery.superarchery;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
        stars.setPosition(pos.getX() + r.nextInt(radius), pos.getY() + 20, pos.getZ() + r.nextInt(radius));
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
        }, 200);
    }
}
