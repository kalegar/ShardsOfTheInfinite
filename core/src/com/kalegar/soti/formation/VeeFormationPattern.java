package com.kalegar.soti.formation;

import com.badlogic.gdx.ai.fma.FormationPattern;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class VeeFormationPattern implements FormationPattern<Vector2> {

    int numberOfSlots;

    /** The radius of one member */
    float memberRadius;

    /** The angle between the two lines of the V */
    private float veeAngle = 50 * MathUtils.degreesToRadians;

    public VeeFormationPattern(float memberRadius) {
        this.memberRadius = memberRadius;
    }

    public VeeFormationPattern(float memberRadius, float angle) {
        this(memberRadius);
        this.veeAngle = angle;
    }

    @Override
    public void setNumberOfSlots(int numberOfSlots) {
        this.numberOfSlots = numberOfSlots;
    }

    @Override
    public Location<Vector2> calculateSlotLocation(Location<Vector2> outLocation, int slotNumber) {
        if (numberOfSlots > 1) {
            // Determine which side of the V this slot is on.
            int side = slotNumber % 2;
            // Index on V is offset by 1 for the right side.
            int veeIndex = slotNumber / 2 + side;
            float dist  = veeIndex * memberRadius;
            float halfVee = veeAngle/2f;
            float angle = MathUtils.PI - halfVee + (((float)side)*veeAngle);

            outLocation.angleToVector(outLocation.getPosition(), angle).scl(dist);

            // The members should be forward
            outLocation.setOrientation(0f);
        }
        else {
            outLocation.getPosition().setZero();
            //outLocation.setOrientation(MathUtils.PI2 * slotNumber);
        }

        // Return the slot location
        return outLocation;
    }

    @Override
    public boolean supportsSlots(int slotCount) {
        // Support any number of slots.
        return true;
    }

    public void setVeeAngle(float veeAngle) {
        this.veeAngle = veeAngle;
    }

    public float getVeeAngle() {
        return veeAngle;
    }
}
