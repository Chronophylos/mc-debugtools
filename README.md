# Debug Tools

## Usage

```
/debugtools <command> [<required args>] [optional args]
/dt <command> [<required args>] [optional args]
```

### Commands

#### Dump

```
/debugtools dump <which> [showNBT]
/debugtools d <which> [showNBT]
```

`Which` is a string and determines which items to dump.

* **hand**, **h**: Dump your hands contents.
* **inventory**, **inv**, **i**: Dump your inventory and armor.
* **hotbar**, **b**: Dump your hotbar. *Not Yet Implemented*
* **registry**, **all**, **a**: Dump all registered items.

`showNBT` is a boolean and should be `true` if you want NBT data as well. Defaults to `false`.

