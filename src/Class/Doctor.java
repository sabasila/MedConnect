package Class;

public class Doctor {
    private int id;
    private String fullName;
    private String photo;
    private String bio;
    private String ClinicAddress ;
    private double averageRating;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public double getAverageRating() { return averageRating; }
    public void setAverageRating(double averageRating) { this.averageRating = averageRating; }

    public String getClinicAddress() { return ClinicAddress; }
    public void setClinicAddress(double ClinicAddress) { this.ClinicAddress = String.valueOf(ClinicAddress); }
    }

