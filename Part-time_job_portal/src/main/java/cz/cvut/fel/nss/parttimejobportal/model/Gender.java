package cz.cvut.fel.nss.parttimejobportal.model;

public enum Gender {
    WOMAN("WOMAN"), MAN("MAN");

    private final String gender;

    Gender(String gender) {
        this.gender = gender;
    }


    @Override
    public String toString() {
        return gender;
    }
}

