package zawaski;

import java.util.List;

public class BattleSystemView {
	public static void showPlayerHandCards(List<Card> handCards) {
        if (handCards.isEmpty()) {
            System.out.println("Your hand is empty.");
            return;
        }
        System.out.println("Cards in hand:");
        for (int i = 0; i < handCards.size(); i++) {
            Card card = handCards.get(i);
            System.out.println((i + 1) + ". " + card.getCardName() + " (AP Cost: " + card.getApCost() + ")");
        }
    }
	
	public static void showPlayerTurn(BattleSystem battleSystem) {
		System.out.println();
		System.out.println("Your turn.");
        System.out.println(battleSystem.getPlayer().getName() + "'s HP: " + battleSystem.getPlayer().getStatus().getHp() + "/" + battleSystem.getPlayer().getStatus().getMaxHp() + " | " + battleSystem.getPlayer().getName() + "'s AP: " + battleSystem.getPlayer().getStatus().getAp() + "/" + battleSystem.getPlayer().getStatus().getMaxAp());
        System.out.println(battleSystem.getEnemy().getName() + "'s HP: " + battleSystem.getEnemy().getStatus().getHp() + "/" + battleSystem.getEnemy().getStatus().getMaxHp());
        BattleSystemView.showPlayerHandCards(battleSystem.getPlayerHandCards().getItems());
	}
}
