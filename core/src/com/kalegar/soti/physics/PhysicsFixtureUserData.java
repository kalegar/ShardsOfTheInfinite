package com.kalegar.soti.physics;

public class PhysicsFixtureUserData {

    public enum FixtureUserDataType{
        ENTITY,
        TERRAIN
    }

    private FixtureUserDataType type;

    public PhysicsFixtureUserData(FixtureUserDataType type){
        this.type = type;
    }

    public FixtureUserDataType getType(){
        return type;
    }

    public void setType(FixtureUserDataType type){
        this.type = type;
    }
}
