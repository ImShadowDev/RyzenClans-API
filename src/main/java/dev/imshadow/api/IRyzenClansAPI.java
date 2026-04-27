package dev.imshadow.api;

import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface IRyzenClansAPI {

    Optional<? extends IClan> getClan(String clanName);

    Optional<? extends IClan> getPlayerClan(UUID playerUuid);

    Optional<? extends IClan> getPlayerClan(Player player);

    boolean hasPlayerClan(UUID playerUuid);

    boolean hasPlayerClan(Player player);

    ClanRelation getRelation(UUID player1, UUID player2);

    ClanRelation getRelation(Player player1, Player player2);

    boolean areSameClan(UUID player1, UUID player2);

    boolean areSameClan(Player player1, Player player2);

    boolean areAllies(String clan1Name, String clan2Name);

    boolean areAllies(UUID player1, UUID player2);

    boolean areAllies(Player player1, Player player2);

    boolean areEnemies(String clan1Name, String clan2Name);

    boolean areEnemies(UUID player1, UUID player2);

    boolean areEnemies(Player player1, Player player2);

    boolean isFocused(String clanName, String targetClanName);

    Collection<? extends IClan> getAllClans();

    List<? extends IClan> getTopClans(int limit);

    boolean isLeader(UUID playerUuid);

    boolean isLeader(Player player);

    Optional<ClanRank> getPlayerRank(UUID playerUuid);

    Optional<ClanRank> getPlayerRank(Player player);

    boolean canPlayerManage(UUID playerUuid);

    boolean canPlayerManage(Player player);

    boolean canPlayerInvite(UUID playerUuid);

    boolean canPlayerInvite(Player player);

    boolean canPlayerKick(UUID playerUuid);

    boolean canPlayerKick(Player player);

    String getClanDisplayName(UUID playerUuid);

    String getClanDisplayName(Player player);

    String getClanPrefix(UUID playerUuid);

    String getClanPrefix(Player player);

    int getClanPoints(String clanName);

    int getClanSize(String clanName);

    boolean isClanFriendlyFireEnabled(String clanName);

    Set<String> getClanAllies(String clanName);

    Set<String> getClanEnemies(String clanName);

    Set<UUID> getClanMembers(String clanName);
}
