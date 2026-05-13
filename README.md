<div align="center">

<img src="https://img.shields.io/badge/Java-11+-orange?style=for-the-badge&logo=openjdk&logoColor=white"/>
<img src="https://img.shields.io/badge/Spigot-1.8.8+-yellow?style=for-the-badge"/>
<img src="https://img.shields.io/badge/Version-1.0.0-5865F2?style=for-the-badge"/>
<img src="https://img.shields.io/badge/License-Commercial-e74c3c?style=for-the-badge"/>

# RyzenClans API

Hook into clan data, relations, ranks, and permissions from your own plugin — without touching internals.

</div>

---

## Installation

The API jar is `compileOnly`. It should **never** end up shaded into your plugin — the full RyzenClans plugin handles everything at runtime.

**Gradle**
```groovy
dependencies {
    compileOnly files('libs/RyzenClans-API-1.0.0.jar')
}
```

**Maven**
```xml
<dependency>
    <groupId>dev.imshadow</groupId>
    <artifactId>RyzenClans-API</artifactId>
    <version>1.0.0</version>
    <scope>provided</scope>
</dependency>
```

Then in your `plugin.yml`, declare the dependency. Use `soft-depend` if your plugin can run without clans, or `depend` if it can't:

```yaml
soft-depend: [RyzenClan]
```

---

## Getting Started

Everything goes through `RyzenClansProvider`. If you're using `soft-depend`, always check `isAvailable()` first — calling `get()` when the plugin isn't loaded throws `IllegalStateException`.

```java
public class MyPlugin extends JavaPlugin {

private IRyzenClansAPI clansAPI = null;

@Override
public void onEnable() {
    if (Bukkit.getPluginManager().isPluginEnabled("RyzenClans")) {
        clansAPI = RyzenClansProvider.get();
        getLogger().info("RyzenClans found. Clan features enabled.");
    } else {
        getLogger().warning("RyzenClans not found. Clan features will be disabled.");
    }
}
```

That's it. One object, and you have access to everything.

---

## What you can do

### Look up clans

```java
// By name — stored in lowercase internally
Optional<? extends IClan> clan = api.getClan("warriors");

// By player
Optional<? extends IClan> clan = api.getPlayerClan(player);
Optional<? extends IClan> clan = api.getPlayerClan(player.getUniqueId());

boolean hasClan = api.hasPlayerClan(player);

// All clans, or top N sorted by points
Collection<? extends IClan> all = api.getAllClans();
List<? extends IClan> top10 = api.getTopClans(10);
```

### Check relations

```java
// Full enum — SAME_CLAN, ALLY, ENEMY, FOCUSED, NEUTRAL, NONE
ClanRelation relation = api.getRelation(player1, player2);

// Or just the checks you actually need
api.areSameClan(player1, player2);
api.areAllies(player1, player2);
api.areEnemies(player1, player2);

// Works with clan names too
api.areAllies("alpha", "beta");
api.isFocused("alpha", "beta");
```

`FOCUSED` means the first player's clan has declared focus on the second's. It's directional.

### Ranks and permissions

```java
Optional<ClanRank> rank = api.getPlayerRank(player);

api.isLeader(player.getUniqueId());
api.canPlayerManage(player.getUniqueId()); // ADMIN or above
api.canPlayerInvite(player.getUniqueId());
api.canPlayerKick(player.getUniqueId());
```

### Clan stats

```java
api.getClanDisplayName(uuid); // color-coded display name
api.getClanPrefix(uuid); // color-coded prefix
api.getClanPoints("warriors");
api.getClanSize("warriors");
api.isClanFriendlyFireEnabled("warriors");

api.getClanAllies("warriors"); // Set<String> of clan names
api.getClanEnemies("warriors");
api.getClanMembers("warriors"); // Set<UUID>
```

---

## Working with IClan directly

If you retrieved a clan object, it exposes everything about it without extra API calls:

```java
clan.getName(); // internal name (lowercase)
clan.getDisplayName(); // formatted with color
clan.getPrefix();
clan.getLeader();  // UUID
clan.getCreationTime(); // Unix timestamp in ms

// Members
clan.getMembers(); // Set<UUID>
clan.getMemberRanks(); // Map<UUID, ClanRank>
clan.getRank(uuid);
clan.isMember(uuid);
clan.isInvited(uuid);
clan.getMemberKills(uuid);
clan.getMemberDeaths(uuid);
clan.getMemberJoinedTime(uuid);

// Relations
clan.getAllies(); // Set<String> of clan names
clan.getEnemies();
clan.getFocused();
clan.isAlly("beta");
clan.isEnemy("beta");

// Stats
clan.getPoints();
clan.getBankBalance();
clan.isFriendlyFire();

// Locations
clan.hasClanBase();
clan.getClanBase(); // Location, null if not set
clan.getRallyPoint(); // Location, null if not set

// Permission checks per member
clan.canManage(uuid);
clan.canInvite(uuid);
clan.canKick(uuid);
clan.canSetBase(uuid);
clan.canDeposit(uuid);
clan.canWithdraw(uuid);
clan.canRally(uuid);
```

---

## ClanRank

Four ranks, ordered by authority:

| Rank | Level |
|---|---|
| `LEADER` | 4 |
| `ADMIN` | 3 |
| `MODERATOR` | 2 |
| `MEMBER` | 1 |

```java
ClanRank rank = api.getPlayerRank(uuid).orElse(null);
if (rank == null) return; // player has no clan

rank.getLevel();
rank.getDisplayName(); // pulled from ranks.yml
rank.isHigherThan(ClanRank.MEMBER);
rank.canPromoteTo(ClanRank.ADMIN);
rank.hasPermission("invite"); // checks node or wildcard *
rank.getPermissions();
```

---

## ClanRelation

Returned by `getRelation(player1, player2)`. Represents the relationship **from player1's perspective**.

| Value | Meaning |
|---|---|
| `SAME_CLAN` | Same clan |
| `ALLY` | Clans are allied |
| `ENEMY` | Clans are enemies |
| `FOCUSED` | Player1's clan is focusing player2's clan |
| `NEUTRAL` | No defined relationship |
| `NONE` | One or both players have no clan |

---

## Examples

**Block friendly fire**

```java
@EventHandler(ignoreCancelled = true)
public void onDamage(EntityDamageByEntityEvent event) {
    if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) return;

    Player attacker = (Player) event.getDamager();
    Player victim = (Player) event.getEntity();

    if (!api.areSameClan(attacker, victim)) return;

    api.getPlayerClan(attacker).ifPresent(clan -> {
        if (!clan.isFriendlyFire()) event.setCancelled(true);
    });
}
```

**Clan prefix in chat**

```java
@EventHandler
public void onChat(AsyncPlayerChatEvent event) {
    String prefix = api.getPlayerClan(event.getPlayer())
        .map(IClan::getPrefix)
        .orElse("");

    event.setFormat(prefix + " " + event.getPlayer().getName() + ": %2$s");
}
```

**Team buff on same clan or allied**

```java
public void applyTeamBuff(Player caster, Player target) {
    ClanRelation relation = api.getRelation(caster, target);
    if (relation == ClanRelation.SAME_CLAN || relation == ClanRelation.ALLY) {
        target.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 1));
    }
}
```

---

## License

RyzenClans is proprietary software. Each license is bound to a single server and validated online at startup.

Redistribution, resale, decompilation, or modification without explicit written permission from the author is strictly prohibited.

Purchase your license at **[discord.gg/RKu7vfwtRW](https://discord.gg/RKu7vfwtRW)**

---

<div align="center">

Made by **ImShadow**

</div>
