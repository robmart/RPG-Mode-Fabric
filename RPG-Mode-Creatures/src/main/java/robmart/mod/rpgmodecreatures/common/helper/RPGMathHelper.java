package robmart.mod.rpgmodecreatures.common.helper;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public final class RPGMathHelper {

    public static final float TO_RADIANS = 0.017453292F;
    public static final double TO_DEGREES = 57.2957763671875D;

    public static Vec3d toAbsolutePos(Vec3d absPos, Vec3d relPos, Vec2f rotation) {
        float f = MathHelper.cos((rotation.y + 90.0F) * TO_RADIANS);
        float g = MathHelper.sin((rotation.y + 90.0F) * TO_RADIANS);
        float h = MathHelper.cos(-rotation.x * TO_RADIANS);
        float i = MathHelper.sin(-rotation.x * TO_RADIANS);
        float j = MathHelper.cos((-rotation.x + 90.0F) * TO_RADIANS);
        float k = MathHelper.sin((-rotation.x + 90.0F) * TO_RADIANS);
        Vec3d vec3d2 = new Vec3d(f * h, i, g * h);
        Vec3d vec3d3 = new Vec3d(f * j, k, g * j);
        Vec3d vec3d4 = vec3d2.crossProduct(vec3d3).multiply(-1.0D);
        double d = vec3d2.x * relPos.z + vec3d3.x * relPos.y + vec3d4.x * relPos.x;
        double e = vec3d2.y * relPos.z + vec3d3.y * relPos.y + vec3d4.y * relPos.x;
        double l = vec3d2.z * relPos.z + vec3d3.z * relPos.y + vec3d4.z * relPos.x;
        return new Vec3d(absPos.x + d, absPos.y + e, absPos.z + l);
    }
}
