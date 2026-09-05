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

## Build the JAR using only the GitHub website

The repository includes a GitHub Actions workflow, so you do not need to create
`gradlew`, `gradlew.bat`, or the Gradle wrapper files yourself.

### 1. Create the repository

1. Sign in to [GitHub](https://github.com).
2. Select **+** in the top-right corner, then **New repository**.
3. Name the repository `AutoMendArmor`.
4. Select **Public** or **Private**.
5. Click **Create repository**.

### 2. Add the project files

On the empty repository page, select **Add file** and then **Upload files**.
Upload the project files while keeping this structure:

```text
AutoMendArmor/
├── build.gradle
├── gradle.properties
├── settings.gradle
├── README.md
├── .github/
│   └── workflows/
│       └── build.yml
└── src/
    └── main/
        ├── java/
        │   └── com/kodari/automendarmor/
        │       ├── ArmorRepairer.java
        │       ├── AutoMendArmor.java
        │       └── mixin/ExperienceOrbEntityMixin.java
        └── resources/
            ├── automendarmor.mixins.json
            └── fabric.mod.json
```

`build.gradle` must be in the repository root. Do not put it inside another
folder.

If GitHub does not preserve a folder while uploading, use **Add file** →
**Create new file** and type the complete path into the filename box, for
example:

```text
src/main/java/com/kodari/automendarmor/ArmorRepairer.java
```

Paste that file's contents, then commit it. Repeat for each file.

### 3. Add the workflow

The file `.github/workflows/build.yml` tells GitHub to install Java and Gradle,
compile the mod, and publish the JAR as a downloadable artifact. If it was not
uploaded with the other files:

1. Select **Add file** → **Create new file**.
2. Set the filename to `.github/workflows/build.yml`.
3. Add the workflow file from this project.
4. Select **Commit changes**.

### 4. Run the build

1. Open the repository's **Actions** tab.
2. Select **Build AutoMendArmor**.
3. Select **Run workflow** and then select the green **Run workflow** button.
   A push to the repository also starts the workflow automatically.
4. Wait until the job has a green check mark.

### 5. Download the JAR

1. Open the completed workflow run.
2. Scroll to the **Artifacts** section at the bottom.
3. Select **AutoMendArmor** to download a ZIP file.
4. Extract the ZIP.
5. Copy `auto-mend-armor-1.0.0.jar` into your Minecraft `mods` folder.

Do not install a file ending in `-sources.jar`; that file contains source code,
not the playable mod.

## Source layout

Common code is under `src/main/java`, and resources are under
`src/main/resources`. The project uses Yarn mappings and Java 21 for Fabric
1.21.11.