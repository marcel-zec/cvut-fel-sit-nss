package cz.cvut.fel.nss.parttimejobportal.model;

public enum Role {
    USER("ROLE_USER"), ADMIN("ROLE_ADMIN"), SUPERUSER("ROLE_SUPERUSER");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return role;
    }
}

