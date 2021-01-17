package com.groep6.pfor.models;

import java.util.Random;

/**
 * Dice for Pandemic: Fall of Rome. Each face on this dice represents an outcome
 * in battle. A DiceFace is returned when rolled.
 * @author Mitchell van Rijswijk
 *
 */
public class Dice {
	
	DiceFace[] diceFaces = new DiceFace[6];
	Random rolledDiceFace = new Random();
	
	/**
	 * Initialise dice faces on creation.
	 * 
	 */
	public Dice() {
		setDiceFaces();
	}
	
	/**
	 * Initialise faces. BARBARIAN face is put on the dice twice. 
	 * 
	 */
	public void setDiceFaces() {
		diceFaces[0] = DiceFace.BARBARIAN;
		diceFaces[1] = DiceFace.BARBARIAN;
		diceFaces[2] = DiceFace.LEGION;
		diceFaces[3] = DiceFace.BOTH;
		diceFaces[4] = DiceFace.TWO_BARBARIAN_LEGION;
		diceFaces[5] = DiceFace.SPECIAL;
	}
	
	/**
	 * Rolls dice and yields an outcome of a battle.
	 * @return DiceFace representation of the outcome in a battle.
	 * 
	 */
	public DiceFace determineBattleOutcome(City cityPlayerIsCurrentlyStandingIn) {
		int rolledDiceFaceIndex = roleDice();
		DiceFace rolledDiceFace = getRolledDiceFace(rolledDiceFaceIndex);
		executeDiceFace(cityPlayerIsCurrentlyStandingIn, rolledDiceFace);
		return rolledDiceFace;
	}

	private int roleDice() {
		return rolledDiceFace.nextInt(6);
	}

	private DiceFace getRolledDiceFace(int rolledDiceFaceIndex) {
		return diceFaces[rolledDiceFaceIndex];
	}

	private void executeDiceFace(City cityPlayerIsCurrentlyStandingIn, DiceFace rolledDiceFace) {
		rolledDiceFace.execute(cityPlayerIsCurrentlyStandingIn);
	}
	
}
