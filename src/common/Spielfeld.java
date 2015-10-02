package common;

import java.io.Serializable;

public class Spielfeld implements Serializable {
	private static final long serialVersionUID = 1L;
	private Felder[][] feld = new Felder[10][10];
	public final static int W = 30;

	public Spielfeld() {
		init();

	}

	private void init() {

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				feld[i][j] = Felder.NORMAL;
			}
		}
	}

	public boolean canBePlaced(int x, int y) {
		if (feld[y][x] == Felder.VERFEHLT || feld[y][x] == Felder.BOMBE) {
			return false;
		}
		return true;
	}

	public void setField(int x, int y, Felder art) {
		if (art == Felder.NORMAL) {
			feld[y][x] = Felder.VERFEHLT;
		} else {
			feld[y][x] = Felder.BOMBE;
		}
	}

	public Felder[][] getFelder() {
		return feld;
	}

	public boolean bomb(int x, int y) {
		if (feld[y][x] == Felder.BOMBE)
			return true;
		else
			return false;
	}

}