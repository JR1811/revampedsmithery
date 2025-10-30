package net.shirojr.revampedsmithery.util;

public class AnimationUtil {
    public static float getPunchInterpolation(float tickCooldown, float maxCooldown) {
        float progress = 1.0f - (tickCooldown / maxCooldown);
        float intensity = 0.8f;
        float frequency = 20f;
        float damping = 1.2f;

        float decay = (float) Math.pow(1.0f - progress, damping);
        return (float) Math.sin(progress * Math.PI * frequency) * decay * intensity;
    }
}
