package filler;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class UserDTO {
    private String firstName;
    private String lastName;
    private String alias;
    private String imageUrl;

    private String hashedPassword;
    private int numFollowers;
    private int numFollowees;

    public UserDTO() {}
    public UserDTO(User u) {
        // only set the properties we know
        this.setFirstName(u.getFirstName());
        this.setLastName(u.getLastName());
        this.setAlias(u.getAlias());
        this.setImageUrl(u.getImageUrl());
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @DynamoDbPartitionKey
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public int getNumFollowers() {
        return numFollowers;
    }

    public void setNumFollowers(int numFollowers) {
        this.numFollowers = numFollowers;
    }

    public int getNumFollowees() {
        return numFollowees;
    }

    public void setNumFollowees(int numFollowees) {
        this.numFollowees = numFollowees;
    }
}
