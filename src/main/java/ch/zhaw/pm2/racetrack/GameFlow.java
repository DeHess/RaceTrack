package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.exceptions.EveryoneChoseDoNotMoveStrategyException;
import ch.zhaw.pm2.racetrack.exceptions.InvalidTrackFormatException;
import ch.zhaw.pm2.racetrack.exceptions.NoTracksFoundException;
import ch.zhaw.pm2.racetrack.game.Car;
import ch.zhaw.pm2.racetrack.game.Game;
import ch.zhaw.pm2.racetrack.game.PositionVector;
import ch.zhaw.pm2.racetrack.game.Track;
import ch.zhaw.pm2.racetrack.given.ConfigSpecification;
import ch.zhaw.pm2.racetrack.io.Terminal;
import ch.zhaw.pm2.racetrack.io.UserInterface;
import ch.zhaw.pm2.racetrack.strategy.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * The gameplay is defined by this class
 * It handles the setup of the game and execution
 */
public class GameFlow {
  private final UserInterface ui;
  private final Config config;
  private Track track;

  private final static int EXIT_FAILURE = 1;
  private final static int EXIT_SUCCESS = 0;

  public GameFlow() {
    ui = Terminal.getInstance();
    config = new Config();
  }

  public static void main(String[] args) throws Exception {
    boolean wantsToPlayAgain;
    GameFlow gameFlow = new GameFlow();

    do {
      gameFlow.runNewGame();
      wantsToPlayAgain = Terminal.getInstance().getBoolean("Do you want to play again?");
      Terminal.getInstance().clear();
    } while (wantsToPlayAgain);

    gameFlow.stopGame(EXIT_SUCCESS);
  }

  /**
   * Starts and runs a new game
   */
  public void runNewGame() throws Exception {
    Game game = setup();
    run(game);
  }

  /**
   * Stops the game
   *
   * @param exitStatus the exit status with which you want to terminate the program
   */
  public void stopGame(int exitStatus) {
    System.exit(exitStatus);
  }

  /**
   * Represents the setup phase of the game
   * Has to be executed before running a game
   *
   * @return Game object with completed setup phase
   */
  private Game setup() throws Exception {
    ui.displayMessage("setup phase");
    track = initializeTrack();
    boolean hasToAskAgain;

    do {
      try {
        assert track != null;
        initializeCars(track);
        hasToAskAgain = false;
      } catch (EveryoneChoseDoNotMoveStrategyException e) {
        ui.clear();
        ui.displayMessage(uiErrorMessage(e));
        hasToAskAgain = true;
        Thread.sleep(2000);
      }
    } while (hasToAskAgain);

    Game game = new Game();
    game.setTrack(track);
    return game;
  }

  /**
   * Configures every car for a given track
   * Each player select a move strategy for his car
   *
   * @param track object to be setup
   */
  private void initializeCars(Track track) throws Exception {
    int carCount = track.getCarCount();
    List<MoveStrategy> chosenMoveStrategies = new ArrayList<>();
    for (int i = 0; i < carCount; i++) {
      ui.clear();
      ui.displayMessage("Car " + track.getCar(i).getId());
      setCarMoveStrategy(track, i);
      chosenMoveStrategies.add(track.getCar(i).getMoveStrategy());
    }
    checkIfEveryBodyChoseTheDoNotMoveStrategy(carCount, chosenMoveStrategies);
  }

  private void setCarMoveStrategy(Track track, int i) {
    MoveStrategy moveStrategy = getMoveStrategy(track.getCar(i).getPosition());
    track.getCar(i).setMoveStrategy(moveStrategy);
  }

  private void checkIfEveryBodyChoseTheDoNotMoveStrategy(int carCount, List<MoveStrategy> moveStrategies) throws EveryoneChoseDoNotMoveStrategyException {
    List<MoveStrategy> initializedDoNotMoveStrategies = moveStrategies
            .stream()
            .filter((moveStrategy) -> moveStrategy instanceof DoNotMoveStrategy)
            .toList();

    if (initializedDoNotMoveStrategies.size() == carCount) {
      throw new EveryoneChoseDoNotMoveStrategyException();
    }
  }

  /**
   * Lets the player select MoveStrategy for his car
   *
   * @return MoveStrategy chosen by the player
   */
  public MoveStrategy getMoveStrategy(PositionVector startPosition) {
    while (true) {
      ConfigSpecification.StrategyType moveStrategyType = ui.getEnum(
              ConfigSpecification.StrategyType.class, "Choose a movement strategy:"
      );
      switch (moveStrategyType) {
        case USER -> {
          return new UserMoveStrategy(ui);
        }
        case DO_NOT_MOVE -> {
          return new DoNotMoveStrategy();
        }
        case MOVE_LIST -> {
          MoveListStrategy moveList = getMoveListStrategy();
          if (moveList != null) return moveList;
        }
        case PATH_FOLLOWER -> {
          PathFollowerMoveStrategy followerList = getPathFollowerMoveStrategy(startPosition);
          if (followerList != null) return followerList;
        }
        case PATH_FINDER -> {
          return new PathFinderStrategy(track.getGrid(), startPosition);
        }
      }
    }
  }

  private MoveListStrategy getMoveListStrategy() {
    File[] files = config.getMoveDirectory().listFiles((dir, name) -> name.endsWith(".txt"));
    File moveList = ui.getFile(files, "Choose a move list:");
    try {
      return new MoveListStrategy(moveList);
    } catch (Exception e) {
      ui.displayMessage(uiErrorMessage(e));
    }
    return null;
  }

  private PathFollowerMoveStrategy getPathFollowerMoveStrategy(PositionVector startPosition) {
    File[] files = config.getFollowerDirectory().listFiles((dir, name) -> name.endsWith(".txt"));
    File followerList = ui.getFile(files, "Choose a Follower list:");
    try {
      return new PathFollowerMoveStrategy(followerList, startPosition);
    } catch (Exception e) {
      ui.displayMessage(uiErrorMessage(e));
    }
    return null;
  }

  /**
   * Initializes a track chosen by the players
   *
   * @return Track obj chosen
   */
  private Track initializeTrack() throws InterruptedException {
    boolean couldNotFindTracks = false;
    while (true) {
      try {
        File[] foundTracks = config.getTrackDirectory().listFiles((dir, name) -> name.endsWith(".txt"));
        assert foundTracks != null;
        if (foundTracks.length == 0) {
          throw new NoTracksFoundException();
        }
        File chosenTrack = ui.getFile(foundTracks, "Choose a track:");
        return new Track(chosenTrack);
      } catch (FileNotFoundException | InvalidTrackFormatException e) {
        ui.displayError(uiErrorMessage(e));
      } catch (NoTracksFoundException e) {
        ui.displayError(uiErrorMessage(e) + " Terminating program...");
        couldNotFindTracks = true;
        break;
      }
    }

    if (couldNotFindTracks) {
      Thread.sleep(2000);
      stopGame(EXIT_FAILURE);
    }
    return null;
  }

  /**
   * Represents the run phase of the game
   * Each player selects a move until someone won the game
   *
   * @param game Game obj which should be run
   */
  private void run(Game game) {
    ui.clear();
    ui.displayMessage("run phase");
    do {
      displayTrack(game);
      Car currentCar = game.getTrack().getCar(game.getCurrentCarIndex());
      ui.displayMessage("Current car: " + currentCar.getId() + ", pos:" + currentCar.getPosition() + ", velocity: " + currentCar.getVelocity());
      PositionVector.Direction dir = currentCar.getMoveStrategy().nextMove();
      game.doCarTurn(dir);
      game.switchToNextActiveCar();
    } while (game.getWinner() == -1);
    displayTrack(game);
    ui.displayMessage("Car " + game.getCarId(game.getWinner()) + " won!");
  }

  private void displayTrack(Game game) {
    ui.clear();
    ui.displayMessage(game.getTrack().toString());
  }

  private String uiErrorMessage(Exception exception) {
    return uiErrorMessage(exception.getMessage());
  }

  private String uiErrorMessage(String message) {
    return String.format("Error: %s", message);
  }
}
