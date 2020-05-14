package voxelum.superarchery.superarchery;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Random;

public class ShootingStar {
    public ShootingStar() {
    }

    //fuck
    private static void shoot(EntityPlayer player, BlockPos pos, int radius, Random r, int left) {
        World world = player.getEntityWorld();
        EntityTippedArrow stars = new EntityTippedArrow(world, player);
        stars.setPosition(pos.getX()+r.nextInt(radius), pos.getY() + 20, pos.getZ()+r.nextInt(radius));
        stars.shoot(0, -1, 0, 1, 0.5f);
        world.spawnEntity(stars);
        stars.getTags().add("shooting-start");

        if (left > 0) {
            SuperArcheryMod.proxy.nextWorldTick(() -> shoot(player, pos, radius, r, left - 1));
        }
    }

    public static void spawnArrows(EntityPlayer player, BlockPos pos, int radius, Random r) {
        shoot(player, pos, radius, r, 200);
    }

    public static void collectUselessArrows() {

    }
}

