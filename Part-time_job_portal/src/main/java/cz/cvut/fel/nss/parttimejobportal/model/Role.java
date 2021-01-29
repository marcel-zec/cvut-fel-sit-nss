package cz.cvut.fel.nss.parttimejobportal.model;

public enum Role {
    USER("ROLE_USER"), ADMIN("ROLE_ADMIN"), MANAGER("ROLE_MANAGER");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return role;
    }
}

