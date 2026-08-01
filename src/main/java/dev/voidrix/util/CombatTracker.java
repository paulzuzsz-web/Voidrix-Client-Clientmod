package dev.voidrix.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Observes your fights so the combat widgets have something to show.
 *
 * <p>Purely an observer: it reads the attack key, what is under your crosshair and your own health,
 * and derives who you are fighting, how far away they were and how many hits you have strung
 * together. It never sends input, never changes reach and never touches an entity - everything here
 * is information the client already has on screen.
 *
 * <p>Sampled per frame rather than per tick, because a fast click can start and finish inside one
 * tick and would otherwise be missed entirely.
 */
public final class CombatTracker {
    /** How long a target stays "current" after the last interaction. */
    private static final long TARGET_TIMEOUT_MS = 4000L;
    /** Gap after which a combo is considered broken. */
    private static final long COMBO_TIMEOUT_MS = 2500L;

    private static LivingEntity target;
    private static long targetSeenAt;

    private static int combo;
    private static long lastHitAt;
    private static double lastReach = -1;

    private static float previousHealth = -1f;
    private static long lastDamageAt;
    private static float lastDamageAmount;

    private static boolean attackWasDown;
    private static long lastFrame = -1;

    private CombatTracker() {
    }

    /**
     * Advances the tracker. Safe to call many times per frame - only the first call in a given
     * frame does any work, so every combat widget can call it without coordinating.
     */
    public static void update() {
        long now = System.currentTimeMillis();
        if (now == lastFrame) {
            return;
        }
        lastFrame = now;

        Minecraft mc = Minecraft.getInstance();
        var player = mc.player;
        if (player == null || mc.level == null) {
            reset();
            return;
        }

        boolean inGame = mc.mouseHandler.isMouseGrabbed();

        // A swing that lands on an entity: record the target, the reach and the combo.
        boolean attackDown = inGame && mc.options.keyAttack.isDown();
        if (attackDown && !attackWasDown) {
            HitResult hit = mc.hitResult;
            if (hit != null && hit.getType() == HitResult.Type.ENTITY
                    && hit instanceof EntityHitResult entityHit
                    && entityHit.getEntity() instanceof LivingEntity living) {
                Vec3 eye = player.getEyePosition();
                lastReach = eye.distanceTo(hit.getLocation());

                if (living != target || now - lastHitAt > COMBO_TIMEOUT_MS) {
                    combo = 1;
                } else {
                    combo++;
                }
                target = living;
                targetSeenAt = now;
                lastHitAt = now;
            }
        }
        attackWasDown = attackDown;

        // Keep the crosshair target warm so the widget does not blink out between swings.
        HitResult hover = mc.hitResult;
        if (hover != null && hover.getType() == HitResult.Type.ENTITY
                && hover instanceof EntityHitResult entityHit
                && entityHit.getEntity() instanceof LivingEntity living
                && living == target) {
            targetSeenAt = now;
        }

        if (target != null && (now - targetSeenAt > TARGET_TIMEOUT_MS || !target.isAlive())) {
            target = null;
        }
        if (now - lastHitAt > COMBO_TIMEOUT_MS) {
            combo = 0;
        }

        // Damage taken, derived from our own health dropping.
        float health = player.getHealth() + player.getAbsorptionAmount();
        if (previousHealth >= 0f && health < previousHealth - 0.01f) {
            lastDamageAmount = previousHealth - health;
            lastDamageAt = now;
        }
        previousHealth = health;
    }

    private static void reset() {
        target = null;
        combo = 0;
        lastReach = -1;
        previousHealth = -1f;
    }

    /** The entity you are currently fighting, or null. */
    public static LivingEntity target() {
        return target;
    }

    /** Distance in blocks from your eyes to where your last hit landed, or -1. */
    public static double lastReach() {
        return lastReach;
    }

    /** Consecutive hits on the current target, 0 when not in a combo. */
    public static int combo() {
        return combo;
    }

    public static long lastHitAt() {
        return lastHitAt;
    }

    /** Most recent damage taken, in half-hearts, and when it happened. */
    public static float lastDamage() {
        return lastDamageAmount;
    }

    public static long lastDamageAt() {
        return lastDamageAt;
    }

    /** Distance from the player to an entity, in blocks. */
    public static double distanceTo(Entity entity) {
        var player = Minecraft.getInstance().player;
        if (player == null || entity == null) {
            return -1;
        }
        return player.position().distanceTo(entity.position());
    }
}
