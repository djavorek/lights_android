package doublevv.lights.fragments;

public enum FunctionFragment {
    IDLE("idle"),
    UNAVAILABLE("unavailable"),
    COLOR("color"),
    FADE("fade"),
    SLEEP("sleep");

    private final String tag;

    FunctionFragment(String tag) {
        this.tag = tag;
    }

    public String getTag()
    {
        return tag;
    }
}
