package voxelum.superarchery.superarchery.entities;

import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBow;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.event.sound.SoundEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class EntityTeleportArrow extends EntityTippedArrow {
    public EntityTeleportArrow(World worldIn) {
        super(worldIn);
    }

    public EntityTeleportArrow(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
    }

    @Override
    protected void onHit(RayTraceResult raytraceResultIn) {
        super.onHit(raytraceResultIn);
        Entity entity = this.shootingEntity;
        if (entity instanceof EntityPlayer && !entity.world.isRemote) {
            EntityPlayer player = (EntityPlayer) this.shootingEntity;
            if (!this.inWater) {
                player.setPositionAndUpdate(this.posX, this.posY, this.posZ);
                player.fallDistance = 0;
                this.setDead();
            } else {
                this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 0.5f, 1);
                ITextComponent arrowInWater = new TextComponentString("传送箭在水中爆炸了");
                player.sendMessage(arrowInWater);
            }
        }
    }
}