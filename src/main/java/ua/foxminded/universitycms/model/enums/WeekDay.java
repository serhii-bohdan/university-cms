package ua.foxminded.universitycms.model.enums;

/**
 * The {@code WeekDay} enum represents the days of the week.
 * <p>
 * This enum includes seven constants, one for each day of the week. Each
 * constant is associated with a {@code String} that represents the name of the
 * day.
 * <p>
 * The {@code toString()} method is overridden to return the name of the day
 * associated with each constant.
 *
 * @author Serhii Bohdan
 */
public enum WeekDay {

    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday"),
    SATURDAY("Saturday"),
    SUNDAY("Sunday");

    private String weekDay;

    /**
     * Constructs a new {@code WeekDay} with the specified name.
     *
     * @param weekDay the name of the day
     */
    private WeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    /**
     * Returns the name of this day of the week.
     *
     * @return the name of this day of the week
     */
    @Override
    public String toString() {
        return this.weekDay;
    }

}