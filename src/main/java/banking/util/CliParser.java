package banking.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The CliParser class parses command line arguments and stores them in a map
 */
public class CliParser {

    private final Map<String, String> argValues;

    public CliParser(String[] arguments) {
        argValues = new HashMap<>();
        parse(arguments);
    }

    /**
     * The function iterates through the arguments array and checks if the argument starts with a dash.
     * If it does, it adds the argument and its value to the argValues map
     */
    private void parse(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-")) {
                argValues.put(args[i], args[i + 1]);
            }
        }
    }

    /**
     * If the argument is present, return an Optional of the value, otherwise return an empty Optional
     *
     * @param arg The name of the argument.
     * @return An Optional<String> object.
     */
    public Optional<String> optionOf(String arg) {
        return Optional.ofNullable(argValues.get(arg));
    }
}
