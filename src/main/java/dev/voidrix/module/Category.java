package dev.voidrix.module;

/** Top level grouping shown as the sidebar of the Voidrix menu. */
public enum Category {
    HUD("HUD", "Readouts drawn over the game"),
    COMBAT("Combat", "Readouts for fights - all display only"),
    VISUAL("Visual", "How the world and your view look"),
    INTERFACE("Interface", "Menus, inventory and on-screen furniture"),
    CHAT("Chat", "Chat window behaviour"),
    MISC("Misc", "Everything else");

    private final String displayName;
    private final String description;

    Category(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String displayName() {
        return displayName;
    }

    public String description() {
        return description;
    }
}
