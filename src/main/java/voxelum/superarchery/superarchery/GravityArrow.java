package voxelum.superarchery.superarchery;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
//jian bu qi lai jian
public class GravityArrow extends EntityTippedArrow {
    private int age = 0;
    private double level = 30D;

    public GravityArrow(World worldIn) {
        super(worldIn);
        GravityArrow.gravityArrowList.add(this);
    }

    public GravityArrow(World worldIn, EntityLivingBase shoot) {
        super(worldIn, shoot);
        GravityArrow.gravityArrowList.add(this);
    }

    /**
     * Called at the moment of the arrow hitting on ground or hitting an entity
     */
    public void updateGravity() {
        if (!this.inGround) {
            return;
        }
        List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(this.shootingEntity, this.getEntityBoundingBox().expand(level, 0, level));
        if (!list.isEmpty()) {
            for (Entity entity : list) {
                if (entity instanceof EntityLivingBase) {
                    EntityLivingBase living = (EntityLivingBase) entity;
                    living.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 30, 90, false, false));
                }
            }
        }
    }

    private static List<GravityArrow> gravityArrowList = new ArrayList<>();

    public static List<GravityArrow> getGravityArrowList() {
        return gravityArrowList;
    }

    @Override
    public void setDead() {
        super.setDead();
        gravityArrowList.remove(this);
    }

    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();
    }

    @Override
    public void onUpdate() {
        if (!this.world.isRemote) {
            this.age--;

//            updateGravity();
        }
        super.onUpdate();
    }
}
