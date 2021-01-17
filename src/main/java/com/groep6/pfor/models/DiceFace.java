package com.groep6.pfor.models;

/**
 * Faces of the dice in Pandemic: Fall of Rome. BARBARIAN face is on the dice twice.
 * @author Mitchell van Rijswijk
 *
 */
public enum DiceFace {
	BARBARIAN {
		@Override
		public void execute(City city) {
			city.removeBarbariansFromCurrentCity(1);
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
		public void execute(City city) {
			city.removeLegions(1);
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
		public void execute(City city) {
			city.removeBarbariansFromCurrentCity(1);
			city.removeLegions(1);
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
		public void execute(City city) {
			city.removeBarbariansFromCurrentCity(2);
			city.removeLegions(1);
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
		public void execute(City city) {
			Game game = Game.getInstance();
			Player player =  game.getPlayerFromCurrentTurn();
			player.getRoleCard().executeRoleCardAbility();
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
	
	public abstract void execute(City city);
	public abstract int getBarbarianCount();
	public abstract int getLegionCount();
}
