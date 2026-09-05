# Auto Mend Armor

A Fabric 1.21.11 mod that changes how Mending repairs equipped armor.

## What it does

- Uses XP orbs collected by the player, including XP produced by splash XP bottles.
- Repairs only equipped armor pieces that have Mending and are damaged.
- Chooses the eligible armor piece with the lowest current durability first.
- Repairs a piece only until its remaining durability reaches 400, then checks the other pieces.
- Does not create XP, throw bottles, or repair armor automatically without collected XP.

## Requirements

- Minecraft 1.21.11
- Fabric Loader 0.19.2 or newer
- Fabric API for 1.21.11
- Java 21
