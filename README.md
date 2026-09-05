# Mending Priority

Minecraft 1.21.11 Fabric.

**G** toggles the feature.

When ON:
- Looks only at equipped armor with Mending.
- Ignores armor already at 400 durability or higher.
- Picks whichever eligible armor piece currently has the lowest durability.
- Sends already-collected XP to that piece.
- Recalculates after each XP repair.
- Does not throw XP bottles or generate XP.
- If no armor is below 400, vanilla Mending behavior is left unchanged.

For multiplayer, install the mod on both client and server.
Java 21 is required.

Build:
`gradlew.bat build`
