package aml.hdmi_in;

public class hdmi_in_api {
	public native void init();
	public native int display_hdmi();
	public native int display_android();
	public native int display_pip(int x, int y, int width, int height);
	public native int get_h_active();
	public native int get_v_active();
	public native boolean is_dvi();
	public native boolean is_interlace();
	public native int enable_audio(int flag);
}
