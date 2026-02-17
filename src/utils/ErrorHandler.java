package utils;

public class ErrorHandler {
    private int errors = 0;
    public void report(int line, int col, String message) {
        errors++;
        System.err.printf("[ERROR] Line %d, Col %d: %s\n", line, col, message);
    }
    public int getCount() { return errors; }
}