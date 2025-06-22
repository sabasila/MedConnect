package Class;

public class User {
    private int id;
    private String fullName;
    private String personalId;
    private String userType;

    public User() {}

    public User(int id, String fullName, String personalId, String userType) {
        this.id = id;
        this.fullName = fullName;
        this.personalId = personalId;
        this.userType = userType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return fullName + " (პირადი №: " + personalId + ", ტიპი: " + userType + ")";
    }
}
