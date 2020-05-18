package voxelum.superarchery.superarchery.entities;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityBouncingArrow extends EntityTippedArrow {
    private int unusedCount = 0;

    public EntityBouncingArrow(World worldIn, int intensity) {
        super(worldIn);
        this.unusedCount = intensity;
    }

    protected void onHit(RayTraceResult raytraceResultIn) {
        super.onHit(raytraceResultIn);
        if (raytraceResultIn.entityHit instanceof EntityCreature) {
            bounceOnce((EntityCreature) raytraceResultIn.entityHit);
        }
    }

    public void bounceOnce(EntityCreature currentShootBody) {
        if (!this.world.isRemote) {
            // search all entities in a range from target entity
            // how to search entities in world?
            // use world.getEntitiesInAABB
            AxisAlignedBB targetEntityBox = currentShootBody.getEntityBoundingBox();
            if (unusedCount != 0) {
                world.getEntitiesWithinAABBExcludingEntity(currentShootBody, targetEntityBox.expand(5, 5, 5).expand(-5, -5, -5))
                        .stream()
                        .filter(e -> e instanceof EntityCreature)
                        .min((a, b) -> (int) (a.getDistanceSq(currentShootBody) - b.getDistanceSq(currentShootBody)))
                        .ifPresent((victim) -> {
                            this.isDead = false;
                            this.shoot(currentShootBody.posX - victim.posX, currentShootBody.posY - victim.posY, currentShootBody.posZ - victim.posZ, 1, 1);
                        });
                //if cant bring back to life , fire a new arrow
            } else {
                this.setDead();
            }
        }
    }
}
