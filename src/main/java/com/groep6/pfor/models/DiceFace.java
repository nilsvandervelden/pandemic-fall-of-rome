package com.groep6.pfor.models;

/**
 * Faces of the dice in Pandemic: Fall of Rome. BARBARIAN face is on the dice twice.
 * @author Mitchell van Rijswijk
 *
 */
public enum DiceFace {
	BARBARIAN {
		@Override
		public void executeDiceAction(City currentCity) {
			removeBarbariansFromCurrentCity(currentCity, 1);
		}
		@Override
		public int getBarbarianCount() {
			return 1;
		}
		@Override
		public int getLegionCount() {
			return 0;
		}
	},
	LEGION {
		@Override
		public void executeDiceAction(City currentCity) {
			removeLegionsFromCurrentCity(currentCity, 1);
		}
		@Override
		public int getBarbarianCount() {
			return 0;
		}
		@Override
		public int getLegionCount() {
			return 1;
		}
	},
	BOTH {
		@Override
		public void executeDiceAction(City currentCity) {
			removeBarbariansFromCurrentCity(currentCity,1);
			removeLegionsFromCurrentCity(currentCity, 1);
		}
		@Override
		public int getBarbarianCount() {
			return 1;
		}
		@Override
		public int getLegionCount() {
			return 1;
		}
	},
	TWO_BARBARIAN_LEGION {
		@Override
		public void executeDiceAction(City currentCity) {
			removeBarbariansFromCurrentCity(currentCity,2);
			removeLegionsFromCurrentCity(currentCity, 1);
		}
		@Override
		public int getBarbarianCount() {
			return 2;
		}
		@Override
		public int getLegionCount() {
			return 2;
		}
	},
	SPECIAL {
		@Override
		public void executeDiceAction(City currentCity) {
			Game currentGame = Game.getGameInstance();
			Player playerFromCurrentTurn = currentGame.getPlayerFromCurrentTurn();
			executeRoleCardAbility(playerFromCurrentTurn);
		}
		@Override
		public int getBarbarianCount() {
			return 0;
		}
		@Override
		public int getLegionCount() {
			return 0;
		}
	};

	private static void executeRoleCardAbility(Player playerFromCurrentTurn) {
		playerFromCurrentTurn.getPlayerRole().executeRoleCardAbility();
	}

	private static void removeBarbariansFromCurrentCity(City currentCity, int amountOfBarbariansToRemove) {
		currentCity.removeBarbariansFromCurrentCity(amountOfBarbariansToRemove);
	}
	private static void removeLegionsFromCurrentCity(City currentCity, int amountOfLegionsToRemove) {
		currentCity.removeLegions(amountOfLegionsToRemove);
	}

	public abstract void executeDiceAction(City city);
	public abstract int getBarbarianCount();
	public abstract int getLegionCount();
}
