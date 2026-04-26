package com.codex.composer.api.v1.targeting;

import org.lilbrocodes.constructive.api.v1.anno.Constructive;

@Constructive(builder = true)
public class TargetingContext {
    public final int minDistance;
    public final int maxDistance;
    public final int decayTicks;
    public final boolean targetNonLiving;
    public final boolean targetTamed;
    public final boolean targetDead;

    TargetingContext(int minDistance, int maxDistance, int decayTicks, boolean targetNonLiving, boolean targetTamed, boolean targetDead) {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.decayTicks = decayTicks;
        this.targetNonLiving = targetNonLiving;
        this.targetTamed = targetTamed;
        this.targetDead = targetDead;
    }

    public static TargetingContext getDefault() {
        return TargetingContextBuilder.create().build();
    }

    public static TargetingContext getIgnoring() {
        return TargetingContextBuilder.create().minDistance(0).maxDistance((int) Math.floor(Math.sqrt(Integer.MAX_VALUE))).targetDead(true).targetTamed(true).targetNonLiving(true).decayTicks(Integer.MAX_VALUE).build();
    }
}
