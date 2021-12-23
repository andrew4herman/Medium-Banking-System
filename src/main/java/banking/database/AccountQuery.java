package banking.database;

public enum AccountQuery {
    CREATE_TABLE("""
            CREATE TABLE IF NOT EXISTS account(
                id INTEGER PRIMARY KEY,
                cardNumber TEXT UNIQUE,
                cardPIN TEXT,
                balance INTEGER DEFAULT 0
            );
            """),
    GET("SELECT * FROM account WHERE cardNumber = ? AND cardPin = ?;"),
    GET_ALL("SELECT * FROM account;"),
    SAVE("INSERT INTO account(cardNumber, cardPin) VALUES(?, ?);"),
    UPDATE_BALANCE("UPDATE account SET balance = balance + ? WHERE cardNumber = ?"),
    DELETE("DELETE FROM account WHERE id = ?;");

    private final String query;

    AccountQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
