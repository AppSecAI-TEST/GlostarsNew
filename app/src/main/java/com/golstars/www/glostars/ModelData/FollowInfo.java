package com.golstars.www.glostars.ModelData;

public class FollowInfo
{
    public String originatedById;
    public String destinationById;
    public boolean isMutual;
    public boolean originateFollowDestination;
    public boolean destinationFollowOriginate;
    public String originateProfilePhoto;
    public String destinationProfilePhoto;
    public String destinationUserName;
    public String originateUserName;

    @Override
    public String toString() {
        return "FollowInfo{" +
                "originatedById='" + originatedById + '\'' +
                ", destinationById='" + destinationById + '\'' +
                ", isMutual=" + isMutual +
                ", originateFollowDestination=" + originateFollowDestination +
                ", destinationFollowOriginate=" + destinationFollowOriginate +
                ", originateProfilePhoto='" + originateProfilePhoto + '\'' +
                ", destinationProfilePhoto='" + destinationProfilePhoto + '\'' +
                ", destinationUserName='" + destinationUserName + '\'' +
                ", originateUserName='" + originateUserName + '\'' +
                '}';
    }
}