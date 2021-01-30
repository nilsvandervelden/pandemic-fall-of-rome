package com.groep6.pfor.models;

import com.groep6.pfor.models.factions.Faction;
import com.groep6.pfor.util.Vector2f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Represents a barbarian base
 *
 * @author Nils van der Velden
 */

public class Base<boardPiece extends Piece> extends Tile {
	private List<boardPiece> boardPieces = new ArrayList<>();

	/**
	 * Initializes a new Base with the given components.
	 * @param positionOfBarbarianBase The Vector2f (positionOfBarbarianBase) of a specific base
	 * @param factionsAllowedInBarbarianBase What faction is allowed in a specific base
	 */
	public Base(Vector2f positionOfBarbarianBase, Faction[] factionsAllowedInBarbarianBase) {
		super(positionOfBarbarianBase, factionsAllowedInBarbarianBase);
	}

	/**
	 * Intializes a new Base from Firebase data
	 * @param factionsAllowedInBarbarianBase the factionsAllowedInBarbarianBase allowed in this base
	 * @param boardPieces The pieces in the base
	 */
	public Base(Faction[] factionsAllowedInBarbarianBase, boardPiece... boardPieces) {
		super(null, factionsAllowedInBarbarianBase);
		addBarbariansToBarbarianBase(boardPieces);
	}

	private void addBarbariansToBarbarianBase(boardPiece... boardPieces) {
		this.boardPieces.addAll(Arrays.asList(boardPieces));
	}

	/**
	 * Overwrite the pieces in this base with those in a Firebase instance
	 * @param base The firebase instance of the base
	 */
	public void updateBase(Base base) {
		this.boardPieces = base.boardPieces;
	}

	public Faction getFactionAllowedInBase() {
		return factionsAllowedOnTile[0];
	}
	
    /**
     * @returns the amount of barbarians in a base
     */
	public int getPieceCount() {
		return boardPieces.size();
	}
}
