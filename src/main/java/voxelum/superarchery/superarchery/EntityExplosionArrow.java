package voxelum.superarchery.superarchery;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityExplosionArrow extends EntityTippedArrow {

    public EntityExplosionArrow(World worldIn) {
        super(worldIn);
    }

    public void explode(RayTraceResult result) {
        super.onHit(result);
        Entity suspect = this.shootingEntity;
        if (suspect instanceof EntityPlayer && !suspect.world.isRemote) {
            EntityPlayer shooter = (EntityPlayer) this.shootingEntity;
            if (this.collided && !this.inWater) {
                shooter.getEntityWorld().createExplosion(this,this.posX,this.posY,this.posZ,2,true);
                this.setDead();
            }

        }
    }
}
