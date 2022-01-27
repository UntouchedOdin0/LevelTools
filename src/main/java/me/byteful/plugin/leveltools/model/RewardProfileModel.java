package me.byteful.plugin.leveltools.model;

import me.byteful.plugin.leveltools.api.RewardType;
import me.byteful.plugin.leveltools.api.item.LevelToolsItem;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public final class RewardProfileModel {
  @NotNull private final String name;
  @NotNull private final Map<Integer, RewardProcessor> rewards;

  public RewardProfileModel(@NotNull String name, @NotNull Map<Integer, RewardProcessor> rewards) {
    this.name = name;
    this.rewards = rewards;
  }

  public @NotNull String getName() {
    return name;
  }

  public @NotNull Map<Integer, RewardProcessor> getRewards() {
    return rewards;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    RewardProfileModel that = (RewardProfileModel) o;
    return name.equals(that.name) && rewards.equals(that.rewards);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, rewards);
  }

  @Override
  public String toString() {
    return "RewardProfileModel{" + "name='" + name + '\'' + ", rewards=" + rewards + '}';
  }

  private static final class RewardProcessor {
    @NotNull private final RewardType type;
    @NotNull private final String[] args;

    public RewardProcessor(@NotNull RewardType type, @NotNull String[] args) {
      this.type = type;
      this.args = args;
    }

    public @NotNull RewardType getType() {
      return type;
    }

    public @NotNull String[] getArgs() {
      return args;
    }

    public void process(@NotNull final LevelToolsItem item, @NotNull Player player) {
      type.apply(item, args, player);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      RewardProcessor that = (RewardProcessor) o;
      return type == that.type && Arrays.equals(args, that.args);
    }

    @Override
    public int hashCode() {
      int result = Objects.hash(type);
      result = 31 * result + Arrays.hashCode(args);
      return result;
    }

    @Override
    public String toString() {
      return "RewardProcessor{" + "type=" + type + ", args=" + Arrays.toString(args) + '}';
    }
  }
}
