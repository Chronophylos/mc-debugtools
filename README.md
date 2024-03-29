# Debug Tools

## Requirements

* LLibrary

## Usage

```
/debugtools <command> [<required args>] [optional args]
/dt <command> [<required args>] [optional args]
```

### Commands

#### Dump

Dump infos about items to a file.

```
/debugtools dump <which> [modid] [showNBT]
/debugtools d <which> [modid] [showNBT]
```

`which` is a string and determines which items to dump.

* **hand**, **h**: Dump your hands contents.
* **inventory**, **inv**, **i**: Dump your inventory and armor.
* **hotbar**, **b**: Dump your hotbar. *Not Yet Implemented*
* **registry**, **all**, **a**: Dump all registered items.

`modid` is a string which you can use to only dump a single mod. If you want to dump everything use `*` or `all`. Defaults to all.

`showNBT` is a boolean and should be `true` if you want NBT data as well. Defaults to `false`.


#### Info

```
/debugtools info
```

Show some infos for the item your are holding.


#### Make a Test World

Make a world into a test world.

* Disables Daylight and Weather Cycle
* Sets time to 1000
* Disables Mob Spawning
* Enable more debug info
* Enable flight

```
/debugtools makeTestWorld
```
