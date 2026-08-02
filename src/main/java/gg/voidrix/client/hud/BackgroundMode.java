package gg.voidrix.client.hud;

/**
 * Hintergrund-Variante eines HUD-Elements.
 * Wird im Detail-Screen ueber ein Dropdown gewaehlt.
 */
public enum BackgroundMode {

	/** Dunkle, weiche Flaeche - wirkt wie ein Blur hinter dem Element. */
	BLUR,

	/** Volle Flaeche in der eingestellten Hintergrundfarbe. */
	COLOR,

	/** Gar kein Hintergrund - nur der Text. */
	BLANK
}
