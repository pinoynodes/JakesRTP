package biz.donvi.jakesRTP;

import java.util.HashMap;

public class CoolDownTracker {

    private final HashMap<String, Long> tracker = new HashMap<>();
    final long coolDownTime;

    /**
     * Creates a CoolDownTracker object, and sets the coolDownTime.
     *
     * @param coolDownTimeInSeconds The amount of time that must be waited to get True from a check.
     */
    CoolDownTracker(double coolDownTimeInSeconds) {
        coolDownTime = (long) (coolDownTimeInSeconds * 1000);
    }

    /**
     * Checks if between now and the last logged time is greater or less than the last logged time.
     * No changes will be made to the data regardless of the result.
     *
     * @param playerName Name of the player/user to check.
     * @return If the difference in time between now and the last logged time is greater than the cool down.
     */
    boolean check(String playerName) {
        Long time = tracker.get(playerName);
        long currentTime = System.currentTimeMillis();
        return time == null || currentTime - time > coolDownTime;
    }

    /**
     * Checks if the time difference between now and the last logged time is greater or less than
     * the last logged time. If true (or this is the first time) the current time will be logged.
     *
     * @param playerName Name of the player/user to check.
     * @return If the difference in time between now and the last logged time is greater than the cool down.
     */
    boolean checkAndLog(String playerName) {
        Long time = tracker.get(playerName);
        long currentTime = System.currentTimeMillis();
        if (time == null || currentTime - time > coolDownTime) {
            tracker.put(playerName, currentTime);
            return true;
        }
        return false;
    }

    /**
     * Sets the logged time for the given player to the current time.
     *
     * @param playerName Player to log time for.
     */
    void log(String playerName) {
        log(playerName, System.currentTimeMillis());
    }

    /**
     * Sets the logged time for the given player to the given time.
     *
     * @param playerName Player to log time for.
     * @param time       The time to log.
     */
    void log(String playerName, long time) {
        tracker.put(playerName, time);
    }

    /**
     * Returns the amount of time between now and the last successful log, or -1 if no time was ever logged.
     *
     * @param playerName The player to check.
     * @return The amount of time between now and the last logged time, or -1 if no time was ever logged.
     */
    long timeDifference(String playerName) {
        return timeDifference(playerName, System.currentTimeMillis());
    }

    /**
     * Returns the amount of time between the given time and the last successful log, or -1 if no time was ever logged.
     *
     * @param playerName The player to check.
     * @return The amount of time between given time and the last logged time, or -1 if no time was ever logged.
     */
    long timeDifference(String playerName, long time) {
        Long oldTime = tracker.get(playerName);
        if (oldTime == null) return -1;
        return time - oldTime;
    }

    /**
     * Returns the minimum amount of time to wait (in milliseconds) before being able to call a successful (true) check.
     * Negative values mean a check would return true.
     *
     * @param playerName The player to check.
     * @return Returns the minimum amount of time to wait before being able to call a successful check.
     */
    long timeLeft(String playerName) {
        return coolDownTime - timeDifference(playerName);
    }

    /**
     * Returns the amount of time that the given player must wait, formatted in a reasonable manor.
     *
     * @param playerName The player to check.
     * @return A string describing the time left until the cooldown is over. Human readable.
     */
    String timeLeftWords(String playerName) {
        int days, hours, minutes, seconds;
        long millis = timeLeft(playerName);
        seconds = (int) (millis / 1000) % 60;
        minutes = (int) (millis / (1000 * 60)) % 60;
        hours = (int) (millis / (1000 * 60 * 60)) % 24;
        days = (int) (millis / (1000 * 60 * 60 * 24));
        return Messages.READABLE_TIME.format(
                (days > 0 ? Messages.READABLE_TIME_WORD_DAYS.format(days) : ""),
                (hours > 0 ? Messages.READABLE_TIME_WORD_HOURS.format(hours) : ""),
                (minutes > 0 ? Messages.READABLE_TIME_WORD_MINUTES.format(minutes) : ""),
                (seconds > 0 ? Messages.READABLE_TIME_WORD_SECONDS.format(seconds) : ""));
    }

    //TODO - Make this method and the one above use a method instead of having duplicate code
    public String coolDownTime() {
        if (coolDownTime == 0) return "None.";
        int days, hours, minutes, seconds;
        long millis = coolDownTime;
        seconds = (int) (millis / 1000) % 60;
        minutes = (int) (millis / (1000 * 60)) % 60;
        hours = (int) (millis / (1000 * 60 * 60)) % 24;
        days = (int) (millis / (1000 * 60 * 60 * 24));
        return Messages.READABLE_TIME.format( //TODO - This is ugly. This needs to not be ugly.
                (days > 0 ? Messages.READABLE_TIME_WORD_DAYS.format(days) : ""),
                (hours > 0 ? Messages.READABLE_TIME_WORD_HOURS.format(hours) : ""),
                (minutes > 0 ? Messages.READABLE_TIME_WORD_MINUTES.format(minutes) : ""),
                (seconds > 0 ? Messages.READABLE_TIME_WORD_SECONDS.format(seconds) : ""));
    }
}
