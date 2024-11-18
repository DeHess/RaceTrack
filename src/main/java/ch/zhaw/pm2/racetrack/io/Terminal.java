package ch.zhaw.pm2.racetrack.io;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

import java.io.File;

/**
 * This implementation uses the TextIO library to provide a UserInterface as a terminal
 * The terminal is implemented as a singleton.
 * It's accessible through Terminal.getInstance().
 * On this instance public methods can be called.
 */
public class Terminal implements UserInterface {
  private static Terminal instance;
  private final TextIO textIO;
  private final TextTerminal<?> textTerminal;
  private static final String CLEAR_TERMINAL = "cls";
  private static final int PANE_WIDTH = 700;
  private static final int PANE_HEIGHT = 700;
  private static final String BG_COLOR = "white";
  private static final String FG_COLOR = "black";
  private static final String FG_COLOR_ERR = "red";
  private static final char TRACK_WALL = '\u2588';

  /**
   * Standard constructor, initializes the ui.
   */
  private Terminal() {
    textIO = TextIoFactory.getTextIO();
    textTerminal = textIO.getTextTerminal();
    textTerminal.setBookmark(CLEAR_TERMINAL);
    textTerminal.getProperties().setPaneDimension(PANE_WIDTH, PANE_HEIGHT);
    textTerminal.getProperties().setPromptColor(FG_COLOR);
    textTerminal.getProperties().setPaneBackgroundColor(BG_COLOR);
    textTerminal.getProperties().setInputColor(FG_COLOR);
  }

  /**
   * Gets the terminal instance
   *
   * @return Terminal instance
   */
  public static Terminal getInstance() {
    if (instance == null) {
      instance = new Terminal();
    }

    return instance;
  }

  @Override
  public void displayMessage(String msg) {
    textTerminal.println(msg);
  }

  @Override
  public void displayTrack(String track) {
    track = track.replace('#', TRACK_WALL);
    textTerminal.println(track);
  }

  @Override
  public void displayError(String msg) {
    textTerminal.getProperties().setPromptColor(FG_COLOR_ERR);
    textTerminal.println("\n" + msg + "\n");
    textTerminal.getProperties().setPromptColor(FG_COLOR);
  }

  @Override
  public File getFile(File[] files, String msg) {
    return textIO.<File>newGenericInputReader(null).withNumberedPossibleValues(files).read(msg);
  }

  @Override
  public <T extends Enum<T>> T getEnum(Class<T> e, String msg) {
    return textIO.newEnumInputReader(e).read(msg);
  }

  @Override
  public boolean getBoolean(String msg) {
    return textIO.newBooleanInputReader().read(msg);
  }

  @Override
  public void clear() {
    textTerminal.resetToBookmark(CLEAR_TERMINAL);
  }

}