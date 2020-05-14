package voxelum.superarchery.superarchery;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

//jian bu qi lai jian
public class EntityGravitationalPullArrow extends EntityTippedArrow {
    private int age = 0;
    private double level = 30D;

    public EntityGravitationalPullArrow(World worldIn) {
        super(worldIn);
//        EntityGravitationalPullArrow.entityGravitationalPullArrowList.add(this);
    }

    public EntityGravitationalPullArrow(World worldIn, EntityLivingBase shoot) {
        super(worldIn, shoot);
//        EntityGravitationalPullArrow.entityGravitationalPullArrowList.add(this);
    }

    /**
     * Called at the moment of the arrow hitting on ground or hitting an entity
     */
    private void updateGravity() {
        if (this.world.isRemote) {
            return;
        }
        if (!this.inGround) {
            return;
        }
        List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(this.shootingEntity, this.getEntityBoundingBox().expand(level, 1, level).expand(-level, -1, level));
        if (!list.isEmpty()) {
            for (Entity entity : list) {
                if (entity instanceof EntityLivingBase) {
                    EntityLivingBase living = (EntityLivingBase) entity;
                    if (living == this.shootingEntity) {
                        continue;
                    }
                    living.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 30, 90, false, false));
                    double x = this.posX - living.posX;
                    double y = this.posY - living.posY;
                    double z = this.posZ - living.posZ;
                    Vec3d vec = new Vec3d(x, y, z).normalize().scale(0.05);
                    living.motionX += vec.x;
                    living.motionY += vec.y;
                    living.motionZ += vec.z;
                }
            }
        }
    }

//    private static List<EntityGravitationalPullArrow> entityGravitationalPullArrowList = new ArrayList<>();
//
//    public static List<EntityGravitationalPullArrow> getEntityGravitationalPullArrowList() {
//        return entityGravitationalPullArrowList;
//    }

    @Override
    public void setDead() {
        super.setDead();
//        entityGravitationalPullArrowList.remove(this);
    }

    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();
//        this.updateGravity();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!this.world.isRemote) {
            this.age--;
            if (this.age == 0) {
                this.setDead();
            } else {
                updateGravity();
            }
        }
    }
}
