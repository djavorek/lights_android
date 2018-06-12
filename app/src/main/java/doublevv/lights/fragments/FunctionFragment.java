package doublevv.lights.fragments;

public enum FunctionFragment {
    UNAVAILABLE("unavailable"),
    IDLE("idle"),
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
