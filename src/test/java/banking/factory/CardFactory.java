package banking.factory;

import banking.model.Card;

public class CardFactory {

    public static String CARD_NUMBER = "4000005048166830";
    public static String CARD_PIN = "3276";

    public static Card createCard() {
        return new Card(CARD_NUMBER, CARD_PIN);
    }

}
