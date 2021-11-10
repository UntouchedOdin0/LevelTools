package me.byteful.plugin.leveltools.api.item.impl;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.byteful.plugin.leveltools.LevelToolsPlugin;
import me.byteful.plugin.leveltools.LevelToolsUtil;
import me.byteful.plugin.leveltools.api.item.LevelToolsItem;
import me.lucko.helper.text3.Text;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class NBTLevelToolsItem implements LevelToolsItem {
  @NotNull private NBTItem nbt;
  @NotNull private Map<Enchantment, Integer> enchantments;

  public NBTLevelToolsItem(@NotNull ItemStack stack) {
    this.nbt = new NBTItem(stack);
    this.enchantments = new HashMap<>();
  }

  @NotNull
  @Override
  public ItemStack getItemStack() {
    final ItemStack stack = nbt.getItem().clone();

    final ConfigurationSection cs =
        LevelToolsPlugin.getInstance().getConfig().getConfigurationSection("display");
    List<String> lore = cs.getStringList("default");

    for (String key : cs.getKeys(false)) {
      if (key.equalsIgnoreCase(stack.getType().name())) {
        lore = cs.getStringList(key);
      }
    }

    lore =
        lore.stream()
            .map(
                str ->
                    Text.colorize(
                        str.replace("{level}", String.valueOf(nbt.getInteger("levelToolsLevel")))
                            .replace("{xp}", String.valueOf(getXp()))
                            .replace("{max_xp}", String.valueOf(getMaxXp()))
                            .replace(
                                "{progress_bar}",
                                LevelToolsUtil.createDefaultProgressBar(getXp(), getMaxXp()))))
            .collect(Collectors.toList());

    final ItemMeta meta = stack.getItemMeta();
    meta.setLore(lore);
    stack.setItemMeta(meta);

    for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
      stack.addUnsafeEnchantment(entry.getKey(), entry.getValue());
    }

    return stack;
  }

  @Override
  public int getLevel() {
    return nbt.getInteger("levelToolsLevel");
  }

  @Override
  public void setLevel(int level) {
    if (level <= 0) {
      setLevel0(0);

      return;
    }

    setLevel0(level);
  }

  @Override
  public double getXp() {
    return nbt.getDouble("levelToolsXp");
  }

  @Override
  public void setXp(double xp) {
    if (xp <= 0.0D) {
      setXp0(0.0D);

      return;
    }

    setXp0(xp);
  }

  private void setLevel0(int level) {
    nbt.setInteger("levelToolsLevel", level);
  }

  private void setXp0(double xp) {
    nbt.setDouble("levelToolsXp", xp);
  }

  @Override
  public double getMaxXp() {
    return LevelToolsUtil.round(
        (getLevel() + 1) * LevelToolsPlugin.getInstance().getConfig().getDouble("level_multiplier"),
        1);
  }

  @Override
  public void enchant(Enchantment enchantment, int level) {
    enchantments.put(enchantment, level);
  }

  @NotNull
  public NBTItem getNBT() {
    return nbt;
  }

  public void setNBT(@NotNull NBTItem nbt) {
    this.nbt = nbt;
  }

  @NotNull
  public Map<Enchantment, Integer> getEnchantments() {
    return enchantments;
  }

  public void setEnchantments(@NotNull Map<Enchantment, Integer> enchantments) {
    this.enchantments = enchantments;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NBTLevelToolsItem that = (NBTLevelToolsItem) o;
    return nbt.equals(that.nbt) && enchantments.equals(that.enchantments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nbt, enchantments);
  }

  @Override
  public String toString() {
    return "LevelToolsItemImpl{" + "nbt=" + nbt + ", enchantments=" + enchantments + '}';
  }
}