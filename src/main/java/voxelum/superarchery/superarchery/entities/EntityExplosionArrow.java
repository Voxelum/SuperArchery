package voxelum.superarchery.superarchery.entities;

import com.google.common.collect.Sets;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.*;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

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
//            EntityPlayer shooter = (EntityPlayer) this.shootingEntity;
            if (!this.inWater) {
                newExplosion(this, this.posX, this.posY, this.posZ, 4, false, true);
                this.setDead();
            }
        }
    }

    private DamageSource getDamangeSource() {
        return (new EntityDamageSource("explosion.explosion_arrow", this)).setDifficultyScaled().setExplosion();
    }

    /**
     * returns a new explosion. Does initiation (at time of writing Explosion is not finished)
     */
    public static Explosion newExplosion(EntityExplosionArrow entityIn, double x, double y, double z, float strength, boolean causesFire, boolean damagesTerrain) {
        World world = entityIn.world;
        final Entity exploder = entityIn;
        float size = strength;
        Explosion explosion = new Explosion(entityIn.world, entityIn, x, y, z, strength, causesFire, damagesTerrain) {
            @Override
            public void doExplosionA() {
                Set<BlockPos> set = Sets.<BlockPos>newHashSet();
                for (int j = 0; j < 16; ++j) {
                    for (int k = 0; k < 16; ++k) {
                        for (int l = 0; l < 16; ++l) {
                            if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
                                double d0 = (double) ((float) j / 15.0F * 2.0F - 1.0F);
                                double d1 = (double) ((float) k / 15.0F * 2.0F - 1.0F);
                                double d2 = (double) ((float) l / 15.0F * 2.0F - 1.0F);
                                double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                                d0 = d0 / d3;
                                d1 = d1 / d3;
                                d2 = d2 / d3;
                                float f = size * (0.7F + world.rand.nextFloat() * 0.6F);
                                double d4 = x;
                                double d6 = y;
                                double d8 = z;

                                for (float f1 = 0.3F; f > 0.0F; f -= 0.22500001F) {
                                    BlockPos blockpos = new BlockPos(d4, d6, d8);
                                    IBlockState iblockstate = world.getBlockState(blockpos);

                                    if (iblockstate.getMaterial() != Material.AIR) {
                                        float f2 = exploder != null ? exploder.getExplosionResistance(this, world, blockpos, iblockstate) : iblockstate.getBlock().getExplosionResistance(world, blockpos, (Entity) null, this);
                                        f -= (f2 + 0.3F) * 0.3F;
                                    }

                                    if (f > 0.0F && (exploder == null || exploder.canExplosionDestroyBlock(this, world, blockpos, iblockstate, f))) {
                                        set.add(blockpos);
                                    }

                                    d4 += d0 * 0.30000001192092896D;
                                    d6 += d1 * 0.30000001192092896D;
                                    d8 += d2 * 0.30000001192092896D;
                                }
                            }
                        }
                    }
                }

                getAffectedBlockPositions().addAll(set);
                float f3 = size * 2.0F;
                int k1 = MathHelper.floor(x - (double) f3 - 1.0D);
                int l1 = MathHelper.floor(x + (double) f3 + 1.0D);
                int i2 = MathHelper.floor(y - (double) f3 - 1.0D);
                int i1 = MathHelper.floor(y + (double) f3 + 1.0D);
                int j2 = MathHelper.floor(z - (double) f3 - 1.0D);
                int j1 = MathHelper.floor(z + (double) f3 + 1.0D);
                List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(exploder, new AxisAlignedBB((double) k1, (double) i2, (double) j2, (double) l1, (double) i1, (double) j1));
                net.minecraftforge.event.ForgeEventFactory.onExplosionDetonate(world, this, list, f3);
                Vec3d vec3d = new Vec3d(x, y, z);

                for (int k2 = 0; k2 < list.size(); ++k2) {
                    Entity entity = list.get(k2);

                    if (!entity.isImmuneToExplosions()) {
                        double d12 = entity.getDistance(x, y, z) / (double) f3;

                        if (d12 <= 1.0D) {
                            double d5 = entity.posX - x;
                            double d7 = entity.posY + (double) entity.getEyeHeight() - y;
                            double d9 = entity.posZ - z;
                            double d13 = (double) MathHelper.sqrt(d5 * d5 + d7 * d7 + d9 * d9);

                            if (d13 != 0.0D) {
                                d5 = d5 / d13;
                                d7 = d7 / d13;
                                d9 = d9 / d13;
                                double d14 = (double) world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
                                double d10 = (1.0D - d12) * d14;
                                entity.attackEntityFrom(entityIn.getDamangeSource(), (float) ((int) ((d10 * d10 + d10) / 2.0D * 7.0D * (double) f3 + 1.0D)));
                                double d11 = d10;

                                if (entity instanceof EntityLivingBase) {
                                    d11 = EnchantmentProtection.getBlastDamageReduction((EntityLivingBase) entity, d10);
                                }

                                entity.motionX += d5 * d11;
                                entity.motionY += d7 * d11;
                                entity.motionZ += d9 * d11;

                                if (entity instanceof EntityPlayer) {
                                    EntityPlayer entityplayer = (EntityPlayer) entity;

                                    if (!entityplayer.isSpectator() && (!entityplayer.isCreative() || !entityplayer.capabilities.isFlying)) {
                                        getPlayerKnockbackMap().put(entityplayer, new Vec3d(d5 * d10, d7 * d10, d9 * d10));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        };
        if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(world, explosion)) return explosion;
        explosion.doExplosionA();
        explosion.doExplosionB(true);
        return explosion;
    }
}
