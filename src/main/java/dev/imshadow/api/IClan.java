package dev.imshadow.api;

import org.bukkit.Location;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface IClan {

    String getName();

    String getDisplayName();

    String getPrefix();

    UUID getLeader();

    Set<UUID> getMembers();

    Map<UUID, ClanRank> getMemberRanks();

    Set<String> getAllies();

    Set<String> getEnemies();

    Set<String> getFocused();

    int getPoints();

    int getSize();

    boolean isEmpty();

    boolean isFriendlyFire();

    double getBankBalance();

    Location getRallyPoint();

    long getLastRallyTime();

    long getCreationTime();

    Location getClanBase();

    boolean hasClanBase();

    boolean isMember(UUID uuid);

    boolean isInvited(UUID uuid);

    boolean isAlly(String clanName);

    boolean isEnemy(String clanName);

    boolean isFocused(String clanName);

    ClanRank getRank(UUID uuid);

    boolean canManage(UUID uuid);

    boolean canSetBase(UUID uuid);

    boolean canDeleteBase(UUID uuid);

    boolean canDeposit(UUID uuid);

    boolean canWithdraw(UUID uuid);

    boolean canInvite(UUID uuid);

    boolean canKick(UUID uuid);

    boolean canRally(UUID uuid);

    int getMemberKills(UUID uuid);

    int getMemberDeaths(UUID uuid);

    long getMemberJoinedTime(UUID uuid);
}
