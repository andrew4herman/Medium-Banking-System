package banking.database;

public enum DBQuery {
    CREATE_TABLE("""
            CREATE TABLE IF NOT EXISTS card(
                id INTEGER PRIMARY KEY,
                number TEXT,
                pin TEXT,
                balance INTEGER DEFAULT 0
            );
            """),
    INSERT_INTO("""
            INSERT INTO
                card(number, pin)
            VALUES(?, ?);
            """),
    SELECT_ALL("""
            SELECT * FROM card;
            """),
    SELECT_VALUE("""
            SELECT %s FROM card WHERE number = ?;
            """),
    UPDATE_VALUE("""
            UPDATE card SET %s = ? WHERE number = ?;
            """),
    DELETE_ROW("""
            DELETE FROM card WHERE number = ?;
            """);

    DBQuery(String query) {
        this.query = query;
    }

    private String query;

    public String getQuery() {
        return query;
    }
}
