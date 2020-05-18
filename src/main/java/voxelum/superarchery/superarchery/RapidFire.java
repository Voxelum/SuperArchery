package voxelum.superarchery.superarchery;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.world.World;

import java.util.Random;

public class RapidFire {
    public static void firing(World world, EntityPlayer player, int wave) {
        Random r = world.rand;
        WorldTickSchedule.repeatInterval(20, (left, cancel) -> {
            EntityTippedArrow arrow = new EntityTippedArrow(world, player);
            arrow.shoot(player, player.cameraPitch + r.nextFloat() / 2, player.cameraYaw + r.nextFloat() / 2, 1, 1, 1);
            world.spawnEntity(arrow);

            arrow = new EntityTippedArrow(world, player);
            arrow.shoot(player, player.cameraPitch + r.nextFloat() / 2, player.cameraYaw + r.nextFloat() / 2, 1, 1, 1);
            world.spawnEntity(arrow);
           
            arrow = new EntityTippedArrow(world, player);
            arrow.shoot(player, player.cameraPitch + r.nextFloat() / 2, player.cameraYaw + r.nextFloat() / 2, 1, 1, 1);
            world.spawnEntity(arrow);
           
            return left - 1;
        }, wave);
    }
}
