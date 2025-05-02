package one.oth3r.otterlib.base;

public class Num {

    public static boolean isInt(String string) {
        try {
            Integer.parseInt(string);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static Integer toInt(String s) {
        // return an int no matter what
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            try {
                return (int) Double.parseDouble(s);
            } catch (NumberFormatException e2) {
                return 0;
            }
        }
    }

    public static boolean isNum(String s) {
        // checks if int or a double
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e1) {
            try {
                Double.parseDouble(s);
                return true;
            } catch (NumberFormatException e2) {
                return false;
            }
        }
    }

    public static boolean inBetween(double num, double min, double max) {
        // if min is greater than max, flip
        if (min > max) return num >= min || num <= max;
        return num >= min && num <= max;
    }

    public static double wSubtract(double num, double sub, double max) {
        // wrapped subtract
        double output = num - sub;
        if (output < 0) output = max - (output*-1);
        return output;
    }

    public static double wAdd(double num, double add, double max) {
        // wrapped add
        return (num+add)%max;
    }

    /**
     * formats a given number to have a specific number of digits (good for clocks etc.) <br>
     * the method adds leading 0's as necessary, and if the original number is too big, it will be cut off
     *
     * @param number the number to be formatted
     * @param digits the total number of digits the output string should have
     * @return a formatted string of the number with the specified number of digits
     */
    public static String formatToXDigits(int number, int digits) {
        // put digit -1 amount of zeros in front of the original number
        String result = "0".repeat(Math.max(0,digits-1)) + number;
        // return the string shortened to the right size
        return result.substring(result.length() - digits);
    }

    public static String formatToTwoDigits(int number) {
        return formatToXDigits(number,2);
    }
}
