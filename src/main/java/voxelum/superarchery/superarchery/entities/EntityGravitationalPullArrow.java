package voxelum.superarchery.superarchery.entities;

import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import voxelum.superarchery.superarchery.GravityPull;

//jian bu qi lai jian
public class EntityGravitationalPullArrow extends EntityTippedArrow {
    private int age = 5 * 20; // live for about 5 sec
    private double radius = 30D;
    private UUID shooterUUID;

    public EntityGravitationalPullArrow(World worldIn) {
        super(worldIn);
    }

    public EntityGravitationalPullArrow(World worldIn, EntityLivingBase shoot, ItemStack item) {
        super(worldIn, shoot);
        this.age = 5 * 20;
        this.shooterUUID = shoot.getUniqueID();
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("pull_age", age);
        compound.setDouble("pull_radius", radius);
        compound.setUniqueId("pull_shooter", shooterUUID);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        radius = compound.getDouble("pull_radius");
        age = compound.getInteger("pull_age");
        shooterUUID = compound.getUniqueId("pull_shooter");
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
        GravityPull.pull(this, this.shooterUUID, world, posX, posY, posZ, radius);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!this.world.isRemote) {
            this.age--;
            if (this.age < 0) {
                this.setDead();
            } else {
                updateGravity();
            }
        } else {
            // if (rand.nextInt(100) == 0) {
            // this.world.playSound((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D,
            // (double) pos.getZ() + 0.5D,
            // SoundEvents.BLOCK_PORTAL_AMBIENT, SoundCategory.BLOCKS, 0.5F,
            // rand.nextFloat() * 0.4F + 0.8F,
            // false);
            // }

            for (int i = 0; i < 4; ++i) {
                double d0 = (double) ((float) posX + rand.nextFloat());
                double d1 = (double) ((float) posY + rand.nextFloat());
                double d2 = (double) ((float) posZ + rand.nextFloat());
                double d3 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
                double d4 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
                double d5 = ((double) rand.nextFloat() - 0.5D) * 0.5D;
                // int j = rand.nextInt(2) * 2 - 1;

                // if (world.getBlockState(pos.west()).getBlock() != this
                //         && world.getBlockState(pos.east()).getBlock() != this) {
                //     d0 = (double) pos.getX() + 0.5D + 0.25D * (double) j;
                //     d3 = (double) (rand.nextFloat() * 2.0F * (float) j);
                // } else {
                //     d2 = (double) pos.getZ() + 0.5D + 0.25D * (double) j;
                //     d5 = (double) (rand.nextFloat() * 2.0F * (float) j);
                // }

                world.spawnParticle(EnumParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
            }
        }

    }

}
