package ch.zhaw.pm2.racetrack.exceptions;

public class NoTracksFoundException extends Exception {
  public NoTracksFoundException() {
    super("Could not find any tracks.");
  }
}
