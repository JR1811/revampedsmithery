package net.shirojr.revampedsmithery.util;

import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class ShapeUtil {
    /**
     * Voxel Coordinates are the default pixels in a Block. A block is made out of 16 voxels in every axis
     *
     * @return a box scaled down to a block's size (div by 16)
     */
    public static Box getBoxFromVoxelCoordinates(Vec3d start, Vec3d end) {
        Vec3d localSpaceStart = new Vec3d(start.x / 16, start.y / 16, start.z / 16);
        Vec3d localSpaceEnd = new Vec3d(end.x / 16, end.y / 16, end.z / 16);
        return new Box(localSpaceStart, localSpaceEnd);
    }

    public static Vec3d rotatePoint(Vec3d point, Direction facing) {
        Vec3d result = point.subtract(0.5, 0.5, 0.5);
        double x;
        double z;

        switch (facing) {
            case SOUTH -> {
                x = -result.x;
                z = -result.z;
            }
            case WEST -> {
                x = result.z;
                z = -result.x;
            }
            case EAST -> {
                x = -result.z;
                z = result.x;
            }
            default -> {
                x = result.x;
                z = result.z;
            }
        }
        return new Vec3d(x, result.y, z).add(0.5, 0.5, 0.5f);
    }

    public static Box rotateBox(Box box, Direction facing) {
        Vec3d start = rotatePoint(new Vec3d(box.minX, box.minY, box.minZ), facing);
        Vec3d end = rotatePoint(new Vec3d(box.maxX, box.maxY, box.maxZ), facing);
        return new Box(start, end);
    }
}
