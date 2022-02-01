package banking.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CliParser {

    private final Map<String, String> argValues;

    public CliParser(String[] arguments) {
        argValues = new HashMap<>();
        parse(arguments);
    }

    private void parse(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-")) {
                argValues.put(args[i], args[i + 1]);
            }
        }
    }

    public Optional<String> optionOf(String arg) {
        return Optional.ofNullable(argValues.get(arg));
    }
}
