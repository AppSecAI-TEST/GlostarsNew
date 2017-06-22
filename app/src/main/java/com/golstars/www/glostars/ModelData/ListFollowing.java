package com.golstars.www.glostars.ModelData;

public class ListFollowing
{
    public String id;
    public String email;
    public String name;
    public String lastName;
    public String gender;
    public String profilePicURL;
    public int followerCount;
    public int followingCount;
    public int totalPhoto;

    @Override
    public String toString() {
        return "ListFollowing{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", profilePicURL='" + profilePicURL + '\'' +
                ", followerCount=" + followerCount +
                ", followingCount=" + followingCount +
                ", totalPhoto=" + totalPhoto +
                '}';
    }
}