package voxelum.superarchery.superarchery;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.world.World;

import java.util.Random;

public class RapidFire {
    public static void firing(World world, EntityPlayer player, int wave) {
        for (int i = 0; i < wave; i++) {
            EntityTippedArrow arrow = new EntityTippedArrow(world, player);
            Random r = new Random();
            SuperArcheryMod.proxy.nextWorldTick(() -> arrow.shoot(player, player.cameraPitch + r.nextFloat()/2, player.cameraYaw + r.nextFloat()/2,
                    1, 1, 1));

        }


    }
}
