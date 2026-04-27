package dev.imshadow.api;

import java.util.HashSet;
import java.util.Set;

public enum ClanRank {
    LEADER(4, "Leader"),
    ADMIN(3, "Admin"),
    MODERATOR(2, "Moderator"),
    MEMBER(1, "Member");

    private final int level;
    private String displayName;
    private Set<String> permissions;

    ClanRank(int level, String defaultDisplayName) {
        this.level = level;
        this.displayName = defaultDisplayName;
        this.permissions = new HashSet<>();
    }

    public int getLevel() {
        return level;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isHigherThan(ClanRank other) {
        return this.level > other.level;
    }

    public boolean canPromoteTo(ClanRank target) {
        return this.level > target.level;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions != null ? permissions : new HashSet<>();
    }

    public boolean hasPermission(String permission) {
        return permissions.contains("*") || permissions.contains(permission);
    }

    public Set<String> getPermissions() {
        return new HashSet<>(permissions);
    }
}
