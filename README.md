# Race Track

## Execution

The programm should be executed with gradle. Either use the gradle run task or
enter ```./ gradle run ``` in the console

## Playing

### Rules

- The car which crosses the finish line first wins
- Or the last car standing wins
- If a car touches a Border it crashes

### Setup phase

1. select a track file
    - The track directory can be found under ```./tracks```
2. select a move strategy for each car
    - ```DO_NOT_MOVE```: car wont move
    - ```USER```: you can select a direction for each move
    - ```MOVE_LIST```: directions for each move are predefined
    - ```PATH_FOLLOWER```: The car follows a path defined through different
      predefined waypoints
    - ```PATH_FINDER```: The path to the finish line is calculated automatically

### Game phase

The goal of the game is to get over the finishing line first without crashing
into a wall or any other player. If you crash into a wall or another player,
you'll lose automatically.

### Controls

The controls for this game are simple:

- 1: ```DOWN_LEFT```
- 2: ```DOWN```
- 3: ```DOWN_RIGHT```
- 4: ```LEFT```
- 5: ```NONE```
- 6: ```RIGHT```
- 7: ```UP_LEFT```
- 8: ```UP```
- 9: ```UP_RIGHT```

If you use the numpad, it's gonna be fairly easy, because the numbers there
already point in the right direction

## Diagrams

### Class Diagram

https://github.zhaw.ch/PM2-IT21aWIN-fame-rayi-wahl/IT21aWIN-PM2-Team04-QuattroProgrammieri-RaceTrack/files/564/racetrack.zip
