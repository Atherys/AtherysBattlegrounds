# AtherysBattlegrounds
A capture-and-hold mini-game intended to be played in the open-world for the A'therys Horizons server

## Commands

### Base Commands

| Aliases                | Permission                        | Description                                        |
|------------------------|-----------------------------------|----------------------------------------------------|
| `/team`                | `atherysbattlegrounds.team.base`  | Shows info on own current team                     |
| `/team info`           | `atherysbattlegrounds.team.info`  | Shows info on own current team ( same as `/team` ) |
| `/team join <team-id>` | `atherysbattlegrounds.team.join`  | Join the provided team                             |
| `/team leave`          | `atherysbattlegrounds.team.leave` | Leave current team                                 |

### Admin Commands

| Aliases                        | Permission                         | Description                                   |
|--------------------------------|------------------------------------|-----------------------------------------------|
| `/team add <player> <team-id>` | `atherysbattlegrounds.team.add`    | Add another player to the provided team       |
| `/team remove <player>`        | `atherysbattlegrounds.team.remove` | Remove another player from their current team |