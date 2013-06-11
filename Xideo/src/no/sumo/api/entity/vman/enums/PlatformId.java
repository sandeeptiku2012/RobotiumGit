package no.sumo.api.entity.vman.enums;

public enum PlatformId {
	UNKNOWN,
    WEBTV,
    MOBILE,
    IPTV,
    MCE,
    TABLET,
    STB;

    public static PlatformId findPlatformIdWith(int ordinal) {
        for (PlatformId p : values()) {
            if (p.ordinal() == ordinal) {
                return p;
            }
        }
        return UNKNOWN;
    }
}