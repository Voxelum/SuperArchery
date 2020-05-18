package voxelum.superarchery.superarchery.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityExplosionArrow extends EntityTippedArrow {

    public EntityExplosionArrow(World worldIn) {
        super(worldIn);
    }

    public EntityExplosionArrow(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
    }

    @Override
    public void onHit(RayTraceResult result) {
        super.onHit(result);
        Entity suspect = this.shootingEntity;
        if (suspect instanceof EntityPlayer && !suspect.world.isRemote) {
            EntityPlayer shooter = (EntityPlayer) this.shootingEntity;
            if (!this.inWater) {
                shooter.getEntityWorld().createExplosion(this,this.posX,this.posY,this.posZ,4,true);
                this.setDead();
            }
        }
    }
}
