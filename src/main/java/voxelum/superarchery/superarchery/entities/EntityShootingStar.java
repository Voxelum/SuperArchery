package voxelum.superarchery.superarchery.entities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import voxelum.superarchery.superarchery.ShootingStar;

public class EntityShootingStar extends EntityTippedArrow {
    public EntityShootingStar(World worldIn) {
        super(worldIn);
    }

    public EntityShootingStar(World worldIn, EntityLivingBase shooter, ItemStack stack) {
        super(worldIn, shooter);
    }

    @Override
    protected void onHit(RayTraceResult raytraceResultIn) {
        super.onHit(raytraceResultIn);
        if (!this.world.isRemote) {
            if (raytraceResultIn.typeOfHit == RayTraceResult.Type.BLOCK) {
                ShootingStar.create(((EntityPlayer) this.shootingEntity), raytraceResultIn.getBlockPos(), 5, world.rand);
            } else {
                ShootingStar.create(((EntityPlayer) this.shootingEntity), raytraceResultIn.entityHit.getPosition(), 5, world.rand);
            }
        }
    }

//    @Override
//    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
//        super.shoot(x, y, z, velocity, inaccuracy);
//
//        this.
//    }
}
