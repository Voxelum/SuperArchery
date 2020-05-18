package voxelum.superarchery.superarchery;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import java.util.List;
import java.util.UUID;

import com.google.common.base.Objects;

public class GravityPull {
    public static void pull(Entity self, UUID shooter, World world, double posX, double posY, double posZ, double radius) {
        AxisAlignedBB box = new AxisAlignedBB(posX - radius, posY - radius, posZ - radius, posX + radius, posY + radius,
                posZ + radius);
        List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(self, box);
        if (!list.isEmpty()) {
            for (Entity entity : list) {
                if (entity instanceof EntityLivingBase) {
                    EntityLivingBase living = (EntityLivingBase) entity;
                    if (living == self || Objects.equal(shooter, living.getUniqueID())) {
                        continue;
                    }
                    living.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 30, 90, false, false));

                    Vec3d motionImpect = exponentialGravityMotionImpact(living, posX, posY, posZ);
                    living.motionX += motionImpect.x;
                    living.motionY += motionImpect.y;
                    living.motionZ += motionImpect.z;
                }
            }
        }
    }

    public static Vec3d uniformGravityMotionImpact(EntityLivingBase living, double posX, double posY, double posZ,
            double scale) {
        double x = posX - living.posX;
        double y = posY - living.posY;
        double z = posZ - living.posZ;
        Vec3d vec = new Vec3d(x, y, z).normalize().scale(scale);
        return vec;
    }

    public static Vec3d exponentialGravityMotionImpact(EntityLivingBase living, double posX, double posY, double posZ) {
        // F = m * a
        // F = G * m * M / rSquare
        // m * a = G * m * M / rSquare
        // a = G * M / rSquare
        // where `a` is the motion on XYZ
        // `G` is a constant
        // `M` is the mass (pull) of the pulling source
        // `rSquare` is the distance squre between entityLiving and pulling source
        double x = posX - living.posX;
        double y = posY - living.posY;
        double z = posZ - living.posZ;
        Vec3d disposition = new Vec3d(x, y, z);
        Vec3d commonPull = disposition.normalize().scale(0.6);
        double rSquare = disposition.lengthSquared();
        rSquare = Math.max(rSquare, 1);
        return new Vec3d(commonPull.x / rSquare, commonPull.y / rSquare, commonPull.z / rSquare);
    }
}