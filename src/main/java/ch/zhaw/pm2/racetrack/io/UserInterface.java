package ch.zhaw.pm2.racetrack.io;

import java.io.File;

/**
 * User interface which provides feedback to the user and the ability to get inputs from the user
 */
public interface UserInterface {

  /**
   * Displays a string as a message on the ui
   *
   * @param msg message to be displayed
   */
  void displayMessage(String msg);

  /**
   * Displays the track on the ui
   * Formats the string for better readability
   *
   * @param track String representing the track
   */
  void displayTrack(String track);

  /**
   * Displays an error message on the ui
   * Formats the message to stand out
   *
   * @param msg String representing the error message
   */
  void displayError(String msg);

  /**
   * Helper method to let the user select a file from a list of files
   *
   * @param files List of files to chose from
   * @param msg   String representing the prompt message
   * @return File selected by the user
   */
  File getFile(File[] files, String msg);

  /**
   * Helper method to let the user select an enum from an enum class
   *
   * @param msg String representing the prompt message
   * @param <T> enum Class to chose from
   * @return Enum selected by the user
   */
  <T extends Enum<T>> T getEnum(Class<T> e, String msg);

  /**
   * Helper method to clear the ui
   */
  void clear();

  /**
   * Lets the user select boolean ( yes or no)
   *
   * @param msg String prompt displayed
   * @return Selected boolean
   */
  boolean getBoolean(String msg);
}
